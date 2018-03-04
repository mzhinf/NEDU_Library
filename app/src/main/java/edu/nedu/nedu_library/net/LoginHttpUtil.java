package edu.nedu.nedu_library.net;

import android.os.Handler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import edu.nedu.nedu_library.entity.UserInfo;
import edu.nedu.nedu_library.util.InfoUtil;
import edu.nedu.nedu_library.util.ToolUtil;

/**
 * Created by 小呓的欧尼酱 on 2017/1/25.
 */

public class LoginHttpUtil {

    public static void requestNetForPostLogin(Handler handler, UserInfo userInfo, int way){

        //调用BaseHttpUtil
        //设置参数
        String urlStr = ToolUtil.url + "/LoginServlet";
        String body = null;
        try {
            if (way == 3) {
                body = "way=" + URLEncoder.encode(String.valueOf(way),"utf-8")
                        + "&userInfo=" + URLEncoder.encode(InfoUtil.userInfoToJSONObject(userInfo).toString(),"utf-8");
            } else {
                body = "userid=" + URLEncoder.encode(userInfo.getUserid(),"utf-8")
                        + "&password=" + URLEncoder.encode(userInfo.getPassword(),"utf-8")
                        + "&way=" + URLEncoder.encode(String.valueOf(way),"utf-8");
            }
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
                    URL url = new URL("http://192.168.214.49:8080/NEDULibrary/LoginServlet");
                    //2.通过URL对象获取一个HttpUrlConnection对象
                    HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
                    //3.设置HttpUrlConnection对象的一些参数 请求方式 连接的超时时间 请求头信息
                    openConnection.setRequestMethod("POST");    //请求方式
                    openConnection.setConnectTimeout(10*1000);  //连接超时
                    //设置请求头
                    //String body = "username=" + username + "&password=" + password;
                    String body = "userid="+ URLEncoder.encode(userInfo.getUserid(),"utf-8")+"&password="+URLEncoder.encode(userInfo.getPassword(),"utf-8");
                    //openConnection.setRequestProperty("Content-Length",String.valueOf(body.length()));
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
