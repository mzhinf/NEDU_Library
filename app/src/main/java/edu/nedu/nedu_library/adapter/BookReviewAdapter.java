package edu.nedu.nedu_library.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.nedu.nedu_library.R;
import edu.nedu.nedu_library.entity.BookReviewInfo;

/**
 * Created by 小呓的欧尼酱 on 2017/3/23.
 */

public class BookReviewAdapter extends BaseAdapter {

    private ArrayList<BookReviewInfo> bookReviewInfoList;
    private ArrayList<String> userIdList;
    private Context context;

    public BookReviewAdapter() {
        super();
    }

    public BookReviewAdapter(Context context, ArrayList<BookReviewInfo> bookReviewInfoList, ArrayList<String> userIdList) {
        this.context = context;
        this.bookReviewInfoList = bookReviewInfoList;
        this.userIdList = userIdList;
    }

    @Override
    public int getCount() {
        return bookReviewInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookReviewInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        //1.复用convertView优化ListView
        if (convertView != null) {
            view = convertView;
        } else {
            //context: 上下文 resource:layout的id root:将layout用root包一层 一般传null
            view = View.inflate(context, R.layout.item_bookreviewinfo_layout, null);
        }

        //2.获取view上的子控件对象
        RatingBar item_rbGrade = (RatingBar) view.findViewById(R.id.item_rbGrade);
        TextView item_tvUserId = (TextView) view.findViewById(R.id.item_tvUserId);
        TextView item_tvReviewTime = (TextView) view.findViewById(R.id.item_tvReviewTime);
        TextView item_tvBookReviewContent = (TextView) view.findViewById(R.id.item_tvBookReviewContent);

        //3.获取position对应条目数据
        BookReviewInfo bookReviewInfo = bookReviewInfoList.get(position);

        //4.将数据设置给这些子控件
        //获取时间
        Date date = new Date(bookReviewInfo.getReviewTime().getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(date);

        item_rbGrade.setRating((float)bookReviewInfo.getGrade());
        item_tvUserId.setText(userIdList.get(position));
        item_tvReviewTime.setText(" " + dateStr);
        item_tvBookReviewContent.setText(bookReviewInfo.getBookReviewContent());
        return view;
    }
}
