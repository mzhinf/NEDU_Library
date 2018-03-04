package edu.nedu.nedu_library.net;

import android.os.Handler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import edu.nedu.nedu_library.util.ToolUtil;

/**
 * Created by 小呓的欧尼酱 on 2017/3/16.
 */

public class ReservationInfoHttpUtil {

    public static void requestNetForPostReservationInfo(final Handler handler, final int way, final int u_id, final int b_id){

        //调用BaseHttpUtil
        //设置参数
        String urlStr = ToolUtil.url + "/ReservationInfoServlet";
        String body = null;
        try {
            body = "way="+ URLEncoder.encode(String.valueOf(way),"utf-8")
                    + "&u_id=" + URLEncoder.encode(String.valueOf(u_id),"utf-8")
                    + "&b_id=" + URLEncoder.encode(String.valueOf(b_id),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //调用BaseHttpUtil
        BaseHttpUtil.requestNetForPost(urlStr,body,handler);

        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //1.创建一个URL对象
                    URL url = new URL("http://192.168.214.42:8080/NEDULibrary/ReservationInfoServlet");
                    //2.通过URL对象获取一个HttpUrlConnection对象
                    HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
                    //3.设置HttpUrlConnection对象的一些参数 请求方式 连接的超时时间 请求头信息
                    openConnection.setRequestMethod("POST");    //请求方式
                    openConnection.setConnectTimeout(10*1000);  //连接超时
                    //设置请求头
                    String body = "way="+ URLEncoder.encode(String.valueOf(way),"utf-8")
                            + "&u_id=" + URLEncoder.encode(String.valueOf(u_id),"utf-8")
                            + "&b_id=" + URLEncoder.encode(String.valueOf(b_id),"utf-8");
                    openConnection.setDoOutput(true);//设置UrlConnection可以写入内容
                    openConnection.getOutputStream().write(body.getBytes());//获取一个outputstream
                    //4.获取响应码，判断响应码是否是 HttpURLConnection.HTTP_OK(200)
                    int code = openConnection.getResponseCode();
                    if(code == HttpURLConnection.HTTP_OK){
                        InputStream inputStream = openConnection.getInputStream();
                        //5.获取网络链接的读取流信息，将流转换成字符串。 ByteArrayOutputStream
                        String result = StreamUtil.streamToString(inputStream);
                        Message msg = Message.obtain();
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        */

    }

}
