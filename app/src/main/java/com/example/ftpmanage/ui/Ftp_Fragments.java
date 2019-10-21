package com.example.ftpmanage.ui;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ftpmanage.DBHelper;
import com.example.ftpmanage.R;

import java.util.ArrayList;
import java.util.List;


public class Ftp_Fragments extends Fragment {

    private static AppCompatActivity appCompatActivity;
    private static Fragment currentFragment = new Fragment();
    private static List<Fragment> fragments = new ArrayList<>();

    private FrameLayout fl;
    private View root;
    private FtpFragment ftpFragment;
    private FtpFilesFragment ftpFilesFragment;

    private SQLiteDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.ftp_fragment, container, false);
        appCompatActivity = (AppCompatActivity) getActivity();
        fl = root.findViewById(R.id.frame_layout);
        if (savedInstanceState != null) {
            fl.removeAllViews();
        }
        DBHelper dbHelper = new DBHelper(appCompatActivity);
        db = dbHelper.getWritableDatabase();
        initFragment();
        return root;
    }

    private void initFragment() {
        fragments.clear();
        ftpFragment = new FtpFragment(db);
        ftpFilesFragment = new FtpFilesFragment(db);
        fragments.add(ftpFragment);
        fragments.add(ftpFilesFragment);
        showFtpFragment();
    }

    public static void showFtpFragment() {
        boolean isLoad = false;
        FragmentTransaction transaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
        if (fragments.get(0).isAdded()) {
            transaction.hide(currentFragment).show(fragments.get(0));
            isLoad = true;
        } else {
            transaction.hide(currentFragment).add(R.id.frame_layout, fragments.get(0), "0");
        }
        currentFragment = fragments.get(0);
        FtpFragment fragment = ((FtpFragment) currentFragment);
        if (isLoad) {
            fragment.loadFtpList();
        }
        transaction.commit();
    }

    public static void showFtpFilesFragment(String ftpPath, int ftpConfigId) {
        boolean isLoad = false;
        FragmentTransaction transaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
        if (fragments.get(1).isAdded()) {
            transaction.hide(currentFragment).show(fragments.get(1));
            isLoad = true;
        } else {
            transaction.hide(currentFragment).add(R.id.frame_layout, fragments.get(1), "1");
        }
        currentFragment = fragments.get(1);
        FtpFilesFragment fragment = ((FtpFilesFragment) currentFragment);
        if (isLoad) {
            fragment.setFtpConfig(ftpPath, ftpConfigId);
            fragment.loadFTPFileList();
        } else {
            fragment.setFtpConfig(ftpPath, ftpConfigId);
        }
        transaction.commit();
    }
}
