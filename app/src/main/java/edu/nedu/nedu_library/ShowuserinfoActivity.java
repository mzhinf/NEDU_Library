package edu.nedu.nedu_library;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.nedu.nedu_library.entity.UserInfo;
import edu.nedu.nedu_library.net.LoginHttpUtil;
import edu.nedu.nedu_library.util.InfoUtil;
import edu.nedu.nedu_library.util.UserInfoUtil;

public class ShowuserinfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private TextView tvName;
    private TextView tvSex;
    private TextView tvPhone;
    private TextView tvEmail;
    private TextView tvDepartment;
    private EditText edName;
    private EditText edSex;
    private EditText edPhone;
    private EditText edEmail;
    private EditText edDepartment;
    private Button btnEdit;
    private Button btnSubmit;
    private Button btnCancel;

    private ActionBar actionBar;

    private UserInfo userInfo;

    /**
     * gone 表示不可见并且不占用空间
     * visible 表示可见
     * invisible 表示不可见但是占用空间
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showuserinfo);
        mContext = this;
        setTitle("个人信息");
        userInfo = UserInfoUtil.getUserInfo(mContext);

        //加载控件
        tvName = (TextView) findViewById(R.id.tvName);
        tvSex = (TextView) findViewById(R.id.tvSex);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvDepartment = (TextView) findViewById(R.id.tvDepartment);
        edName = (EditText) findViewById(R.id.edName);
        edSex = (EditText) findViewById(R.id.edSex);
        edPhone = (EditText) findViewById(R.id.edPhone);
        edEmail = (EditText) findViewById(R.id.edEmail);
        edDepartment = (EditText) findViewById(R.id.edDepartment);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        actionBar = getSupportActionBar();

        btnEdit.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        updateUI();
        this.setVisibility(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEdit:
                this.setVisibility(3);
                this.setVisibility(2);
                break;
            case R.id.btnSubmit:
                updateUserInfo();
                break;
            case R.id.btnCancel:
                this.setVisibility(1);
                this.setVisibility(4);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateUserInfo(){
        userInfo.setName(edName.getText().toString());
        //性别特殊处理
        if (!edSex.getText().toString().equals("")) {
            if (edSex.getText().toString().equals("男")) {
                userInfo.setSex('m');
            } else if (edSex.getText().toString().equals("女")) {
                userInfo.setSex('w');
            } else {
                Toast.makeText(mContext, "性别不符合规范(男&女)", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            userInfo.setSex((char)0);
        }
        //email特殊处理
        if (!edEmail.getText().toString().equals("")) {
            if (edEmail.getText().toString().contains("@")) {
                userInfo.setEmail(edEmail.getText().toString());
            } else {
                Toast.makeText(mContext, "email不符合规范", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            userInfo.setEmail("");
        }
        if (!edPhone.getText().toString().equals("")) {
            if (edPhone.getText().toString().length() == 11) {
                userInfo.setPhone(edPhone.getText().toString());
            } else {
                Toast.makeText(mContext, "phone长度不符合规范", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            userInfo.setPhone("");
        }
        userInfo.setDepartment(edDepartment.getText().toString());
        LoginHttpUtil.requestNetForPostLogin(handler, userInfo, 3);
    }

    public void setVisibility(int type){//设置可见性
        switch (type) {
            case 1:
                edName.setVisibility(View.GONE);
                edSex.setVisibility(View.GONE);
                edPhone.setVisibility(View.GONE);
                edEmail.setVisibility(View.GONE);
                edDepartment.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                break;
            case 2:
                edName.setVisibility(View.VISIBLE);
                edSex.setVisibility(View.VISIBLE);
                edPhone.setVisibility(View.VISIBLE);
                edEmail.setVisibility(View.VISIBLE);
                edDepartment.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);

                edName.setText(tvName.getText().toString());
                edSex.setText(tvSex.getText().toString());
                edPhone.setText(tvPhone.getText().toString());
                edEmail.setText(tvEmail.getText().toString());
                edDepartment.setText(tvDepartment.getText().toString());
                break;
            case 3:
                tvName.setVisibility(View.GONE);
                tvSex.setVisibility(View.GONE);
                tvPhone.setVisibility(View.GONE);
                tvEmail.setVisibility(View.GONE);
                tvDepartment.setVisibility(View.GONE);
                btnEdit.setVisibility(View.GONE);
                break;
            case 4:
                tvName.setVisibility(View.VISIBLE);
                tvSex.setVisibility(View.VISIBLE);
                tvPhone.setVisibility(View.VISIBLE);
                tvEmail.setVisibility(View.VISIBLE);
                tvDepartment.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void updateUI(){
        //设置控件值
        if (userInfo.getName() != null) {
            tvName.setText(userInfo.getName());
        } else {
            tvName.setText("");
        }
        if (userInfo.getSex() == 'm') {
            tvSex.setText("男");
        } else if (userInfo.getSex() == 'w') {
            tvSex.setText("女");
        } else {
            tvSex.setText("");
        }
        if (userInfo.getPhone() != null) {
            tvPhone.setText(userInfo.getPhone());
        } else {
            tvPhone.setText("");
        }
        if (userInfo.getEmail() != null) {
            tvEmail.setText(userInfo.getEmail());
        } else {
            tvEmail.setText("");
        }
        if (userInfo.getDepartment() != null) {
            tvDepartment.setText(userInfo.getDepartment());
        } else {
            tvDepartment.setText("");
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String res = (String) msg.obj;
            try {
                JSONObject jsonObject = new JSONObject(res);
                String result = jsonObject.getString("res");
                if (result.equals("update success")) {
                    UserInfoUtil.saveUserInfo(mContext, InfoUtil.userInfoToJSONObject(userInfo));
                    updateUI();
                    //设置可见性
                    setVisibility(1);
                    setVisibility(4);
                }
                Toast.makeText(mContext,result,Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
