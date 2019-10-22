package com.example.ftpmanage.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ftpmanage.CustomPopup;
import com.example.ftpmanage.FtpUtils;
import com.example.ftpmanage.ImageActivity;
import com.example.ftpmanage.LocalAdapter;
import com.example.ftpmanage.R;
import com.example.ftpmanage.entity.BackHandlerHelper;
import com.example.ftpmanage.entity.FTPFileExt;
import com.example.ftpmanage.entity.FileExt;
import com.example.ftpmanage.entity.FolderEntity;
import com.example.ftpmanage.entity.FragmentBackHandler;
import com.example.ftpmanage.entity.FtpDirListArray;
import com.example.ftpmanage.entity.LocalFileArray;
import com.example.ftpmanage.utils.AppUtil;
import com.example.ftpmanage.utils.ConstantUtil;
import com.example.ftpmanage.utils.FileProvider;
import com.example.ftpmanage.utils.ImgUtil;
import com.example.ftpmanage.utils.UiUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalFragment extends Fragment implements FragmentBackHandler {

    private List<String> pathList;

    private List<LocalFileArray> localList = new ArrayList<>();

    private View root;

    private Toolbar toolbar;

    private AppCompatActivity appCompatActivity;

    private RecyclerView mRecyclerView;

    private LocalAdapter localAdapter;

    private GridLayoutManager mLayoutManager;

    private ProgressDialog pd = null;

    private String localDir = "";

    private List<FileExt> files;

    public static boolean isFirst = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isFirst = true;
        root = inflater.inflate(R.layout.activity_local, container, false);
        appCompatActivity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true);
        toolbar = root.findViewById(R.id.l_toolbar);
        toolbar.inflateMenu(R.menu.menu_local);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        initView();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            loadFileList();
            isFirst = false;
        }
    }

    private void initView() {
        mRecyclerView = root.findViewById(R.id.l_recyclerView);
        mLayoutManager = new GridLayoutManager(appCompatActivity, 4);
        mRecyclerView.setLayoutManager(mLayoutManager);
        pd = new ProgressDialog(appCompatActivity);
        if (pathList == null) {
            pathList = new ArrayList<>();
            pathList.add(appCompatActivity.getExternalFilesDir("").getAbsolutePath());
        }
    }

    private List<FileExt> getFileExtList(int index) {
        if (localList.size() > index && index > -1) {
            LocalFileArray arr = localList.get(index);
            String path = arr.getFtpPath();
            if (path.equals(localDir)) {
                return arr.getFextList();
            }
        }
        FileExt fi = new FileExt(localDir);
        List<FileExt> feList = fi.getFileExtList();
        for (int i = 0; i < feList.size(); i++) {
            FileExt fe = feList.get(i);
            if (fe.isDirectory() && fe.getChildCount() < 0) {
                fe.setChildCount(fe.getFileExtList().size());
            }
        }
        return feList;
    }

    private void setFileExtList(int index, List<FileExt> fext, String fpath) {
        if (localList.size() > index && index > -1) {
            LocalFileArray arr = localList.get(index);
            arr.setFextList(fext);
            arr.setFtpPath(fpath);
            localList.set(index, arr);
            return;
        }
        LocalFileArray arry = new LocalFileArray();
        arry.setFextList(fext);
        arry.setFtpPath(fpath);
        localList.add(arry);
        return;
    }

    private void setFileExtList(int index, String fpath) {
        if (localList.size() > index && index > -1) {
            LocalFileArray arr = localList.get(index);
            arr.setFtpPath(fpath);
            localList.set(index, arr);
            return;
        }
        return;
    }

    public void loadFileList() {
        UiUtil.setMenuItemVisible(toolbar, R.id.Menu_Local_Return, false);
        if (pathList.size() > 1) {
            UiUtil.setMenuItemVisible(toolbar, R.id.Menu_Local_Return, true);
        }
        UiUtil.showProgressDialog(pd, "载入中", "载入中,请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                int index = pathList.size() - 1;
                localDir = pathList.get(index);
                files = getFileExtList(index);
                setFileExtList(index, files, localDir);
                msg.what = 4;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void showFileList() {
        localAdapter = new LocalAdapter(appCompatActivity, mLayoutManager, localDir, files);
        localAdapter.setOnItemClickListener(new LocalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int position, final FileExt fe) {
                String fName = fe.getName();
                if (fe.isDirectory()) {
                    pathList.add(localDir + "/" + fName);
                    loadFileList();
                } else {
                    if (AppUtil.isExt(fName, ConstantUtil.IMAGE_ALL_SUFFIX_GATHER)) {
                        String imPath = ImgUtil.getLocalImagePath(fe.getPath());
                        if (AppUtil.isNotEmpty(imPath)) {
                            File f = new File(imPath);
                            Intent intent = new Intent(appCompatActivity, ImageActivity.class);
                            intent.putExtra("fname", f.getName());
                            intent.putExtra("fdir", localDir);
                            intent.putExtra("fpath", imPath);
                            startActivity(intent);
                        }
                    } else {
                        FileProvider.openFileOfApp(appCompatActivity, fe);
                    }
                }
            }

            @Override
            public void onItemLongClick(View v, final int position, final FileExt fe) {
                PopupMenu popupMenu = new PopupMenu(appCompatActivity, v);
                popupMenu.getMenuInflater().inflate(R.menu.long_menu_local, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.Menu_localInfo) {
                            loadFileSize(fe.getPath());
                        } else if (item.getItemId() == R.id.Menu_deleteLocal) {
                            new XPopup.Builder(appCompatActivity).asConfirm("确定删除", "文件删除后不能恢复，确定删除？",
                                    new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            deleteLocalFile(position, fe);
                                        }
                                    }).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        mRecyclerView.setAdapter(localAdapter);
    }

    public void deleteLocalFile(final int position, final FileExt f) {
        UiUtil.showProgressDialog(pd, "删除中", "删除中,请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                try {
                    FileProvider.deleteFile(f);
                    msg.what = 1;
                    data.putString("infoMessage", "删除操作成功！");
                    data.putInt("position", position);
                    msg.setData(data);
                } catch (Exception e) {
                    msg.what = 9999;
                    data.putString("errMessage", "删除失败，失败信息：" + e.getMessage());
                    msg.setData(data);
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            if (msg.what == 1) {
                String infoMessage = data.getString("infoMessage");
                int position = data.getInt("position");
                files.remove(position);
                localAdapter.notifyItemRemoved(position);
                localAdapter.notifyItemRangeChanged(position, files.size());
                UiUtil.showToast(appCompatActivity, infoMessage);
            } else if (msg.what == 2) {
                FolderEntity[] farr = (FolderEntity[]) data.getSerializable("folderEntity");
                FolderEntity fe = farr[0];
                String filesSize = FileProvider.getSize(fe.getCountSzie());
                String containInfo = fe.getFilesSize() + "个文件";
                if (fe.getFileType() == 1) {
                    containInfo = fe.getFoldersSize() + "个文件夹，" + fe.getFilesSize() + "个文件";
                }
                new XPopup.Builder(appCompatActivity).asCustom(new CustomPopup(appCompatActivity, filesSize, containInfo, fe.getFilePath())).show();
            } else if (msg.what == 3) {
                int position = data.getInt("position");
                localAdapter.notifyItemChanged(position, new Object());
            } else if (msg.what == 4) {
                showFileList();
            } else if (msg.what == 9999) {
                String errMessage = data.getString("errMessage");
                UiUtil.showPosition(appCompatActivity, errMessage);
            }
            if (pd != null) {
                pd.hide();
            }
            super.handleMessage(msg);
        }
    };

    public void loadFileSize(final String paths) {
        UiUtil.showProgressDialog(pd, "加载中", "加载中,请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                try {
                    FolderEntity fe = new FolderEntity();
                    FileProvider.getFilesCountSize(fe, paths, 0);
                    FolderEntity[] farr = {fe};
                    msg.what = 2;
                    data.putSerializable("folderEntity", farr);
                    msg.setData(data);
                } catch (Exception e) {
                    msg.what = 9999;
                    data.putString("errMessage", "加载失败，失败信息：" + e.getMessage());
                    msg.setData(data);
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener
            = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.Menu_Local_Refresh) {
                setFileExtList(pathList.size() - 1, "");
                loadFileList();
                return true;
            } else if (id == R.id.Menu_See_Local_Info) {
                loadFileSize(localDir);
                return true;
            } else if (id == R.id.Menu_Local_Return) {
                if (pathList.size() > 1) {
                    backList();
                }
                return true;
            }
            return true;
        }
    };

    private void backList() {
        int delIndex = pathList.size() - 1;
        if (delIndex <= 0) {
            pathList.clear();
        } else {
            pathList.remove(delIndex);
        }
        loadFileList();
    }

    @Override
    public boolean onBackPressed() {
        if (pathList.size() > 1) {
            backList();
            return true;
        }
        return BackHandlerHelper.handleBackPress(this);
    }

}
