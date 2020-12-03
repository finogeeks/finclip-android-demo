package com.finogeeks.mop.demo.customapi;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.finogeeks.lib.applet.api.AbsApi;
import com.finogeeks.lib.applet.interfaces.ICallback;
import com.finogeeks.mop.demo.InputContentActivity;
import com.finogeeks.mop.demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;
import static com.finogeeks.mop.demo.InputContentActivity.EXTRA_NAME_INPUT_CONTENT;

/**
 * 自定义H5 API
 * 跳转到原生APP的输入内容页面{@link InputContentActivity}，输入内容提交后，把输入的内容回传给小程序中的网页
 */
public class CustomH5Api extends AbsApi {

    private static final int REQ_CODE_INPUT_CONTENT = 0x02;

    private static final String API_NAME_USER_DEFINE_NATIVE = "user_define_native";

    @NonNull
    private Context mContext;

    public CustomH5Api(@NonNull Context context) {
        mContext = context;
    }

    /**
     * @return 支持可调用的api名称的数组
     */
    @Override
    public String[] apis() {
        return new String[]{API_NAME_USER_DEFINE_NATIVE};
    }

    /**
     * 接收到对应的api调用时，会调用此方法，在此方法中处理api调用的功能逻辑
     *
     * @param event    事件名称，即api名称
     * @param param    参数
     * @param callback 回调接口
     */
    @Override
    public void invoke(String event, JSONObject param, ICallback callback) {
        if (API_NAME_USER_DEFINE_NATIVE.equals(event)) {
            Intent intent = new Intent(mContext, InputContentActivity.class);
            callback.startActivityForResult(intent, REQ_CODE_INPUT_CONTENT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, ICallback callback) {
        super.onActivityResult(requestCode, resultCode, data, callback);
        if (requestCode == REQ_CODE_INPUT_CONTENT) {
            if (resultCode == RESULT_OK && data != null) {
                String inputContent = data.getStringExtra(EXTRA_NAME_INPUT_CONTENT);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("text", inputContent);
                    callback.onSuccess(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFail();
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("errMsg", mContext.getString(R.string.fin_clip_get_input_content_failed));
                    callback.onFail(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFail();
                }
            }
        }
    }
}