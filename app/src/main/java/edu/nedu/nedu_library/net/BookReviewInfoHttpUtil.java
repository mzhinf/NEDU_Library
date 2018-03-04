package edu.nedu.nedu_library.net;

import android.os.Handler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import edu.nedu.nedu_library.entity.BookReviewInfo;
import edu.nedu.nedu_library.util.ToolUtil;

/**
 * Created by 小呓的欧尼酱 on 2017/3/23.
 */

public class BookReviewInfoHttpUtil {

    //获取书评信息
    public static void requestNetForPostGetBookReviewInfo(Handler handler, int way, int b_id, int page) {
        //调用BaseHttpUtil
        //设置参数
        String urlStr = ToolUtil.url + "/BookReviewServlet";
        String body = null;
        try {
            body = "&way=" + URLEncoder.encode(String.valueOf(way), "utf-8")
                    + "&b_id=" + URLEncoder.encode(String.valueOf(b_id), "utf-8")
                    + "&page=" + URLEncoder.encode(String.valueOf(page), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //调用BaseHttpUtil
        BaseHttpUtil.requestNetForPost(urlStr, body, handler);
    }

    //添加书评信息
    public static void requestNetForPostAddBookReviewInfo(Handler handler, int way, BookReviewInfo bookReviewInfo) {
        //调用BaseHttpUtil
        //设置参数
        String urlStr = ToolUtil.url + "/BookReviewServlet";
        String body = null;
        try {
            body = "&way=" + URLEncoder.encode(String.valueOf(way), "utf-8")
                    + "&bookReviewInfo=" + URLEncoder.encode(bookReviewInfo.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //调用BaseHttpUtil
        BaseHttpUtil.requestNetForPost(urlStr, body, handler);
    }

}
