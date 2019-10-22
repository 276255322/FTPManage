package com.example.ftpmanage.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ftpmanage.entity.FTPClientEntity;
import com.example.ftpmanage.FtpAdapter;
import com.example.ftpmanage.FtpEditActivity;
import com.example.ftpmanage.FtpUtils;
import com.example.ftpmanage.R;
import com.example.ftpmanage.entity.FtpConfig;
import com.example.ftpmanage.utils.UiUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.List;


public class FtpFragment extends Fragment {

    private View root;

    private AppCompatActivity appCompatActivity;

    private RecyclerView mRecyclerView;

    private LinearLayout linearLayoutAddFtp;

    private FtpAdapter ftpAdapter;

    private GridLayoutManager mLayoutManager;

    private ProgressDialog pd = null;

    private SQLiteDatabase db;

    List<FtpConfig> ftpList;

    public static boolean isFirst = true;

    public FtpFragment(SQLiteDatabase db) {
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
        root = inflater.inflate(R.layout.activity_main, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = root.findViewById(R.id.f_toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        initView();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            loadFtpList();
            isFirst = false;
        }
    }

    private void initView() {
        mRecyclerView = root.findViewById(R.id.f_recyclerView);
        linearLayoutAddFtp = root.findViewById(R.id.linear_add_ftp);
        linearLayoutAddFtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appCompatActivity, FtpEditActivity.class);
                startActivity(intent);
            }
        });
        mLayoutManager = new GridLayoutManager(appCompatActivity, 4);
        mRecyclerView.setLayoutManager(mLayoutManager);
        pd = new ProgressDialog(appCompatActivity);
    }

    public void loadFtpList() {
        UiUtil.showProgressDialog(pd, "载入中", "载入中,请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                ftpList = FtpUtils.getFTPConfigList(db);
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void showFtpList() {
        if (ftpList == null || ftpList.size() <= 0) {
            linearLayoutAddFtp.setVisibility(View.VISIBLE);
        } else {
            linearLayoutAddFtp.setVisibility(View.GONE);
        }
        ftpAdapter = new FtpAdapter(appCompatActivity, mLayoutManager, ftpList);
        ftpAdapter.setOnItemClickListener(new FtpAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View v, final int position, final FtpConfig fe) {
                FtpConfig fc = FtpUtils.getFTPConfigById(db, fe.getFid().toString());
                connFTP(fc);
            }

            @Override
            public void onItemLongClick(final View v, final int position, final FtpConfig fe) {
                PopupMenu popupMenu = new PopupMenu(appCompatActivity, v);
                popupMenu.getMenuInflater().inflate(R.menu.long_menu_main, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.Menu_deleteFtp) {
                            new XPopup.Builder(appCompatActivity).asConfirm("确定删除", "删除后不能恢复，确定删除？",
                                    new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            db.delete("FtpList", "fid=?", new String[]{fe.getFid().toString()});
                                            ftpList.remove(position);
                                            ftpAdapter.notifyItemRemoved(position);
                                            ftpAdapter.notifyItemRangeChanged(position, ftpList.size());
                                            UiUtil.showToast(appCompatActivity, "删除操作成功！");
                                        }
                                    }).show();
                        } else if (item.getItemId() == R.id.Menu_editFtp) {
                            Intent intent = new Intent(appCompatActivity, FtpEditActivity.class);
                            intent.putExtra("fid", fe.getFid().toString());
                            startActivity(intent);
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        mRecyclerView.setAdapter(ftpAdapter);
    }

    public void connFTP(final FtpConfig fc) {
        UiUtil.showProgressDialog(pd, "连接中", "连接中,请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                FTPClientEntity.createConnection(fc, appCompatActivity);
                if (FTPClientEntity.client.isErr()) {
                    msg.what = 9999;
                    data.putString("errMessage", FTPClientEntity.client.getErrMessage());
                    msg.setData(data);
                } else {
                    msg.what = 1;
                    data.putInt("ftpConfigId", fc.getFid());
                    msg.setData(data);
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (pd != null) {
                pd.cancel();
            }
            Bundle data = msg.getData();
            if (msg.what == 1) {
                if (FTPClientEntity.client.getConnStatus() == 1) {
                    int ftpConfigId = data.getInt("ftpConfigId");
                    Ftp_Fragments.showFtpFilesFragment("/", ftpConfigId);
                }
            } else if (msg.what == 2) {
                showFtpList();
            } else if (msg.what == 9999) {
                String errMessage = data.getString("errMessage");
                UiUtil.showPosition(appCompatActivity, errMessage);
            }
            super.handleMessage(msg);
        }
    };

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener
            = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.Menu_AddFtp) {
                Intent intent = new Intent(appCompatActivity, FtpEditActivity.class);
                startActivity(intent);
                return true;
            }
            return true;
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume，为true时，Fragment已经可见
        } else {
            //相当于Fragment的onPause，为false时，Fragment不可见
        }
    }
}
