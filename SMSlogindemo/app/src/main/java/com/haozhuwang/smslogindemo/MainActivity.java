package com.haozhuwang.smslogindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton;
    private Button mButton2;
    private Button mShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.main_btn);
        mButton2 = (Button) findViewById(R.id.search_home);
        mShare = (Button) findViewById(R.id.share_weixin);
        mShare.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mButton2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_btn:
                login();
                break;
            case R.id.search_home:
                search();
                break;
            case R.id.share_weixin:
                share();
                break;
            default:
                break;
        }

    }

    private void share() {
        Intent intent=new Intent(this,ShareActivity.class);
        startActivity(intent);
    }

    private void search() {
        Intent intent=new Intent(this,SearchHomeActivity.class);
        startActivity(intent);
    }

    private void login() {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
