package edu.nedu.nedu_library;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import edu.nedu.nedu_library.entity.UserInfo;
import edu.nedu.nedu_library.net.LoginHttpUtil;
import edu.nedu.nedu_library.net.PingHttpUtil;
import edu.nedu.nedu_library.util.UserInfoUtil;

public class WelcomeActivity extends AppCompatActivity {

    private Context mContext;
    private UserInfo userInfo;
    private boolean flag;
    private boolean ping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContext = this;
        flag = false;
        ping = false;
        //先ping一哈
        PingHttpUtil.ping(pingHandler);
        Timer pingTime = new Timer();
        TimerTask pingTask = new TimerTask() {
            @Override
            public void run() {
                if (UserInfoUtil.isLogin(mContext)) {
                    if (ping) {//ping通则检查网络
                        userInfo = UserInfoUtil.getUserInfo(mContext);
                        LoginHttpUtil.requestNetForPostLogin(handler, userInfo, 1);
                    } else {//如果ping不通则直接进入程序
                        flag = true;
                    }
                }
            }
        };
        pingTime.schedule(pingTask, 1000);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (flag) {
                    Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mainIntent.putExtra("ping", ping);
                    startActivity(mainIntent);
                } else {
                    Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }
                //finish();
            }
        };
        timer.schedule(task, 2000);
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
                //Toast.makeText(mContext,res,Toast.LENGTH_SHORT).show();
                if (res.equals("login success")) {
                    JSONObject userjson = resjson.getJSONObject("userinfo");
                    //UserInfoUtil.deleteUserInfo(mContext);
                    UserInfoUtil.saveUserInfo(mContext, userjson);
                    flag = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Handler pingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.equals("ping")){
                ping = true;
            }
        }
    };
}
