package edu.nedu.nedu_library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.nedu.nedu_library.entity.UserInfo;
import edu.nedu.nedu_library.net.LoginHttpUtil;
import edu.nedu.nedu_library.util.UserInfoUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //注册使用的文本框
    private EditText etUserid;
    private EditText etPassword;
    private EditText etConfirmPassword;
    //隐藏验证密码框使用
    private TextInputLayout confirmPasswordWrapper;
    //注册 忘记密码按钮
    private TextView tvRegister;
    private TextView tvFPassword;
    //登陆 注册 返回按钮
    private Button btnLogin;
    private Button btnRegister;
    private Button btnBack;
    //登陆 注册使用布局
    private LinearLayout lyTvRegister;
    private LinearLayout lyBtnRegister;

    private Context mContext;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        //找到控件
        etUserid = (EditText) findViewById(R.id.etUserid);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        confirmPasswordWrapper = (TextInputLayout) findViewById(R.id.confirmPasswordWrapper);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvFPassword = (TextView) findViewById(R.id.tvFPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnBack = (Button) findViewById(R.id.btnBack);
        lyTvRegister = (LinearLayout) findViewById(R.id.lyTvRegister);
        lyBtnRegister = (LinearLayout) findViewById(R.id.lyBtnRegister);
        //设置可见性 注册的密码验证登陆不可见
        confirmPasswordWrapper.setVisibility(View.GONE);
        lyBtnRegister.setVisibility(View.GONE);
        //设置事件 登陆 注册 忘记密码
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvFPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                login();
                break;
            case R.id.tvRegister:
                Toast.makeText(mContext,"register",Toast.LENGTH_SHORT).show();
                //更改控件可见性
                confirmPasswordWrapper.setVisibility(View.VISIBLE);
                lyBtnRegister.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.GONE);
                lyTvRegister.setVisibility(View.GONE);
                break;
            case R.id.tvFPassword:
                Toast.makeText(mContext,"forget_password",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnRegister:
                register();
                break;
            case R.id.btnBack:
                //更改控件可见性
                confirmPasswordWrapper.setVisibility(View.GONE);
                lyBtnRegister.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                lyTvRegister.setVisibility(View.VISIBLE);
                //清空信息
                etUserid.setText("");
                etPassword.setText("");
                etConfirmPassword.setText("");
                break;
        }

    }

    public void login(){
        String userid = etUserid.getText().toString();
        String password = etPassword.getText().toString();
        if (!userid.equals("") && !password.equals("")) {
            userInfo = new UserInfo();
            userInfo.setUserid(userid);
            userInfo.setPassword(password);
            LoginHttpUtil.requestNetForPostLogin(handler, userInfo, 1);
        } else {
            Toast.makeText(mContext, "no userid or password", Toast.LENGTH_SHORT).show();
        }

    }

    public void register(){
        String userid = etUserid.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        if (userid.equals("")) {
            Toast.makeText(mContext, "userid no filled", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(mContext, "password no filled", Toast.LENGTH_SHORT).show();
        } else if (confirmPassword.equals("")) {
            Toast.makeText(mContext, "confirm password no filled", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(mContext, "Two passwords are inconsistent", Toast.LENGTH_SHORT).show();
        } else {
            userInfo = new UserInfo();
            userInfo.setUserid(userid);
            userInfo.setPassword(password);
            LoginHttpUtil.requestNetForPostLogin(handler, userInfo, 2);
        }
    }

    //创建一个handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            try {
                JSONObject resjson = new JSONObject(result);
                String res = resjson.getString("res");
                Toast.makeText(mContext,res,Toast.LENGTH_SHORT).show();
                if (res.equals("login success")) {
                    JSONObject userjson = resjson.getJSONObject("userinfo");
                    UserInfoUtil.saveUserInfo(mContext, userjson);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else if (res.equals("register success")) {
                    LoginHttpUtil.requestNetForPostLogin(handler, userInfo, 1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
