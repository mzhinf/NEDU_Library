package edu.nedu.nedu_library.net;

import android.os.Handler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import edu.nedu.nedu_library.util.ToolUtil;

/**
 * Created by 小呓的欧尼酱 on 2017/3/12.
 */

public class BorrowedInfoHttpUtil {

    public static void requestNetForPostBorrowedInfo(Handler handler, int id, int u_id, int way){

        //调用BaseHttpUtil
        //设置参数
        String urlStr = ToolUtil.url + "/BorrowedInfoServlet";
        String body = null;
        try {
            body = "way="+ URLEncoder.encode(String.valueOf(way),"utf-8")
                    + "&id=" + URLEncoder.encode(String.valueOf(id),"utf-8")
                    + "&u_id=" + URLEncoder.encode(String.valueOf(u_id),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //调用BaseHttpUtil
        BaseHttpUtil.requestNetForPost(urlStr,body,handler);
    }

    public static void borrowAndreturn(Handler handler, int way, int u_id, int b_id, int b_key) {

        //调用BaseHttpUtil
        //设置参数
        String urlStr = ToolUtil.url + "/BorrowedInfoServlet";
        String body = null;
        try {
            body = "way=" + URLEncoder.encode(String.valueOf(way), "utf-8")
                    + "&u_id=" + URLEncoder.encode(String.valueOf(u_id), "utf-8")
                    + "&b_id=" + URLEncoder.encode(String.valueOf(b_id), "utf-8")
                    + "&b_key=" + URLEncoder.encode(String.valueOf(b_key), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //调用BaseHttpUtil
        BaseHttpUtil.requestNetForPost(urlStr, body, handler);
    }

}
