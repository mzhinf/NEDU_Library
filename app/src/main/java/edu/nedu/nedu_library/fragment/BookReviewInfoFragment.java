package edu.nedu.nedu_library.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.nedu.nedu_library.R;
import edu.nedu.nedu_library.adapter.BookReviewAdapter;
import edu.nedu.nedu_library.entity.BookInfo;
import edu.nedu.nedu_library.entity.BookReviewInfo;
import edu.nedu.nedu_library.net.BookReviewInfoHttpUtil;
import edu.nedu.nedu_library.util.UserInfoUtil;

/**
 * Created by 小呓的欧尼酱 on 2017/3/22.
 */

public class BookReviewInfoFragment extends Fragment implements View.OnClickListener {

    private TextView tvBookReviewNum;
    //private ListView lvBookReviewInfo;
    //使用PullToRefreshListView 实现下拉与上拉
    private PullToRefreshListView ptrlvBookReviewInfo;
    private EditText edBookReview;
    private RatingBar rbScoring;
    private Button btnSubmit;

    private ArrayList<BookReviewInfo> bookReviewInfoList;
    private ArrayList<String> userIdList;
    private View view;
    private Context mContext;
    private BookInfo bookInfo;
    private int u_id;
    private int page;
    private int bookReviewNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bookreviewinfo, container, false);

        mContext = getActivity();
        initView();     //初始化控件
        initEvent();    //初始化事件
        initData();     //初始化数据

        //访问服务器 获取BookReviewInfo 书评信息
        BookReviewInfoHttpUtil.requestNetForPostGetBookReviewInfo(handler, 1, bookInfo.getId(), page);

        return view;
    }

    private void initView(){
        tvBookReviewNum = (TextView) view.findViewById(R.id.tvBookReviewNum);
        //lvBookReviewInfo = (ListView) view.findViewById(R.id.lvBookReviewInfo);
        ptrlvBookReviewInfo = (PullToRefreshListView) view.findViewById(R.id.ptrlvBookReviewInfo);
        edBookReview = (EditText) view.findViewById(R.id.edBookReview);
        rbScoring = (RatingBar) view.findViewById(R.id.rbScoring);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        /*
        * Mode.BOTH：同时支持上拉下拉
        * Mode.PULL_FROM_START：只支持下拉Pulling Down
        * Mode.PULL_FROM_END：只支持上拉Pulling Up
        * */
        //设置Mode
        ptrlvBookReviewInfo.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void initEvent(){
        btnSubmit.setOnClickListener(this);
        //设定下拉监听函数
        ptrlvBookReviewInfo.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            //设置逻辑: 下拉更新为第一次获取数据 每一次上拉再获取一次数据 每次数据设置为10条
            //下拉Pulling Down
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //更新为0
                page = 0;
                new GetDataTask().execute();
            }

            //上拉Pulling Up
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //page增1
                if ((page + 1) * 10 <= bookReviewNum) {
                    page++;
                }
                new GetDataTask().execute();
            }
        });
    }

    private void initData(){
        //从上个activity获取该书的数据
        Bundle bundle = this.getArguments();
        String res = bundle.getString("bookinfo");
        u_id = UserInfoUtil.getUserId(mContext);
        //初始页为0
        page = 0;
        //获取BookInfo
        try {
            bookInfo = new BookInfo(new JSONObject(res));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //处理从服务器获取的书评信息
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            //Toast.makeText(mContext,result,Toast.LENGTH_SHORT).show();
            if (page == 0) {//page为1 初始化两组数据
                bookReviewInfoList = new ArrayList<BookReviewInfo>();
                userIdList = new ArrayList<String>();
            }
            try {
                //2.获取数据用list封装到集合中
                JSONObject res = new JSONObject(result);
                //json 转化成数据
                JSONArray bookreviewjson = res.getJSONArray("BookReviewInfo");
                JSONArray userjson = res.getJSONArray("UserId");
                bookReviewNum = res.getInt("BookReviewNum");
                for (int i = 0; i < bookreviewjson.length(); i++) {
                    BookReviewInfo bookReviewInfo = new BookReviewInfo(bookreviewjson.getJSONObject(i));
                    String userId = userjson.getString(i);
                    bookReviewInfoList.add(bookReviewInfo);
                    userIdList.add(userId);
                }
                if (bookReviewNum != 0) {
                    //3.创建一个adapter给ListView
                    BookReviewAdapter bookReviewAdapter = new BookReviewAdapter(mContext,bookReviewInfoList,userIdList);
                    //lvBookReviewInfo.setAdapter(bookReviewAdapter);
                    ptrlvBookReviewInfo.setAdapter(bookReviewAdapter);
                    tvBookReviewNum.setText("共" + bookReviewNum + "条评论");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                //Toast.makeText(mContext,"submit",Toast.LENGTH_SHORT).show();
                //组装BookReviewInfo数据
                //评分不能为0
                int rating = (int) rbScoring.getRating();
                if (rating != 0) {
                    if (!edBookReview.getText().toString().equals("")) {
                        BookReviewInfo bookReviewInfo = new BookReviewInfo();
                        bookReviewInfo.setU_id(u_id);
                        bookReviewInfo.setB_id(bookInfo.getId());
                        bookReviewInfo.setGrade(rating);
                        bookReviewInfo.setBookReviewContent(edBookReview.getText().toString());
                        BookReviewInfoHttpUtil.requestNetForPostAddBookReviewInfo(addReview,2,bookReviewInfo);
                    } else {
                        Toast.makeText(mContext,"书评不能为空",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext,"评分不能为0",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    Handler addReview = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            try {
                JSONObject resjson = new JSONObject(result);
                boolean flag = resjson.getBoolean("res");
                if (flag) {
                    Toast.makeText(mContext,"评价成功",Toast.LENGTH_SHORT).show();
                    //访问服务器 获取BookReviewInfo 书评信息
                    page = 0;
                    BookReviewInfoHttpUtil.requestNetForPostGetBookReviewInfo(handler,1,bookInfo.getId(),page);
                    //清空评价
                    edBookReview.setText("");
                    rbScoring.setRating(0.0f);
                } else {
                    Toast.makeText(mContext,"评价失败",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     *  public abstract class AsyncTask<Params, Progress, Result>
     *
     *     Params：doInBackground方法的参数类型
     *     Progress：AsyncTask所执行的后台任务的进度类型
     *     Result：后台任务的返回结果类型
     */
    private class GetDataTask extends AsyncTask<Void, Void, Void> {

        /**
         * doInBackground执行要于后台执行的语句，返回的值可以是任意类型，
         * 但要提前在extends AsyncTask<Void, Void, String> 中定义，
         * 这个返回值会做为onPostExecute的参数result传到onPostExecute函数中；
         * 如果对于网络访问程序，doInBackground就执行访问网络的代码，
         * 然后讲返回值存在result中传给onPostExecute函数，以刷新列表；
         * @doInBackground
         */
        //子线程请求数据
        @Override
        protected Void doInBackground(Void... params) {
            if (page * 10 <= bookReviewNum) {
                BookReviewInfoHttpUtil.requestNetForPostGetBookReviewInfo(handler, 1, bookInfo.getId(), page);
            }
            return null;
        }


        /**
         * onPostExecute（）是对返回的值进行操作，并添加到ListView的列表中，
         * 有两种添加方式添加到头部----mListItems.addFirst(result);
         * 和添加在尾部----mListItems.addLast(result);
         * @onPostExecute
         */
        //主线程更新UI
        @Override
        protected void onPostExecute(Void result){
            ptrlvBookReviewInfo.onRefreshComplete();
        }
    }

}
