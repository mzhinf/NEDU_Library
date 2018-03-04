package edu.nedu.nedu_library.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.nedu.nedu_library.R;
import edu.nedu.nedu_library.entity.BookInfo;
import edu.nedu.nedu_library.net.GetImgHttpUtil;
import edu.nedu.nedu_library.net.ReservationInfoHttpUtil;
import edu.nedu.nedu_library.util.UserInfoUtil;

/**
 * Created by 小呓的欧尼酱 on 2017/3/22.
 */

public class BookInfoFragment extends Fragment implements View.OnClickListener {

    private TextView tvBookTitle;
    private TextView tvBookAuthor;
    private TextView tvBookContent;
    private Button btnReservation;
    private ImageView imgCover;

    private View view;
    private Context mContext;
    private BookInfo bookInfo;
    private String url;
    private int u_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bookinfo, container, false);
        mContext = getActivity();
        initView();     //初始化控件
        initEvent();    //初始化事件
        initData();     //初始化数据
        return view;
    }

    //加载控件
    private void initView(){
        tvBookTitle = (TextView) view.findViewById(R.id.tvBookTitle);
        tvBookAuthor = (TextView) view.findViewById(R.id.tvBookAuthor);
        tvBookContent = (TextView) view.findViewById(R.id.tvBookContent);
        btnReservation = (Button) view.findViewById(R.id.btnReservation);
        imgCover = (ImageView) view.findViewById(R.id.imgCover);
    }

    //加载事件
    private void initEvent(){
        btnReservation.setOnClickListener(this);
    }

    //加载数据
    private void initData(){
        Bundle bundle = this.getArguments();
        String res = bundle.getString("bookinfo");
        //获取BookInfo
        try {
            bookInfo = new BookInfo(new JSONObject(res));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //获取User id
        u_id = UserInfoUtil.getUserInfo(mContext).getId();
        //绘制ui
        tvBookTitle.setText(bookInfo.getTitle());
        tvBookAuthor.setText("作者: "+bookInfo.getAuthor());
        tvBookContent.setText(getContent());
        GetImgHttpUtil.getUrl(getUrl,bookInfo.getISBN());
    }

    //将bookinfo数据转化成content内容
    private String getContent(){
        String str = "ISBN: " + bookInfo.getISBN() + "\n";

        if (bookInfo.getPublishing() != null) {
            str = str + "出版社: " + bookInfo.getPublishing() + "\n";
        }
        if (bookInfo.getSubject() != null) {
            str = str + "学科主题: " + bookInfo.getSubject() + "\n";
        }
        if (bookInfo.getSearchNumber() != null) {
            str = str + "索书号: " + bookInfo.getSearchNumber() + "\n";
        }
        if (bookInfo.getSummary() != null) {
            str = str + "简介: " + bookInfo.getSummary() + "\n";
        }

        int available = bookInfo.getAmount() - bookInfo.getBorrowedNumber() - bookInfo.getReservationNumber();

        str = str + "馆藏信息: 馆藏数量:" + bookInfo.getAmount()
                + " 可借副本:" + available;

        return str;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnReservation:
                //进行读者预约
                int available = bookInfo.getAmount() - bookInfo.getBorrowedNumber() - bookInfo.getReservationNumber();
                if (available == 0) {
                    Toast.makeText(mContext, "没有可借副本", Toast.LENGTH_SHORT).show();
                } else {
                    ReservationInfoHttpUtil.requestNetForPostReservationInfo(handler,2,u_id,bookInfo.getId());
                }
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String res = (String) msg.obj;
            JSONObject result = null;
            try {
                result = new JSONObject(res);
                boolean flag = result.getBoolean("res");
                if (flag) {
                    Toast.makeText(mContext,"预约成功",Toast.LENGTH_SHORT).show();
                    bookInfo.setReservationNumber(bookInfo.getReservationNumber() + 1);
                    //重新设置
                    tvBookContent.setText(getContent());
                }
                else {
                    Toast.makeText(mContext,"预约失败",Toast.LENGTH_SHORT).show();
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
}
