package com.example.ftpmanage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;


public class ImagesAdapter extends PagerAdapter {

    private String[] imgList;
    private Context context;

    public ImagesAdapter(Context context,String[] imgList){
        this.imgList = imgList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return imgList.length;
    }

    //指定复用的判断逻辑，固定写法：view == object
    @Override
    public boolean isViewFromObject(View view, Object object) {
        //当创建新的条目，又反回来，判断view是否可以被复用(即是否存在)
        return view == object;
    }

    //返回要显示的条目内容
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(context);
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide.with(context).load(imgList[position]).placeholder(R.drawable.sys_placeholder).into(photoView);
        //把图片添加到container中
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(photoView);
        mAttacher.setOnScaleChangeListener(new OnScaleChangedListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onScaleChange(scaleFactor, focusX, focusY);
                }
            }
        });
        container.addView(photoView);
        //把图片返回给框架，用来缓存
        return photoView;
    }

    //销毁条目
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //object:刚才创建的对象，即要销毁的对象
        container.removeView((View) object);
    }

    public interface OnItemClickListener {
        void onScaleChange(float scaleFactor, float focusX, float focusY);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
