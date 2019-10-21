package com.example.ftpmanage;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ftpmanage.utils.AppUtil;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;

public class CustomPopup extends CenterPopupView {

    String folder_size = "";
    String folder_contain = "";
    String folder_path = "";

    //注意：自定义弹窗本质是一个自定义View，但是只需重写一个参数的构造，其他的不要重写，所有的自定义弹窗都是这样。
    public CustomPopup(@NonNull Context context, String size, String contain, String path) {
        super(context);
        folder_size = size;
        folder_contain = contain;
        folder_path = path;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.content_local_popup;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        LinearLayout popup_info_folder_size_line = findViewById(R.id.popup_info_folder_size_line);
        if(AppUtil.isEmpty(folder_size)){
            popup_info_folder_size_line.setVisibility(View.GONE);
        }
        TextView popup_info_folder_size = findViewById(R.id.popup_info_folder_size);
        popup_info_folder_size.setText(folder_size);

        LinearLayout popup_info_folder_contain_line = findViewById(R.id.popup_info_folder_contain_line);
        if(AppUtil.isEmpty(folder_contain)){
            popup_info_folder_contain_line.setVisibility(View.GONE);
        }
        TextView popup_info_folder_contain = findViewById(R.id.popup_info_folder_contain);
        popup_info_folder_contain.setText(folder_contain);

        LinearLayout popup_info_folder_path_line = findViewById(R.id.popup_info_folder_path_line);
        if(AppUtil.isEmpty(folder_path)){
            popup_info_folder_path_line.setVisibility(View.GONE);
        }
        TextView popup_info_folder_path = findViewById(R.id.popup_info_folder_path);
        popup_info_folder_path.setText(folder_path);
    }

    // 设置最大宽度，看需要而定
    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }

    // 设置最大高度，看需要而定
    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }

    // 设置自定义动画器，看需要而定
    @Override
    protected PopupAnimator getPopupAnimator() {
        return super.getPopupAnimator();
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     *
     * @return
     */
    protected int getPopupWidth() {
        return 0;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    protected int getPopupHeight() {
        return 0;
    }

}
