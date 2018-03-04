package edu.nedu.nedu_library.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nedu.nedu_library.R;
import edu.nedu.nedu_library.entity.BookInfo;

/**
 * Created by 小呓的欧尼酱 on 2017/1/26.
 */

public class BookAdapter extends BaseAdapter {

    private ArrayList<BookInfo> list;
    private int page;
    private Context context;

    public BookAdapter() {
        super();
    }

    public BookAdapter(Context context, ArrayList<BookInfo> list, int page){
        this.list = list;
        this.context = context;
        this.page = page;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            view = View.inflate(context, R.layout.item_bookinfo_layout, null);
        }
        //2.获取view上的子控件对象
        TextView item_tvBookTitle = (TextView) view.findViewById(R.id.item_tvBookTitle);
        TextView item_tvBookContent = (TextView) view.findViewById(R.id.item_tvBookContent);
        TextView item_tvBookAmount = (TextView) view.findViewById(R.id.item_tvBookAmount);
        //3.获取position对应条目数据
        BookInfo bookInfo = list.get(position);
        //4.将数据设置给这些子控件
        int available = bookInfo.getAmount() - bookInfo.getBorrowedNumber() - bookInfo.getReservationNumber();

        item_tvBookTitle.setText((page*10+position+1)+"."+bookInfo.getTitle());
        item_tvBookContent.setText("书目信息: 作者:"+bookInfo.getAuthor());
        item_tvBookAmount.setText("馆藏信息: 馆藏数量:"+bookInfo.getAmount()+" 可借副本:"+available);
        return view;
    }
}
