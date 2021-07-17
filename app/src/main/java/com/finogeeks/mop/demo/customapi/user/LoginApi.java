package com.finogeeks.mop.demo.customapi.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.finogeeks.lib.applet.api.AbsApi;
import com.finogeeks.lib.applet.client.FinAppProcessClient;
import com.finogeeks.lib.applet.interfaces.FinCallback;
import com.finogeeks.lib.applet.interfaces.ICallback;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginApi extends AbsApi {

    public final static String API_NAME_LOGIN = "login"; // 小程序基础库调用的api名称
    private final Activity activity;

    public LoginApi(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String[] apis() {
        return new String[]{API_NAME_LOGIN};
    }

    @Override
    public void invoke(String event, JSONObject param, ICallback callback) {
        if (event.equals(API_NAME_LOGIN)) {
            showAuthDialog(callback);
        }
    }

    /**
     * 显示获取用户登录信息的授权提示对话框
     */
    private void showAuthDialog(final ICallback callback) {
        // 是否需要显示授权提示对话框请开发者按照产品需求自行处理
        new AlertDialog.Builder(activity)
                .setTitle("是否同意授权获取用户登录信息？")
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onFail();
                    }
                })
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginMainProcess(callback);
                    }
                })
                .show();
    }

    /**
     * 从主进程获取用户登录信息
     */
    private void loginMainProcess(final ICallback callback) {
        // 小程序进程调用主进程，在主进程获取用户信息后返回给小程序进程
        FinAppProcessClient.INSTANCE.getAppletProcessApiManager()
                .callInMainProcess(API_NAME_LOGIN, null, new FinCallback<String>() {
                    @Override
                    public void onSuccess(final String result) {
                        // 需要在主线程调用callback方法
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    callback.onSuccess(new JSONObject(result));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    callback.onFail();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(int code, String error) {
                        callback.onFail();
                    }

                    @Override
                    public void onProgress(int status, String info) {
                    }
                });
    }

}
