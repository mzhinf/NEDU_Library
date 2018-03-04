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

import edu.nedu.nedu_library.adapter.ReservationAdaper;
import edu.nedu.nedu_library.entity.BookInfo;
import edu.nedu.nedu_library.entity.ReservationInfo;
import edu.nedu.nedu_library.net.ReservationInfoHttpUtil;

public class ShowreservationinfoActivity extends AppCompatActivity {

    private Context mContext;
    private ListView lvReservationInfo;
    private TextView tvReservationNum;
    private ArrayList<ReservationInfo> reservationlist;
    private ArrayList<BookInfo> booklist;
    private ActionBar actionBar;
    private int u_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showreservationinfo);
        mContext = this;
        setTitle("预约信息");

        Intent intent = getIntent();
        u_id = intent.getIntExtra("u_id",-1);

        ReservationInfoHttpUtil.requestNetForPostReservationInfo(handler,1,u_id,-1);

        //1.找到控件
        lvReservationInfo = (ListView) findViewById(R.id.lvReservationInfo);
        tvReservationNum = (TextView) findViewById(R.id.tvReservationNum);

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

            reservationlist = new ArrayList<ReservationInfo>();
            booklist = new ArrayList<BookInfo>();

            try {
                JSONObject reservationjson = new JSONObject(res);
                JSONArray reservationArray = reservationjson.getJSONArray("ReservationInfo");
                JSONArray bookArray = reservationjson.getJSONArray("BookInfo");
                for (int i = 0; i < reservationArray.length(); i++) {
                    ReservationInfo reservationInfo = new ReservationInfo(reservationArray.getJSONObject(i));
                    BookInfo bookInfo = new BookInfo(bookArray.getJSONObject(i));
                    reservationlist.add(reservationInfo);
                    booklist.add(bookInfo);
                }
                if (reservationlist.size() != 0) {
                    ReservationAdaper reservationAdaper = new ReservationAdaper(mContext, reservationlist, booklist);
                    lvReservationInfo.setAdapter(reservationAdaper);
                    tvReservationNum.setText("目前预约" + reservationlist.size() + "本");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
