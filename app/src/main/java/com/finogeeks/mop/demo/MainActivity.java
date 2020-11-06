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

        Button demoButton = findViewById(R.id.btnDemo);
        demoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5fa214a29a6a7900019b5cc1");
            }
        });

        Button profileButton = findViewById(R.id.btnProfile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5fa215459a6a7900019b5cc3");
            }
        });
    }
}