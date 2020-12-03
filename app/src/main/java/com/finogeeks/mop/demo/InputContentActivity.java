package com.finogeeks.mop.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InputContentActivity extends AppCompatActivity {

    public static final String EXTRA_NAME_INPUT_CONTENT = "input_content";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText editTextInputContent = findViewById(R.id.edt_input_content);
        Button btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextInputContent.length() < 1) {
                    Toast.makeText(InputContentActivity.this, getString(R.string.fin_clip_input_content_hint), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(EXTRA_NAME_INPUT_CONTENT, editTextInputContent.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
