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
 * 自定义小程序API
 * 跳转到原生APP中输入内容的页面，获取到输入内容后将数据回传给小程序
 */
public class CustomApi extends AbsApi {

    private static final int REQ_CODE_INPUT = 0x01;

    private static final String API_NAME_ON_NATIVE = "onNative";

    @NonNull
    private Context mContext;

    public CustomApi(@NonNull Context context) {
        mContext = context;
    }

    /**
     * @return 支持可调用的api名称的数组
     */
    @Override
    public String[] apis() {
        return new String[]{API_NAME_ON_NATIVE};
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
        if (API_NAME_ON_NATIVE.equals(event)) {
            Intent intent = new Intent(mContext, InputContentActivity.class);
            callback.startActivityForResult(intent, REQ_CODE_INPUT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, ICallback callback) {
        super.onActivityResult(requestCode, resultCode, data, callback);
        if (requestCode == REQ_CODE_INPUT) {
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