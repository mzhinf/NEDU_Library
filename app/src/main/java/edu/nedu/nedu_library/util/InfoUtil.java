package edu.nedu.nedu_library.util;

import org.json.JSONException;
import org.json.JSONObject;

import edu.nedu.nedu_library.entity.UserInfo;

/**
 * Created by 小呓的欧尼酱 on 2017/3/18.
 */

public class InfoUtil {

    public static JSONObject userInfoToJSONObject(UserInfo userInfo){
        JSONObject userjson = new JSONObject();
        try {
            userjson.put("id", userInfo.getId());
            userjson.put("userid", userInfo.getUserid());
            userjson.put("password", userInfo.getPassword());
            userjson.put("name", userInfo.getName());
            userjson.put("sex", String.valueOf(userInfo.getSex()));
            userjson.put("phone", userInfo.getPhone());
            userjson.put("email", userInfo.getEmail());
            userjson.put("department", userInfo.getDepartment());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userjson;
    }
}
