package com.example.ftpmanage.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.ftpmanage.CustomPopup;
import com.example.ftpmanage.entity.FTPClientEntity;
import com.example.ftpmanage.entity.FTPFileExt;
import com.example.ftpmanage.FTPTransferListener;
import com.example.ftpmanage.FilesAdapter;
import com.example.ftpmanage.FtpUtils;
import com.example.ftpmanage.ImageActivity;
import com.example.ftpmanage.R;
import com.example.ftpmanage.dfileselector.activity.DefaultSelectorActivity;
import com.example.ftpmanage.entity.BackHandlerHelper;
import com.example.ftpmanage.entity.FragmentBackHandler;
import com.example.ftpmanage.entity.FtpConfig;
import com.example.ftpmanage.entity.FtpDirList;
import com.example.ftpmanage.entity.FtpDirListArray;
import com.example.ftpmanage.entity.FtpFolderEntity;
import com.example.ftpmanage.entity.FtpLock;
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

public class FtpFilesFragment extends Fragment implements FragmentBackHandler {

    private List<String> pathList = new ArrayList<>();

    private List<FtpDirListArray> ftpList = new ArrayList<>();

    private View root;

    private Toolbar toolbar;

    private AppCompatActivity appCompatActivity;

    private PowerManager.WakeLock m_wklk;

    private RecyclerView mRecyclerView;

    private FilesAdapter filesAdapter;

    private GridLayoutManager mLayoutManager;

    private ProgressDialog pd = null;

    private ProgressDialog hpd = null;

    private String ftpPath = "";

    private SQLiteDatabase db;

    private FtpConfig ftpConfig = null;

    private FTPTransferListener ftpListener;

    private VectorDrawableCompat vectorDrawableCompat;

    private String localSaveDir = "";

    public static boolean isFirst = true;

    public FtpFilesFragment(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        isFirst = true;
        super.onCreate(savedInstanceState);
        appCompatActivity = (AppCompatActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isFirst = true;
        root = inflater.inflate(R.layout.activity_folder, container, false);
        setHasOptionsMenu(true);
        toolbar = root.findViewById(R.id.f_toolbar);
        toolbar.inflateMenu(R.menu.menu_ftp);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        PowerManager pm = (PowerManager) appCompatActivity.getSystemService(appCompatActivity.POWER_SERVICE);
        m_wklk = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getName());
        initView();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            loadFTPFileList();
            isFirst = false;
        }
    }

    private void initView() {
        vectorDrawableCompat = VectorDrawableCompat.create(getResources(), R.drawable.svg_check_24dp, appCompatActivity.getTheme());
        vectorDrawableCompat.setTint(getResources().getColor(R.color.colorGreen));
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mLayoutManager = new GridLayoutManager(appCompatActivity, 4);
        mRecyclerView.setLayoutManager(mLayoutManager);
        pd = UiUtil.getSProgressDialog(appCompatActivity);
        hpd = UiUtil.getHProgressDialog(appCompatActivity);
    }

    public void setFtpConfig(String ftpPath, int ftpConfigId) {
        this.ftpPath = ftpPath;
        this.pathList.clear();
        this.ftpList.clear();
        pathList.add(ftpPath);
        ftpConfig = FtpUtils.getFTPConfigById(db, String.valueOf(ftpConfigId));
    }

    public void connectRefreshFTP(final FtpConfig fc) {
        UiUtil.showProgressDialog(pd, "连接中", "连接中,请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                UiUtil.sleep(100);
                Message msg = new Message();
                Bundle data = new Bundle();
                FTPClientEntity.createConnection(fc, appCompatActivity);
                if (FTPClientEntity.client.isErr()) {
                    msg.what = 9999;
                    data.putString("errMessage", FTPClientEntity.client.getErrMessage());
                    msg.setData(data);
                } else {
                    msg.what = 5;
                    data.putInt("ftpConfigId", fc.getFid());
                    data.putString("infoMessage", "重连FTP成功！");
                    msg.setData(data);
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    private List<FTPFileExt> getFTPFileExtList(int index) {
        if (ftpList.size() > index && index > -1) {
            FtpDirListArray arr = ftpList.get(index);
            String path = arr.getFtpPath();
            if (path.equals(ftpPath)) {
                return arr.getFextList();
            }
        }
        return null;
    }

    private void setFTPFileExtList(int index, List<FTPFileExt> fext, String fpath) {
        if (ftpList.size() > index && index > -1) {
            FtpDirListArray arr = ftpList.get(index);
            arr.setFextList(fext);
            arr.setFtpPath(fpath);
            ftpList.set(index, arr);
            return;
        }
        FtpDirListArray arry = new FtpDirListArray();
        arry.setFextList(fext);
        arry.setFtpPath(fpath);
        ftpList.add(arry);
        return;
    }

    private void setFTPFileExtList(int index, String fpath) {
        if (ftpList.size() > index && index > -1) {
            FtpDirListArray arr = ftpList.get(index);
            arr.setFtpPath(fpath);
            ftpList.set(index, arr);
            return;
        }
    }

    public void loadFTPFileList() {
        final int index = pathList.size() - 1;
        UiUtil.setMenuItemVisible(toolbar, R.id.Menu_Ftp_Return, false);
        if (pathList.size() > 1) {
            UiUtil.setMenuItemVisible(toolbar, R.id.Menu_Ftp_Return, true);
        }
        this.ftpPath = pathList.get(index);
        localSaveDir = appCompatActivity.getExternalFilesDir("").getAbsolutePath() + "/" + ftpConfig.getFtpName() + this.ftpPath;
        final List<FTPFileExt> fflist = getFTPFileExtList(index);
        UiUtil.wakeLockAcquire(m_wklk);
        if (fflist != null) {
            showList(fflist);
            return;
        }
        UiUtil.showProgressDialog(pd, "加载中", "加载中,请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                UiUtil.sleep(100);
                Message msg = new Message();
                Bundle data = new Bundle();
                msg.what = 1;
                List<FTPFileExt> fflist = FTPClientEntity.getFTPList(ftpPath);
                if (FTPClientEntity.client.isErr()) {
                    FTPClientEntity.reconnect();
                    fflist = FTPClientEntity.getFTPList(ftpPath);
                    if (FTPClientEntity.client.isErr()) {
                        msg.what = 9999;
                        data.putString("errMessage", FTPClientEntity.client.getErrMessage());
                        msg.setData(data);
                    }
                }
                if (msg.what == 1) {
                    setFTPFileExtList(index, fflist, ftpPath);
                    data.putSerializable("FTPFiles", new FtpDirList(fflist));
                    data.putBoolean("isLoad", true);
                    msg.setData(data);
                    msg.what = 1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void downFTP(final FTPFileExt ffext, final int index) {
        if (ffext.getType() > 0) {
            return;
        }
        ftpListener = new FTPTransferListener(appCompatActivity, fileHandler, 0, ffext.getSize(), 0);
        UiUtil.wakeLockAcquire(m_wklk);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                FileProvider.createrDirectory(localSaveDir);
                String fName = ffext.getName();
                String savePath = localSaveDir + fName;
                if (AppUtil.isExt(fName, ConstantUtil.IMAGE_SUFFIX_GATHER)) {
                    savePath = savePath + ConstantUtil.IMAGE_PRIVATE_SUFFIX;
                }
                File sfile = new File(savePath);
                boolean isDown = FTPClientEntity.downFile(fName, sfile, ftpListener);
                if (FTPClientEntity.client.isErr()) {
                    msg.what = 9999;
                    data.putString("errMessage", FTPClientEntity.client.getErrMessage());
                    msg.setData(data);
                    handler.sendMessage(msg);
                } else if (isDown) {
                    msg.what = 2;
                    data.putString("infoMessage", "文件下载完成，保存位置：" + sfile.getPath());
                    data.putInt("position", index);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    public void downAllImages() {
        if (filesAdapter == null || filesAdapter.mList == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                FtpLock.unLock = false;
                FTPClientEntity.downAllImages(appCompatActivity, filesAdapter, handler, localSaveDir);
                //FTPClientEntity.loadAllFtpDirectory(filesAdapter, handler, ftpPath);
            }
        }).start();
    }

    Handler fileHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            int type = data.getInt("type");
            if (msg.what == 0) {
                int maxSize = data.getInt("maxSize");
                UiUtil.showFileProgressDialog(hpd, UiUtil.getProgressText(type, msg.what), maxSize);
            } else if (msg.what == 1) {
                int completedSize = data.getInt("completedSize");
                UiUtil.setSeedProgressDialog(hpd, UiUtil.getProgressText(type, msg.what), completedSize);
            } else if (msg.what == 2) {
                int maxSize = data.getInt("maxSize");
                UiUtil.setSeedProgressDialog(hpd, UiUtil.getProgressText(type, msg.what), maxSize);
            } else if (msg.what == 3) {
                UiUtil.setSeedProgressDialog(hpd, UiUtil.getProgressText(type, msg.what));
            } else if (msg.what == 4) {
                UiUtil.setSeedProgressDialog(hpd, UiUtil.getProgressText(type, msg.what));
            }
            super.handleMessage(msg);
        }
    };

    private void showList(List<FTPFileExt> arrList) {
        filesAdapter = new FilesAdapter(appCompatActivity, mLayoutManager, vectorDrawableCompat, ftpConfig, ftpPath, localSaveDir, arrList);
        filesAdapter.setOnItemClickListener(new FilesAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(final View v, final int position, final FTPFileExt ffext) {
                String fName = ffext.getName();
                if (ffext.getType() == 1) {
                    FtpLock.setUnLock();
                    pathList.add(ftpPath + fName + "/");
                    loadFTPFileList();
                } else {
                    if (AppUtil.isExt(fName, ConstantUtil.IMAGE_ALL_SUFFIX_GATHER)) {
                        String imPath = ImgUtil.getLocalImagePath(ffext.getLocalPath());
                        if (AppUtil.isNotEmpty(imPath)) {
                            File f = new File(imPath);
                            Intent intent = new Intent(appCompatActivity, ImageActivity.class);
                            intent.putExtra("fname", f.getName());
                            intent.putExtra("fdir", localSaveDir);
                            intent.putExtra("fpath", imPath);
                            startActivity(intent);
                        }
                    } else {
                        if (ffext.isDown()) {
                            FileProvider.openFileOfApp(appCompatActivity, new File(ffext.getLocalPath()));
                        } else {
                            UiUtil.showPosition(appCompatActivity, ffext.getName());
                        }
                    }
                }
            }

            @Override
            public void onItemLongClick(final View v, final int position, final FTPFileExt ffext) {
                if (ffext.getType() > 0) {
                    return;
                }
                PopupMenu popupMenu = new PopupMenu(appCompatActivity, v);
                popupMenu.getMenuInflater().inflate(R.menu.long_menu_ftp, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.Menu_Long_DownloadLocal) {
                            downFTP(ffext, position);
                        } else if (item.getItemId() == R.id.Menu_Long_DeleteFtp) {
                            new XPopup.Builder(appCompatActivity).asConfirm("确定删除", "删除后不能恢复，确定删除？",
                                    new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            deleteFTP(ffext, position);
                                        }
                                    }).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }

        });
        mRecyclerView.setAdapter(filesAdapter);
        downAllImages();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (pd != null) {
                pd.cancel();
            }
            if (hpd != null) {
                hpd.cancel();
            }
            Bundle data = msg.getData();
            if (msg.what == 1) {
                FtpDirList fflist = (FtpDirList) data.getSerializable("FTPFiles");
                showList(fflist.getList());
            } else if (msg.what == 2) {
                String infoMessage = data.getString("infoMessage");
                int position = data.getInt("position");
                filesAdapter.notifyItemChanged(position, new Object());
                UiUtil.showPosition(appCompatActivity, infoMessage);
            } else if (msg.what == 3) {
                int position = data.getInt("position");
                filesAdapter.notifyItemChanged(position, new Object());
            } else if (msg.what == 4) {
                String infoMessage = data.getString("infoMessage");
                int position = data.getInt("position");
                filesAdapter.mList.remove(position);
                filesAdapter.notifyItemRemoved(position);
                filesAdapter.notifyItemRangeChanged(position, filesAdapter.mList.size());
                UiUtil.showToast(appCompatActivity, infoMessage);
            } else if (msg.what == 5) {
                String infoMessage = data.getString("infoMessage");
                UiUtil.showPosition(appCompatActivity, infoMessage);
            } else if (msg.what == 9999) {
                String errMessage = data.getString("errMessage");
                UiUtil.showPosition(appCompatActivity, errMessage);
            }
            UiUtil.wakeLockRelease(m_wklk);
            super.handleMessage(msg);
        }
    };

    public void deleteFTP(final FTPFileExt ffext, final int position) {
        if (ffext.getType() > 0) {
            return;
        }
        UiUtil.wakeLockAcquire(m_wklk);
        UiUtil.showProgressDialog(pd, "删除中", "删除中,请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                UiUtil.sleep(100);
                Message msg = new Message();
                Bundle data = new Bundle();
                boolean isDel = FTPClientEntity.deleteFile(ftpPath, ffext.getName());
                if (FTPClientEntity.client.isErr()) {
                    msg.what = 9999;
                    data.putString("errMessage", FTPClientEntity.client.getErrMessage());
                    msg.setData(data);
                    handler.sendMessage(msg);
                } else if (isDel) {
                    if (ffext.isDown()) {
                        File f = new File(ffext.getLocalPath());
                        FileProvider.deleteFile(f);
                    }
                    msg.what = 4;
                    data.putString("infoMessage", "FTP文件删除操作成功！");
                    data.putInt("position", position);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener
            = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.Menu_Ftp_Refresh) {
                FtpLock.setUnLock();
                setFTPFileExtList(pathList.size() - 1, "");
                loadFTPFileList();
                return true;
            } else if (id == R.id.Menu_Ftp_ConnectRefresh) {
                FtpLock.setUnLock();
                connectRefreshFTP(FTPClientEntity.client.getFtpConfig());
                return true;
            } else if (id == R.id.Menu_See_Ftp_Info) {
                if (filesAdapter != null && filesAdapter.mList != null) {
                    FtpFolderEntity fe = new FtpFolderEntity();
                    fe.setFileType(1);
                    for (int i = 0; i < filesAdapter.mList.size(); i++) {
                        FTPFileExt ffile = filesAdapter.mList.get(i);
                        if (ffile.getType() == 1) {
                            fe.setFoldersSize(fe.getFoldersSize() + 1);
                        } else {
                            fe.setFilesSize(fe.getFilesSize() + 1);
                        }
                        if (ffile.isDown()) {
                            fe.setDownCountSzie(fe.getDownCountSzie() + 1);
                        }
                    }
                    String filesSize = "";
                    String containInfo = fe.getFilesSize() + "个文件";
                    if (fe.getFileType() == 1) {
                        containInfo = fe.getFoldersSize() + "个文件夹，" + fe.getFilesSize() + "个文件,已下载" + fe.getDownCountSzie() + "个文件";
                    }
                    new XPopup.Builder(appCompatActivity).asCustom(new CustomPopup(appCompatActivity, filesSize, containInfo, ftpPath)).show();
                }
                return true;
            } else if (id == R.id.Menu_upload_Ftp) {
                pickFile();
                return true;
            } else if (id == R.id.Menu_Ftp_Home) {
                FtpLock.setUnLock();
                Ftp_Fragments.showFtpFragment();
                return true;
            } else if (id == R.id.Menu_Ftp_Return) {
                FtpLock.setUnLock();
                if (pathList.size() > 1) {
                    backList();
                    return true;
                } else if (pathList.size() == 1) {
                    FtpLock.setUnLock();
                    Ftp_Fragments.showFtpFragment();
                    return true;
                }
                return true;
            }
            return true;
        }
    };

    public void pickFile() {
        DefaultSelectorActivity.startActivityForResult(appCompatActivity, false, true, 99999999);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == DefaultSelectorActivity.FILE_SELECT_REQUEST_CODE) {
            ArrayList<String> list = DefaultSelectorActivity.getDataFromIntent(data);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String selPath = list.get(i);
                    uploadFTP(selPath);
                }
            }
        }
    }

    public void uploadFTP(final String filePath) {
        final File file = new File(filePath);
        ftpListener = new FTPTransferListener(appCompatActivity, fileHandler, 1, file.length(), 0);
        UiUtil.wakeLockAcquire(m_wklk);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                boolean isDown = FTPClientEntity.upLoadFile(filePath, ftpPath, ftpListener);
                if (FTPClientEntity.client.isErr()) {
                    msg.what = 9999;
                    data.putString("errMessage", FTPClientEntity.client.getErrMessage());
                    msg.setData(data);
                    handler.sendMessage(msg);
                } else if (isDown) {
                    msg.what = 4;
                    data.putString("infoMessage", "文件上传完成，保存位置：" + ftpPath);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void backList() {
        int delIndex = pathList.size() - 1;
        if (delIndex <= 0) {
            pathList.clear();
        } else {
            pathList.remove(delIndex);
        }
        loadFTPFileList();
    }

    @Override
    public boolean onBackPressed() {
        if (pathList.size() > 1) {
            FtpLock.setUnLock();
            backList();
            return true;
        } else if (pathList.size() == 1) {
            FtpLock.setUnLock();
            Ftp_Fragments.showFtpFragment();
            return true;
        }
        return BackHandlerHelper.handleBackPress(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UiUtil.wakeLockRelease(m_wklk);
    }

    @Override
    public void onPause() {
        super.onPause();
        UiUtil.wakeLockRelease(m_wklk);
    }

}
