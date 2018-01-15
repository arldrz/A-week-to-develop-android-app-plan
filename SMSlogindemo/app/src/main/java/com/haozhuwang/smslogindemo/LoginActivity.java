package com.haozhuwang.smslogindemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.haozhuwang.smslogindemo.utils.IEditTextChangeListener;
import com.haozhuwang.smslogindemo.utils.VerifyCodeManager;
import com.haozhuwang.smslogindemo.utils.WorksSizeCheckUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.haozhuwang.smslogindemo.R.id.getyanzhengma1;
import static com.haozhuwang.smslogindemo.R.id.mobile_login;
import static com.haozhuwang.smslogindemo.R.id.yanzhengma;


/**
 * Created by Administrator on 2018/1/9
 */

public class LoginActivity extends AppCompatActivity implements  View.OnTouchListener {
    @BindView(R.id.cancel)
    ImageView mCancel;
    @BindView(mobile_login)
    EditText mMobileLogin;
    @BindView(yanzhengma)
    EditText mYanzhengma;
    @BindView(getyanzhengma1)
    Button mGetyanzhengma1;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    //倒计时秒数
    private int countSeconds = 60;
    private int recLen = 60;
    private Timer timer = new Timer();

    private Context mContext;
    private String usersuccess;
    private String userinfomsg;
    private String mToken;
    private String mUserId;
    private VerifyCodeManager codeManager;

    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initData();

    }

    private void initView() {

        mLoginBtn.setEnabled(false);
        mLoginBtn.setOnTouchListener(this);

        //1.创建工具类对象 把要改变颜色的tv先传过去
        WorksSizeCheckUtil.textChangeListener textChangeListener = new WorksSizeCheckUtil.textChangeListener(mLoginBtn);

        //2.把所有要监听的edittext都添加进去
        textChangeListener.addAllEditText(mMobileLogin, mYanzhengma);

        //3.接口回调 在这里拿到boolean变量 根据isHasContent的值决定 tv 应该设置什么颜色
        WorksSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if (isHasContent) {
                    mLoginBtn.setBackgroundResource(R.drawable.button_normal);
                } else {
                    mLoginBtn.setEnabled(false);
                    mLoginBtn.setBackgroundResource(R.drawable.button_zhihui);
                }
            }
        });

    }

    private void initData() {
    }



    /**获取信息进行登录*/
    public void login() {
        String mobile = mMobileLogin.getText().toString().trim();
        String verifyCode = mYanzhengma.getText().toString().trim();
        RequestParams params = new RequestParams("请求接口");
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.e("tag", "登陆的result=" + jsonObject);
                    String success = jsonObject.optString("success");
                    String data = jsonObject.optString("data");
                    String msg = jsonObject.optString("msg");
                    if ("true".equals(success)) {
                        Log.e("tag", "登陆的data=" + data);
                        JSONObject json = new JSONObject(data);
                        mToken = json.optString("token");
                        mUserId = json.optString("userId");
                        //我这里按照我的要求写的，你们也可以适当改动
                        //获取用户信息的状态
                        //getUserInfo();
                    } else {
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**获取验证码信息，判断是否有手机号码*/
    private void getMobiile(String mobile) {
        if ("".equals(mobile)) {
            Log.e("tag", "mobile=" + mobile);
            new AlertDialog.Builder(mContext).setTitle("提示").setMessage("手机号码不能为空").setCancelable(true).show();
        } else if (isMobileNO(mobile) == false) {
            new AlertDialog.Builder(mContext).setTitle("提示").setMessage("请输入正确的手机号码").setCancelable(true).show();
        } else {
            Log.e("tag", "输入了正确的手机号");
            requestVerifyCode(mobile);
        }
    }

    /**获取验证码信息，进行验证码请求*/
    private void requestVerifyCode(String mobile) {
        RequestParams requestParams = new RequestParams("这里是你请求的验证码接口，参数什么的加在后面");
        x.http().post(requestParams, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject2 = new JSONObject(result);
                    Log.e("tag", "jsonObject2" + jsonObject2);
                    String state = jsonObject2.getString("success");
                    String verifyCode = jsonObject2.getString("msg");
                    Log.e("tag", "获取验证码==" + verifyCode);
                    if ("true".equals(state)) {
                        Toast.makeText(LoginActivity.this, verifyCode, Toast.LENGTH_SHORT).show();
                        //startCountBack();//这里是用来进行请求参数的
                    } else {
                        Toast.makeText(LoginActivity.this, verifyCode, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**使用正则表达式判断电话号码*/
    public static boolean isMobileNO(String tel) {
        Pattern p = Pattern.compile("^(13[0-9]|15([0-3]|[5-9])|14[5,7,9]|17[1,3,5,6,7,8]|18[0-9])\\d{8}$");
        Matcher m = p.matcher(tel);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    /**获取验证码信息,进行计时操作*/
    private void startCountBack() {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGetyanzhengma1.setText(countSeconds + "");
                mCountHandler.sendEmptyMessage(0);
            }
        });
    }

    private Handler mCountHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (countSeconds > 0) {
                --countSeconds;
                mCountHandler.sendEmptyMessageDelayed(0,1000);
                mGetyanzhengma1.setText("(" + countSeconds + ")后获取验证码");

            } else {
                countSeconds = 60;
                mGetyanzhengma1.setText("请重新获取验证码");
            }
        }
    };


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            //更改为按下时的背景图片
            view.setBackgroundResource(R.drawable.button_press2);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            //改为抬起时的图片
            view.setBackgroundResource(R.drawable.button_normal);
        }
        return false;
    }


    @OnClick({R.id.cancel, R.id.getyanzhengma1, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.getyanzhengma1:
                if (countSeconds == 60) {
                    String mobile = mMobileLogin.getText().toString().trim();
                    Log.d(TAG, "mobile==" + mobile);
                    getMobiile(mobile);
                  // codeManager.getVerifyCode(VerifyCodeManager.RESET_PWD);

                } else {
                     //获取手机号
                    Toast.makeText(LoginActivity.this, "不能重复发送验证码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_btn:
                login();
                break;
            default:
                break;
        }
    }
}
