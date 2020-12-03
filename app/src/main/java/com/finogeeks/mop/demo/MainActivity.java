package com.finogeeks.mop.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.finogeeks.lib.applet.client.FinAppClient;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnCharts = findViewById(R.id.btn_charts);
        btnCharts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5facb3a52dcbff00017469bd");
            }
        });

        Button btnDemo = findViewById(R.id.btn_demo);
        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5fa214a29a6a7900019b5cc1");
            }
        });

        Button btnProfile = findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5fa215459a6a7900019b5cc3");
            }
        });

        Button btnCustomApi = findViewById(R.id.btn_custom_api);
        btnCustomApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5fc8934aefb8c600019e9747");
            }
        });
    }
}