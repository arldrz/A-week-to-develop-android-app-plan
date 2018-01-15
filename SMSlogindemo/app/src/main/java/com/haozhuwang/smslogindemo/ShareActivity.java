package com.haozhuwang.smslogindemo;

        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;

        import java.util.HashMap;

        import cn.sharesdk.framework.Platform;
        import cn.sharesdk.framework.PlatformActionListener;
        import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2018/1/11
 */

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton;
    private static final String TAG = "ShareActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mButton = (Button) findViewById(R.id.share);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        OnekeyShare oks=new OnekeyShare();
        oks.setImagePath("/sdcard/1.jpg");
        oks.setText("text");
        oks.setTitle("title");
        oks.show(ShareActivity.this);
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d(TAG,"onComplete");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d(TAG,"onError");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d(TAG,"onCancel");
            }
        });
    }
}
