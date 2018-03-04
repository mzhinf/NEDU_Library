package edu.nedu.nedu_library;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import edu.nedu.nedu_library.adapter.FAQExpandableListAdapter;
import edu.nedu.nedu_library.impl.SimpleImpl;

public class FAQActivity extends AppCompatActivity implements SimpleImpl{

    private ExpandableListView elvFAQ;

    private ActionBar actionBar;

    private Context mContext;
    private List<String> questions;
    private List<String> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        mContext = this;
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        elvFAQ = (ExpandableListView) findViewById(R.id.elvFAQ);
    }

    @Override
    public void initEvent() {
        elvFAQ.setAdapter(new FAQExpandableListAdapter(mContext,questions,answers));
    }

    @Override
    public void initData() {
        setTitle("F.A.Q");
        questions = new ArrayList<String>();
        answers = new ArrayList<String>();
        questions.add("如何借阅");
        answers.add("点击右上方的扫码借阅，扫描书目的借阅二维码，提交即可完成借阅。");
        questions.add("如何还书");
        answers.add("点击右上方的扫码借阅，扫描书目的借阅二维码，点击还书即可完成还书。");
        questions.add("如何预约");
        answers.add("搜索你想看的书，在数目详情页内点击读者预约，即可完成预约。");
        questions.add("如何准确的找到一本书");
        answers.add("强烈建议您使用高级搜索，这样可以加快你的搜书速度。");
        questions.add("意见建议");
        answers.add("您可以在意见反馈页内，向我们提出你宝贵的意见与建议。谢谢！");

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
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
