package edu.nedu.nedu_library;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.nedu.nedu_library.impl.SimpleImpl;
import edu.nedu.nedu_library.util.ToolUtil;
import edu.nedu.nedu_library.util.UserInfoUtil;

public class SettingsActivity extends AppCompatActivity implements SimpleImpl, View.OnClickListener {

    private TextView tvLogout;
    private TextView tvVersion;

    private ActionBar actionBar;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        initView();
        initEvent();
        initData();
    }

    @Override
    public void initView() {
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
    }

    @Override
    public void initEvent() {
        tvLogout.setOnClickListener(this);
        tvVersion.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setTitle("设置");
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogout:
                UserInfoUtil.deleteUserInfo(mContext);
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class).setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
                break;
            case R.id.tvVersion:
                Toast.makeText(mContext, "Version:" + ToolUtil.Version, Toast.LENGTH_SHORT).show();
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
}
