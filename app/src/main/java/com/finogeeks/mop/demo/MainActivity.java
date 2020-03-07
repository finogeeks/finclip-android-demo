package com.finogeeks.mop.demo;

import android.os.Bundle;

import com.finogeeks.lib.applet.client.FinAppClient;
import com.finogeeks.lib.applet.client.FinAppConfig;
import com.finogeeks.lib.applet.interfaces.FinCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FinAppClient.INSTANCE.isFinAppProcess(this)) {
            return;
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FinAppConfig config = new FinAppConfig.Builder()
                .setAppKey("22LyZEib0gLTQdU3MUauAWQrT5jdkwBnjD/4Rw9r+4kA")
                .setAppSecret("1a90cee1773badee")
                .setApiUrl("https://mp.finogeeks.com")
                .setApiPrefix("/api/v1/mop/")
                .setGlideWithJWT(false)
                .build();
// SDK初始化结果回调，用于接收SDK初始化状态
        FinCallback<Object> callback = new FinCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                // SDK初始化成功
            }

            @Override
            public void onError(int code, String error) {
                // SDK初始化失败
                Toast.makeText(MainActivity.this, "SDK初始化失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int status, String error) {

            }
        };
        FinAppClient.INSTANCE.init(this.getApplication(), config, callback);
        Button canvasButton = findViewById(R.id.btnCanvas);
        Button demoButton = findViewById(R.id.btnDemo);
        Button profileButton = findViewById(R.id.btnProfile);
        canvasButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5e3c147a188211000141e9b1",
                        new HashMap(){
                            {
                                put("path","/pages/index/index");
                            }
                        });
            }
        });

        demoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5e4d123647edd60001055df1");
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5e637a18cbfae4000170fa7a");
            }
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FinAppClient.INSTANCE.getAppletApiManager().startApplet(MainActivity.this, "5e3c147a188211000141e9b1");
//
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
