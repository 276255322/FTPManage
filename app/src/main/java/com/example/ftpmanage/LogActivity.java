package com.example.ftpmanage;

import android.os.Bundle;

import com.tao.admin.loglib.FileUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

public class LogActivity extends AppCompatActivity {

    private TextView logText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        logText =  findViewById(R.id.log_text_view);
        String log = FileUtils.readLogText();
        logText.setText(log);
    }

}
