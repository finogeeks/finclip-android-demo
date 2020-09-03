package com.finogeeks.mop.demo;

import android.os.Bundle;
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

        Button canvasButton = findViewById(R.id.btnCanvas);
        Button demoButton = findViewById(R.id.btnDemo);
        Button profileButton = findViewById(R.id.btnProfile);
        canvasButton.setOnClickListener(v -> {
            Map<String, String> startParams = new HashMap<>();
            startParams.put("path", "/pages/index/index");
            FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5ea03fa563cb900001d73863", startParams);
        });

        demoButton.setOnClickListener(v -> FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5ea0401463cb900001d73865"));

        profileButton.setOnClickListener(v -> FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5ea0412663cb900001d73867"));
    }
}