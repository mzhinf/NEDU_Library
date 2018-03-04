package edu.nedu.nedu_library;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.nedu.nedu_library.impl.SimpleImpl;
import edu.nedu.nedu_library.net.AdviseInfoHttpUtil;

public class FeedbackActivity extends AppCompatActivity implements SimpleImpl, View.OnClickListener {

    private EditText edOpinion,edEmail;
    private TextView tvWordLlimit;
    private Button btnSubmit;

    private ActionBar actionBar;

    private Context mContext;
    private int u_id;
    private String advise;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mContext = this;
        Intent intent = getIntent();
        u_id = intent.getIntExtra("u_id",-1);

        initView();     //初始化控件
        initEvent();    //初始化事件
        initData();     //初始化数据
    }

    @Override
    public void initView() {
        edOpinion = (EditText) findViewById(R.id.edOpinion);
        edEmail = (EditText) findViewById(R.id.edEmail);
        tvWordLlimit = (TextView) findViewById(R.id.tvWordLlimit);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    @Override
    public void initEvent() {
        btnSubmit.setOnClickListener(this);
        edOpinion.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void initData() {
        setTitle("意见反馈");
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                getData();
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

    /**
     * 使用一个TextWatcher监视EditText变化
     * 达到统计字符数量 控制字符长度
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        private int editStart;
        private int editEnd;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = edOpinion.getSelectionStart();
            editEnd = edOpinion.getSelectionEnd();
            // 先去掉监听器，否则会出现栈溢出
            edOpinion.removeTextChangedListener(mTextWatcher);
            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (s.toString().length() > 300) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            // 恢复监听器
            edOpinion.addTextChangedListener(mTextWatcher);
            tvWordLlimit.setText(String.valueOf(300-s.toString().length()));
        }
    };

    private void getData() {
        if (edOpinion.getText().toString() != null && !edOpinion.getText().toString().equals("")) {
            advise = edOpinion.getText().toString();
            email = edEmail.getText().toString();
            if (!email.equals("") && !email.contentEquals("@")) {
                Toast.makeText(mContext, "email不符合规范", Toast.LENGTH_SHORT).show();
            } else {
                AdviseInfoHttpUtil.sendAdviseInfo(sendAdvise, advise, email, u_id);
            }
            setData();
        } else {
            Toast.makeText(mContext, "no content", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        edOpinion.setText("");
        edEmail.setText("");
    }

    Handler sendAdvise = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;

            if (result.equals("true")) {
                Toast.makeText(mContext, "建议已提交，我们会尽快处理。", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "建议提交失败", Toast.LENGTH_SHORT).show();
            }

        }
    };


}
