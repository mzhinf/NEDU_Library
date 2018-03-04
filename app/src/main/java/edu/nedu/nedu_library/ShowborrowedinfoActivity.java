package edu.nedu.nedu_library;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.nedu.nedu_library.adapter.BorrowedAdapter;
import edu.nedu.nedu_library.entity.BookInfo;
import edu.nedu.nedu_library.entity.BorrowedInfo;
import edu.nedu.nedu_library.net.BorrowedInfoHttpUtil;

public class ShowborrowedinfoActivity extends AppCompatActivity {

    private Context mContext;
    private ListView lvBorrowedInfo;
    private TextView tvBorrowedNum;
    private ArrayList<BorrowedInfo> borrowedlist;
    private ArrayList<BookInfo> booklist;
    private ActionBar actionBar;
    private int u_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showborrowedinfo);
        mContext = this;
        setTitle("借阅信息");

        Intent intent = getIntent();
        u_id = intent.getIntExtra("u_id",-1);

        BorrowedInfoHttpUtil.requestNetForPostBorrowedInfo(handler,-1,u_id,1);

        //1.找到控件
        lvBorrowedInfo = (ListView) findViewById(R.id.lvBorrowedInfo);
        tvBorrowedNum = (TextView) findViewById(R.id.tvBorrowedNum);

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

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String res = (String) msg.obj;

            borrowedlist = new ArrayList<BorrowedInfo>();
            booklist = new ArrayList<BookInfo>();

            try {
                //2.获取数据用list封装到集合中
                JSONObject borrowedjson = new JSONObject(res);
                //json 转化成数据
                JSONArray borrowedArray = borrowedjson.getJSONArray("BorrowedInfo");
                JSONArray bookArray = borrowedjson.getJSONArray("BookInfo");
                for (int i = 0; i < borrowedArray.length(); i++) {
                    BorrowedInfo borrowedInfo = new BorrowedInfo(borrowedArray.getJSONObject(i));
                    BookInfo bookInfo = new BookInfo(bookArray.getJSONObject(i));
                    borrowedlist.add(borrowedInfo);
                    booklist.add(bookInfo);
                }
                if (borrowedlist.size() != 0) {
                    //3.创建一个adapter给ListView
                    BorrowedAdapter borrowedAdapter = new BorrowedAdapter(mContext, borrowedlist, booklist);
                    lvBorrowedInfo.setAdapter(borrowedAdapter);
                    tvBorrowedNum.setText("目前借书" + borrowedlist.size() + "本");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

}
