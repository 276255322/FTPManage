package com.example.ftpmanage.entity;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.example.ftpmanage.R;
import com.lxj.xpopup.core.PositionPopupView;

public class QQMsgPopup extends PositionPopupView {

    public QQMsgPopup(@NonNull Context context, String message) {
        super(context);
        TextView mess = findViewById(R.id.popup_qq_msg_message);
        mess.setText(message);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_qq_msg;
    }
}
