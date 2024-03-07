package com.finogeeks.mop.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.finogeeks.lib.applet.client.FinAppClient;
import com.finogeeks.lib.applet.sdk.api.request.IFinAppletRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnScan = findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScanStartAppletActivity.class));
            }
        });


        Button btnCharts = findViewById(R.id.btn_charts);
        btnCharts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, IFinAppletRequest.Companion.fromAppId("5facb3a52dcbff00017469bd"),null);
            }
        });

        Button btnDemo = findViewById(R.id.btn_demo);
        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this,IFinAppletRequest.Companion.fromAppId( "5fa214a29a6a7900019b5cc1"),null);
            }
        });

        Button btnProfile = findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, IFinAppletRequest.Companion.fromAppId("5fa215459a6a7900019b5cc3"),null);
            }
        });

        Button btnCustomApi = findViewById(R.id.btn_custom_api);
        btnCustomApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("path", "pages/index/index");
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, IFinAppletRequest.Companion.fromAppId("5fc8934aefb8c600019e9747").setStartParams(params),null);
            }
        });

        Button btnH5Api = findViewById(R.id.btn_h5_api);
        btnH5Api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("path", "pages/webview/webview");
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5fc8934aefb8c600019e9747", params,null);
            }
        });

        Button btnAppletLogin = findViewById(R.id.btn_applet_login);
        btnAppletLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "60f051ea525ea10001c0bd22",null);
            }
        });

        Button btnComponent = findViewById(R.id.btn_component);
        btnComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ComponentActivity.class));
            }
        });
    }
}