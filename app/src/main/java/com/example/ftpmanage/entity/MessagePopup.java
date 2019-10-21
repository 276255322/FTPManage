package com.example.ftpmanage.entity;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ftpmanage.R;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;

public class MessagePopup extends CenterPopupView {

    public String message = "";

    /**
     * 注意：自定义弹窗本质是一个自定义View，但是只需重写一个参数的构造，其他的不要重写，所有的自定义弹窗都是这样。
     *
     * @param context
     */
    public MessagePopup(@NonNull Context context, String message) {
        super(context);
        this.message = message;
    }

    /**
     * 返回自定义弹窗的布局
     *
     * @return
     */
    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_qq_msg;
    }

    /**
     * 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
     */
    @Override
    protected void onCreate() {
        super.onCreate();
        TextView mess = findViewById(R.id.popup_qq_msg_message);
        mess.setText(this.message);
    }

    /**
     * 设置最大宽度，看需要而定
     *
     * @return
     */
    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }

    /**
     * 设置最大高度，看需要而定
     *
     * @return
     */
    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }

    /**
     * 设置自定义动画器，看需要而定
     *
     * @return
     */
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
