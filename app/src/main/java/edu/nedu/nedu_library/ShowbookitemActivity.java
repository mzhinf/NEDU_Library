package edu.nedu.nedu_library;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.nedu.nedu_library.fragment.BookInfoFragment;
import edu.nedu.nedu_library.fragment.BookReviewInfoFragment;
import edu.nedu.nedu_library.indicator.ViewPagerIndicator;

public class ShowbookitemActivity extends AppCompatActivity implements View.OnClickListener {

    //声明ViewPagerIndicator
    private ViewPagerIndicator indicator;
    //声明ViewPager
    private ViewPager vpBookItem;
    //适配器
    private FragmentPagerAdapter bookItemAdapter;
    //装载Fragment的集合
    private List<Fragment> fragmentList;
    //加入集合的fragment
    private BookInfoFragment bookInfoFg;
    private BookReviewInfoFragment bookReviewInfoFg;

    private TextView tvBookInfo;
    private TextView tvBookReviewInfo;

    private ActionBar actionBar;

    private Context mContext;
    private String toBookInfoFg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbookitem);
        mContext = this;
        setTitle("书目信息");

        Intent intent = getIntent();
        toBookInfoFg = intent.getStringExtra("bookinfo");

        initView();     //初始化控件
        initEvent();    //初始化事件
        initData();     //初始化数据
    }

    //加载控件
    private void initView() {
        vpBookItem = (ViewPager) findViewById(R.id.vpBookItem);
        tvBookInfo = (TextView) findViewById(R.id.tvBookInfo);
        tvBookReviewInfo = (TextView) findViewById(R.id.tvBookReviewInfo);
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
    }

    //加载事件
    private void initEvent() {
        tvBookInfo.setOnClickListener(this);
        tvBookReviewInfo.setOnClickListener(this);
    }

    //加载数据
    private void initData() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        fragmentList = new ArrayList<>();
        //创建fragment
        bookInfoFg = new BookInfoFragment();
        bookReviewInfoFg = new BookReviewInfoFragment();
        //给子fragment传递数据
        Bundle bundle = new Bundle();
        bundle.putString("bookinfo",toBookInfoFg);
        //bookInfoFg传递
        bookInfoFg.setArguments(bundle);
        bookReviewInfoFg.setArguments(bundle);
        //将两个tab fragment加入集合
        fragmentList.add(bookInfoFg);
        fragmentList.add(bookReviewInfoFg);

        //初始化适配器
        bookItemAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };

        //为ViewPager加载适配器
        vpBookItem.setAdapter(bookItemAdapter);

        //设置tab数目
        indicator.setVisibleTabs(2);
        //为indicator设置ViewPager
        indicator.setViewPager(vpBookItem);

        //在indicator设置ViewPager的切换监听
        indicator.setViewPagerListener(new ViewPagerIndicator.ViewPagerListener(){

            //页面滚动事件
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                selectTabFg(position);
            }
            //页面滚动状态改变事件
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /*
            indicator设置后ViewPager不需要再次设置
        //设置ViewPager的切换监听
        vpBookItem.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页面滚动事件
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                selectTabFg(position);
            }
            //页面滚动状态改变事件
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        */
    }

    private void selectTabFg(int position){
        switch (position) {
            case 0:
                tvBookInfo.setTextColor(Color.parseColor("#fdf6e3"));
                tvBookReviewInfo.setTextColor(Color.parseColor("#969696"));
                break;
            case 1:
                tvBookInfo.setTextColor(Color.parseColor("#969696"));
                tvBookReviewInfo.setTextColor(Color.parseColor("#fdf6e3"));
                break;
        }
        vpBookItem.setCurrentItem(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvBookInfo:
                selectTabFg(0);
                break;
            case R.id.tvBookReviewInfo:
                selectTabFg(1);
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
