package edu.nedu.nedu_library.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

import edu.nedu.nedu_library.R;
import edu.nedu.nedu_library.entity.BookInfo;
import edu.nedu.nedu_library.entity.BorrowedInfo;
import edu.nedu.nedu_library.net.BorrowedInfoHttpUtil;
import edu.nedu.nedu_library.util.ToolUtil;

/**
 * Created by 小呓的欧尼酱 on 2017/3/12.
 */

public class BorrowedAdapter extends BaseAdapter {

    private ArrayList<BorrowedInfo> borrowedlist;
    private ArrayList<BookInfo> booklist;
    private Context context;

    public BorrowedAdapter(){
        super();
    }

    public BorrowedAdapter(Context context,ArrayList<BorrowedInfo> borrowedlist,ArrayList<BookInfo> booklist){
        this.context = context;
        this.borrowedlist = borrowedlist;
        this.booklist = booklist;
    }

    @Override
    public int getCount() {
        return borrowedlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            view = View.inflate(context, R.layout.item_borrowedinfo_layout, null);
        }

        //3.获取position对应条目数据
        BookInfo bookInfo = booklist.get(position);
        final BorrowedInfo borrowedInfo = borrowedlist.get(position);

        //2.获取view上的子控件对象
        TextView item_tvBookTitle = (TextView) view.findViewById(R.id.item_tvBookTitle);
        TextView item_tvBookContent = (TextView) view.findViewById(R.id.item_tvBookContent);
        TextView item_tvBorrowedContent = (TextView) view.findViewById(R.id.item_tvBorrowedContent);

        view.findViewById(R.id.item_btnRenew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否超期
                boolean flag = true;
                for (BorrowedInfo b : borrowedlist) {
                    if (b.isOverdue()) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    if (borrowedInfo.isRenew()) {
                        BorrowedInfoHttpUtil.requestNetForPostBorrowedInfo(handler, borrowedInfo.getId(), borrowedInfo.getU_id(), 2);
                    } else {
                        Toast.makeText(context, "无法再次续借", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "存在超期图书", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //4.将数据设置给这些子控件
        String isRenew = borrowedInfo.isRenew() ? "是" : "否";
        String isOverdue = borrowedInfo.isOverdue() ? "是" : "否";
        long time = (new Timestamp(System.currentTimeMillis())).getTime() - borrowedInfo.getBorrowedTime().getTime();
        long expire = ToolUtil.BORROWED_MAX_DAY - ((time/1000/60/60) + 12 )/24 ;

        item_tvBookTitle.setText((position+1) + "."+bookInfo.getTitle());
        item_tvBookContent.setText("书目信息: 作者:"+bookInfo.getAuthor());
        item_tvBorrowedContent.setText("借阅信息: 到期天数: " + expire + "天 续借:" + isRenew + " 超期:" + isOverdue);

        return view;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {//续借功能返回信息
            super.handleMessage(msg);
            String res = (String) msg.obj;
            try {
                JSONObject result = new JSONObject(res);
                boolean str = result.getBoolean("res");
                if (str) {
                    Toast.makeText(context,"续借成功",Toast.LENGTH_SHORT).show();
                    int id = result.getInt("id");
                    //续借成功 更新borrowedlist数据
                    for (int i = 0; i < borrowedlist.size(); i++) {
                        if (borrowedlist.get(i).getId() == id) {
                            borrowedlist.get(i).setBorrowedTime(new Timestamp(System.currentTimeMillis()));
                            borrowedlist.get(i).setRenew(false);
                            break;
                        }
                    }
                    //更新listview
                    notifyDataSetChanged();
                }
                else {
                    Toast.makeText(context,"续借失败",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
