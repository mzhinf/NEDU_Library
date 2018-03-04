package edu.nedu.nedu_library;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.nedu.nedu_library.entity.BookInfo;
import edu.nedu.nedu_library.net.BookInfoHttpUtil;
import edu.nedu.nedu_library.net.BorrowedInfoHttpUtil;
import edu.nedu.nedu_library.net.GetImgHttpUtil;

public class ScaningActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnScaning;
    private Button btnBorrow;
    private Button btnReturn;
    private Button btnClear;
    private TextView tvBookTitle;
    private TextView tvBookAuthor;
    private TextView tvBookISBN;
    private TextView tvBookBorrow;
    private TextView tvBookCode;
    private ImageView imgCover;

    private ActionBar actionBar;

    private Context mContext;
    private BookInfo bookInfo;
    private int u_id;
    private String ISBN;
    private int code;
    private String url;


    private int REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaning);
        mContext = this;
        setTitle("扫码借阅");
        Intent intent = getIntent();
        u_id = intent.getIntExtra("u_id",-1);
        initView();     //初始化控件
        initEvent();    //初始化事件
        initData();     //初始化数据
    }

    private void initView() {
        btnScaning = (Button) findViewById(R.id.btnScaning);
        btnBorrow = (Button) findViewById(R.id.btnBorrow);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        btnClear = (Button) findViewById(R.id.btnClear);
        tvBookTitle = (TextView) findViewById(R.id.tvBookTitle);
        tvBookAuthor = (TextView) findViewById(R.id.tvBookAuthor);
        tvBookISBN = (TextView) findViewById(R.id.tvBookISBN);
        tvBookBorrow = (TextView) findViewById(R.id.tvBookBorrow);
        tvBookCode = (TextView) findViewById(R.id.tvBookCode);
        imgCover = (ImageView) findViewById(R.id.imgCover);
    }

    private void initEvent() {
        btnScaning.setOnClickListener(this);
        btnBorrow.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        //初始化
        ZXingLibrary.initDisplayOpinion(this);
    }

    private void initData() {
        bookInfo = null;
        setData();
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScaning:
                Intent intent = new Intent(ScaningActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.btnBorrow:
                if (bookInfo != null){
                    BorrowedInfoHttpUtil.borrowAndreturn(borrowAndreturn, 3, u_id, bookInfo.getId(), code);
                } else {
                    Toast.makeText(mContext, "没有图书信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnReturn:
                if (bookInfo != null){
                    BorrowedInfoHttpUtil.borrowAndreturn(borrowAndreturn, 4, u_id, bookInfo.getId(), code);
                } else {
                    Toast.makeText(mContext, "没有图书信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnClear:
                bookInfo = null;
                setData();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {// 如果返回码是可以用的
            if (requestCode == REQUEST_CODE) {
                //处理扫描结果（在界面上显示）
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        /**
                         * str格式
                         * {"ISBN": "1234567891234","code": "1"}
                         */
                        String str = bundle.getString(CodeUtils.RESULT_STRING);
                        //Toast.makeText(mContext, "str:" + str, Toast.LENGTH_LONG).show();
                        try {//获取信息
                            JSONObject jsonObject = new JSONObject(str);
                            ISBN = jsonObject.getString("ISBN");
                            code = jsonObject.getInt("code");
                            BookInfoHttpUtil.requestNetForPostBookInfo(getBookInfo,ISBN,"ISBN",0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(mContext, "解析失败", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    Handler getBookInfo = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String res = (String) msg.obj;
            //Toast.makeText(mContext,res,Toast.LENGTH_SHORT).show();
            ArrayList<BookInfo> booklist = new ArrayList<BookInfo>();
            try {
                //2.获取数据用list封装到集合中
                JSONObject bookjson = new JSONObject(res);
                //json 转化成数据
                JSONArray jsonarray = bookjson.getJSONArray("BookInfo");
                int bookNum = bookjson.getInt("bookNum");
                if (bookNum == 1) {
                    bookInfo = new BookInfo(jsonarray.getJSONObject(0));
                    setData();
                    GetImgHttpUtil.getUrl(getUrl,bookInfo.getISBN());
                } else {
                    Toast.makeText(mContext, "找不到该书", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Handler getUrl = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = (String) msg.obj;
            if (str != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                    JSONObject urlJson = jsonObject.getJSONObject("images");
                    url = urlJson.getString("large");
                    //Toast.makeText(mContext, url, Toast.LENGTH_SHORT).show();
                    GetImgHttpUtil.getImg(setImg, url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Handler setImg = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            imgCover.setImageBitmap(bitmap);
        }
    };

    Handler borrowAndreturn = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = (String) msg.obj;
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(str);
                int error = jsonObject.getInt("error");
                if (error == 300){
                    str = "借阅成功";
                } else if (error == 310) {
                    str = "还书成功";
                } else {
                    str = "error:" + error;
                }
                Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void setData() {
        if (bookInfo != null) {
            tvBookTitle.setText(bookInfo.getTitle());
            tvBookAuthor.setText(bookInfo.getAuthor());
            tvBookISBN.setText(bookInfo.getISBN());
            int available = bookInfo.getAmount() - bookInfo.getBorrowedNumber() - bookInfo.getReservationNumber();
            tvBookBorrow.setText(String.valueOf(available));
            tvBookCode.setText(String.valueOf(code));
        } else {
            tvBookTitle.setText("");
            tvBookAuthor.setText("");
            tvBookISBN.setText("");
            tvBookBorrow.setText("");
            tvBookCode.setText("");
            imgCover.setImageBitmap(null);
        }
    }
}
