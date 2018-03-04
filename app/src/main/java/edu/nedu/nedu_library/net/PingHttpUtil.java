package edu.nedu.nedu_library.net;

import android.os.Handler;

import edu.nedu.nedu_library.util.ToolUtil;

/**
 * Created by 小呓的欧尼酱 on 2017/4/6.
 */

public class PingHttpUtil {

    public static void ping(Handler handler){
        //调用BaseHttpUtil
        //设置参数
        String urlStr = ToolUtil.url + "/PingServlet";
        String body = "";
        //调用BaseHttpUtil
        BaseHttpUtil.requestNetForPost(urlStr,body,handler);
    }

}
