package com.example.ftpmanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;

import com.example.ftpmanage.entity.BackHandlerHelper;
import com.example.ftpmanage.entity.BottomNavigationViewEx;
import com.example.ftpmanage.ui.FtpFragment;
import com.example.ftpmanage.ui.Ftp_Fragments;
import com.example.ftpmanage.ui.LocalFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ActivityPermissions ap;

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationViewEx bnve;
    private FloatingActionButton fb;

    private SparseIntArray items;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ap = new ActivityPermissions(this);
        ap.requestPermissions();
        initView();
        initData();
    }

    private void initView() {
        viewPager = findViewById(R.id.vPager);
        fb = findViewById(R.id.fab);
        VectorDrawableCompat svg_add = VectorDrawableCompat.create(getResources(), R.drawable.svg_add_24dp, getTheme());
        svg_add.setTint(getResources().getColor(R.color.colorPrimary));
        fb.setImageDrawable(svg_add);
        bnve = findViewById(R.id.nBar);
        bnve.enableItemShiftingMode(true);
        bnve.enableAnimation(false);
        bnve.setItemIconTintList(null);
        bnve.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        viewPager.addOnPageChangeListener(viewPagerOnPageChangeListener);
        updateItemIcon(0);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FtpEditActivity.class);
                startActivity(intent);
            }
        });
    }

    private ViewPager.OnPageChangeListener viewPagerOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            bnve.setCurrentItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private BottomNavigationViewEx.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationViewEx.OnNavigationItemSelectedListener() {
        private int previousPosition = -1;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int position = 0;
            switch (item.getItemId()) {
                case R.id.Menu_bottom_ftp:
                    position = 0;
                    break;
                case R.id.Menu_bottom_local:
                    position = 1;
                    break;
                case R.id.i_empty: {
                    return false;
                }
            }
            updateItemIcon(position);
            if (previousPosition != position) {
                previousPosition = position;
                viewPager.setCurrentItem(position);
            }
            return true;
        }
    };

    /**
     * create fragments
     */
    private void initData() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragments = new ArrayList<>(2);
        items = new SparseIntArray(2);
        Ftp_Fragments ftp_Fragments = new Ftp_Fragments();
        LocalFragment localFragment = new LocalFragment();
        fragments.add(ftp_Fragments);
        fragments.add(localFragment);
        items.put(R.id.Menu_bottom_ftp, 0);
        items.put(R.id.Menu_bottom_local, 1);
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager, fragments);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }

    public void updateItemIcon(int selIndex) {
        if (selIndex == 0) {
            VectorDrawableCompat svg_ftp_server = VectorDrawableCompat.create(getResources(), R.drawable.svg_ftp_server_24dp, getTheme());
            VectorDrawableCompat svg_folder = VectorDrawableCompat.create(getResources(), R.drawable.svg_folder_24dp_c, getTheme());
            MenuItem item1 = bnve.getMenu().findItem(R.id.Menu_bottom_ftp);
            item1.setIcon(svg_ftp_server);
            MenuItem item2 = bnve.getMenu().findItem(R.id.Menu_bottom_local);
            item2.setIcon(svg_folder);
        } else if(selIndex == 1) {
            VectorDrawableCompat svg_ftp_server = VectorDrawableCompat.create(getResources(), R.drawable.svg_ftp_server_24dp_c, getTheme());
            VectorDrawableCompat svg_folder = VectorDrawableCompat.create(getResources(), R.drawable.svg_folder_24dp, getTheme());
            MenuItem item1 = bnve.getMenu().findItem(R.id.Menu_bottom_ftp);
            item1.setIcon(svg_ftp_server);
            MenuItem item2 = bnve.getMenu().findItem(R.id.Menu_bottom_local);
            item2.setIcon(svg_folder);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
    }
}
