package edu.nedu.nedu_library.net;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.nedu.nedu_library.util.StreamUtil;

/**
 * Created by 小呓的欧尼酱 on 2017/3/16.
 * 使用BaseHttpUtil减少Http代码量
 */

public class BaseHttpUtil {
    /**
     * @urlStr 为url地址
     * @body 为访问请求头
     * @handler 为访问结果返回
     * 调用BaseHttpUtil
     * 设置参数
     * urlStr
     * body
     * 调用BaseHttpUtil
     * BaseHttpUtil.requestNetForPost(urlStr,body,handler);
     */
    public static void requestNetForPost(final String urlStr, final String body, final Handler handler){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //1.创建一个URL对象
                    URL url = new URL(urlStr);
                    //2.通过URL对象获取一个HttpUrlConnection对象
                    HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
                    //3.设置HttpUrlConnection对象的一些参数 请求方式 连接的超时时间 请求头信息
                    openConnection.setRequestMethod("POST");    //请求方式
                    openConnection.setConnectTimeout(10*1000);  //连接超时
                    //设置请求头
                    openConnection.setDoOutput(true);//设置UrlConnection可以写入内容
                    openConnection.getOutputStream().write(body.getBytes());//获取一个outputstream
                    //4.获取响应码，判断响应码是否是 HttpURLConnection.HTTP_OK(200)
                    int code = openConnection.getResponseCode();
                    Message msg = Message.obtain();
                    msg.what = code;//传递成功或者失败信息
                    if (code == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = openConnection.getInputStream();
                        //5.获取网络链接的读取流信息，将流转换成字符串。 ByteArrayOutputStream
                        String result = StreamUtil.streamToString(inputStream);
                        msg.obj = result;
                        handler.sendMessage(msg);
                    } else {
                        //传递失败信息
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
