package edu.nedu.nedu_library.net;

import android.os.Handler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import edu.nedu.nedu_library.util.ToolUtil;

/**
 * Created by 小呓的欧尼酱 on 2017/4/19.
 */

public class AdviseInfoHttpUtil {

    public static void sendAdviseInfo(Handler handler, String advise, String email, int u_id) {
        //调用BaseHttpUtil
        //设置参数
        String urlStr = ToolUtil.url + "/AdviseInfoServlet";
        String body = null;
        try {
            body = "advise="+ URLEncoder.encode(advise,"utf-8")
                    +"&email="+URLEncoder.encode(email,"utf-8")
                    +"&u_id="+URLEncoder.encode(String.valueOf(u_id),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //调用BaseHttpUtil
        BaseHttpUtil.requestNetForPost(urlStr,body,handler);
    }

}
