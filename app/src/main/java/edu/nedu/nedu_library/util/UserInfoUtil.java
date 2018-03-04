package edu.nedu.nedu_library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONObject;

import edu.nedu.nedu_library.entity.UserInfo;

/**
 * Created by 小呓的欧尼酱 on 2017/1/26.
 */

public class UserInfoUtil {

    public static boolean saveUserInfo(Context context, JSONObject userjson){
        boolean flag = false;
        try {
            //1.通过Context对象创建一个SharedPreference对象
            //name:sharedpreference文件的名称    mode:文件的操作模式
            SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo.txt", Context.MODE_PRIVATE);
            //2.通过sharedPreferences对象获取一个Editor对象
            Editor editor = sharedPreferences.edit();
            //3.往Editor中添加数据
            editor.putString("userjson", userjson.toString());
            editor.putBoolean("isLogin",true);
            //4.提交Editor对象
            editor.commit();
            flag = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    public static void deleteUserInfo(Context context){
        try {
            //1.通过Context对象创建一个SharedPreference对象
            //name:sharedpreference文件的名称    mode:文件的操作模式
            SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo.txt", Context.MODE_PRIVATE);
            //2.通过sharedPreferences对象获取一个Editor对象
            Editor editor = sharedPreferences.edit();
            //3.删除Editor中数据
            editor.remove("userjson");
            editor.remove("isLogin");
            //4.提交Editor对象
            editor.commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static UserInfo getUserInfo(Context context){
        UserInfo userInfo = null;
        try{
            //1.通过Context对象创建一个SharedPreference对象
            SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo.txt", Context.MODE_PRIVATE);
            //key:存放数据时的key   defValue: 默认值,根据业务需求来写
            String userjsonstring = sharedPreferences.getString("userjson","");
            JSONObject userjson = new JSONObject(userjsonstring);
            userInfo = new UserInfo(userjson);
        } catch (Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }

    public static int getUserId(Context context){
        int id=-1;
        UserInfo userInfo = null;
        try{
            //1.通过Context对象创建一个SharedPreference对象
            SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo.txt", Context.MODE_PRIVATE);
            //key:存放数据时的key   defValue: 默认值,根据业务需求来写
            String userjsonstring = sharedPreferences.getString("userjson","");
            JSONObject userjson = new JSONObject(userjsonstring);
            userInfo = new UserInfo(userjson);
            id = userInfo.getId();
        } catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

    public static boolean isLogin(Context context){
        boolean flag = false;
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo.txt", Context.MODE_PRIVATE);
            flag = sharedPreferences.getBoolean("isLogin", false);
        } catch (Exception e){
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

}
