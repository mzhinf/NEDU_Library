package edu.nedu.nedu_library;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.nedu.nedu_library.adapter.BookAdapter;
import edu.nedu.nedu_library.entity.BookInfo;
import edu.nedu.nedu_library.net.BookInfoHttpUtil;

public class ShowbookinfoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView lvBookInfo;
    private TextView tvBookNum;
    private TextView tvPageNum;
    private TextView tvPreviousPage;
    private TextView tvNextPage;
    private ActionBar actionBar;
    private ArrayList<BookInfo> booklist;
    private int page;
    private int pageMax;
    private String str;
    private String way;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbookinfo);
        mContext = this;
        setTitle("馆藏信息");

        Intent intent = getIntent();
        str = intent.getStringExtra("str");
        way = intent.getStringExtra("way");
        page = 1;

        BookInfoHttpUtil.requestNetForPostBookInfo(handler,str,way,page-1);

        //1.找到控件
        lvBookInfo = (ListView) findViewById(R.id.lvBookInfo);
        tvBookNum = (TextView) findViewById(R.id.tvBookNum);
        tvPageNum = (TextView) findViewById(R.id.tvPageNum);
        tvPreviousPage = (TextView) findViewById(R.id.tvPreviousPage);
        tvNextPage = (TextView) findViewById(R.id.tvNextPage);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        lvBookInfo.setOnItemClickListener(this);
        tvPreviousPage.setOnClickListener(this);
        tvNextPage.setOnClickListener(this);
    }

    //创建一个handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String res = (String) msg.obj;
            //Toast.makeText(mContext,res,Toast.LENGTH_SHORT).show();
            booklist = new ArrayList<BookInfo>();
            try {
                //2.获取数据用list封装到集合中
                JSONObject bookjson = new JSONObject(res);
                //json 转化成数据
                JSONArray jsonarray = bookjson.getJSONArray("BookInfo");
                int bookNum = bookjson.getInt("bookNum");
                pageMax = (int) Math.ceil(bookNum/10.0);
                for (int i = 0; i < jsonarray.length(); i++) {
                    BookInfo bookInfo = new BookInfo(jsonarray.getJSONObject(i));
                    booklist.add(bookInfo);
                }
                if (booklist.size() != 0) {
                    //3.创建一个adapter给ListView
                    BookAdapter bookAdapter = new BookAdapter(mContext, booklist, page - 1);
                    lvBookInfo.setAdapter(bookAdapter);
                    tvBookNum.setText("共有" + bookNum + "条搜索结果");
                    tvPageNum.setText(page + "/" + pageMax);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvPreviousPage:
                if (page > 1) {
                    page--;
                    BookInfoHttpUtil.requestNetForPostBookInfo(handler, str, way, page - 1);
                }
                break;
            case R.id.tvNextPage:
                if (page < pageMax) {
                    page++;
                    BookInfoHttpUtil.requestNetForPostBookInfo(handler, str, way, page - 1);
                }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /**
         * @param parent 当前ListView
         * @param view 代表当前被点击的条目
         * @param position 当前条目的位置
         * @param id 当前被点击的条目的id
         */
        Intent intent = new Intent(mContext,ShowbookitemActivity.class);
        intent.putExtra("bookinfo",booklist.get(position).toString());
        startActivity(intent);
    }
}
