package edu.nedu.nedu_library.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 小呓的欧尼酱 on 2017/4/13.
 */

public class GetImgHttpUtil {

    public static void getUrl(Handler handler,String ISBN){
        //调用BaseHttpUtil
        //设置参数
        String urlStr = "https://api.douban.com//v2/book/isbn/:"+ISBN;
        String body = "";
        //调用BaseHttpUtil
        BaseHttpUtil.requestNetForPost(urlStr,body,handler);
    }

    public static void getImg(final Handler handler, final String surl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //1.创建一个URL对象
                    URL url = new URL(surl);
                    //2.通过URL对象获取一个HttpUrlConnection对象
                    HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
                    //3.设置HttpUrlConnection对象的一些参数 请求方式 连接的超时时间 请求头信息
                    openConnection.setRequestMethod("GET");    //请求方式
                    openConnection.setConnectTimeout(10*1000);  //连接超时
                    //4.获取响应码，判断响应码是否是 HttpURLConnection.HTTP_OK(200)
                    int code = openConnection.getResponseCode();
                    Message msg = Message.obtain();
                    if (code == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = openConnection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
