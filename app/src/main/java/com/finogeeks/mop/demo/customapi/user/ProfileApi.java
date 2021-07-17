package com.finogeeks.mop.demo.customapi.user;

import com.finogeeks.lib.applet.api.AbsApi;
import com.finogeeks.lib.applet.interfaces.ICallback;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileApi extends AbsApi {

    private final static String API_NAME_GET_USER_PROFILE = "getUserProfile"; // 小程序基础库调用的api名称

    @Override
    public String[] apis() {
        return new String[]{API_NAME_GET_USER_PROFILE};
    }

    @Override
    public void invoke(String event, JSONObject param, ICallback callback) {
        if (event.equals(API_NAME_GET_USER_PROFILE)) {
            JSONObject jsonObject = new JSONObject();
            try {
                // 这里返回的是虚拟的用户个人信息，开发者请从APP里面自行获取用户个人信息
                jsonObject.put("nickName", "张三");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callback.onSuccess(jsonObject);
        }
    }

}
