package com.example.ftpmanage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;


import com.example.ftpmanage.utils.AppUtil;
import com.example.ftpmanage.utils.ConstantUtil;
import com.example.ftpmanage.utils.UiUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImageActivity extends AppCompatActivity {

    private ProgressDialog pd = null;

    private PhotoViewPager viewPager;
    private ImagesAdapter imagesAdapter;
    private TextView points;
    Intent intent = null;

    private AlphaAnimation mShowAnim, mHiddenAmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        intent = this.getIntent();
        pd = new ProgressDialog(ImageActivity.this);
        viewPager = findViewById(R.id.viewpager);
        mShowAnim = new AlphaAnimation(0.0f, 1.0f);
        mShowAnim.setDuration(300);
        mHiddenAmin = new AlphaAnimation(1.0f, 0.0f);
        mHiddenAmin.setDuration(300);
        loadLocalImages();
    }

    private void loadLocalImages() {
        UiUtil.showProgressDialog(pd, "载入中", "载入中,请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                String sfname = intent.getStringExtra("fname");
                String localDir = intent.getStringExtra("fdir");
                int sindex = 0;
                File fi = new File(localDir);
                File[] files = fi.listFiles();
                List<String> flist = new ArrayList<>();
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    if (!f.isDirectory()) {
                        String fName = f.getName();
                        if (AppUtil.isExt(fName, ConstantUtil.IMAGE_ALL_SUFFIX_GATHER)) {
                            String fPath = f.getPath();
                            flist.add(fPath);
                            if (sfname.equals(fName)) {
                                sindex = flist.size() - 1;
                            }
                        }
                    }
                }
                String[] urls = new String[flist.size()];
                flist.toArray(urls);
                data.putInt("sindex", sindex);
                data.putSerializable("urls", urls);
                msg.setData(data);
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pd.cancel();
            Bundle data = msg.getData();
            if (msg.what == 1) {
                final String[] urls = (String[]) data.getSerializable("urls");
                int sindex = data.getInt("sindex");
                imagesAdapter = new ImagesAdapter(ImageActivity.this, urls);
                viewPager.setAdapter(imagesAdapter);
                points = findViewById(R.id.points);
                points.setText((sindex +1) + "/" + urls.length);
                imagesAdapter.setOnItemClickListener(new ImagesAdapter.OnItemClickListener() {
                    @Override
                    public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                        points.setAnimation(mHiddenAmin);
                        points.setVisibility(View.INVISIBLE);
                    }
                });
                viewPager.setCurrentItem(sindex);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float v, int i1) {
                        points.setText((position +1) + "/" + urls.length);
                        points.setAnimation(mShowAnim);
                        points.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onPageSelected(int i) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {
                    }
                });
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
