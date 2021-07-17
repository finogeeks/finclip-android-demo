package com.finogeeks.mop.demo;

import android.app.Activity;
import android.graphics.Color;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.finogeeks.lib.applet.client.FinAppClient;
import com.finogeeks.lib.applet.client.FinAppConfig;
import com.finogeeks.lib.applet.client.FinAppProcessClient;
import com.finogeeks.lib.applet.interfaces.FinCallback;
import com.finogeeks.lib.applet.interfaces.IApi;
import com.finogeeks.lib.applet.sdk.api.IAppletApiManager;
import com.finogeeks.mop.demo.customapi.CustomApi;
import com.finogeeks.mop.demo.customapi.CustomH5Api;
import com.finogeeks.mop.demo.customapi.user.LoginApi;
import com.finogeeks.mop.demo.customapi.user.ProfileApi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MopApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (FinAppClient.INSTANCE.isFinAppProcess(this)) {
            // 小程序进程
            // 小程序进程中注册api的方法能获取到小程序所在activity对象，可以用做创建对话框的context参数）
            FinAppProcessClient.INSTANCE.setCallback(new FinAppProcessClient.Callback() {
                @Override
                public List<IApi> getRegisterExtensionApis(@NotNull Activity activity) {
                    ArrayList<IApi> apis = new ArrayList<>();
                    apis.add(new LoginApi(activity));
                    apis.add(new ProfileApi());
                    return apis;
                }

                @Nullable
                @Override
                public List<IApi> getRegisterExtensionWebApis(@NotNull Activity activity) {
                    return null;
                }
            });
            return;
        }

        FinAppConfig.UIConfig uiConfig = new FinAppConfig.UIConfig();
        uiConfig.setHideNavigationBarCloseButton(true);
        uiConfig.setHideBackHome(true);
        uiConfig.setHideForwardMenu(true);
        uiConfig.setHideFeedbackAndComplaints(true);
        uiConfig.setMoreMenuStyle(FinAppConfig.UIConfig.MORE_MENU_NORMAL);

        FinAppConfig.UIConfig.CapsuleConfig capsuleConfig = new FinAppConfig.UIConfig.CapsuleConfig();
        capsuleConfig.capsuleWidth = 86f;
        capsuleConfig.capsuleHeight = 31f;
        capsuleConfig.capsuleRightMargin = 15f;
        capsuleConfig.capsuleCornerRadius = 15.5f;
        capsuleConfig.capsuleBorderWidth = 0.5f;
        capsuleConfig.capsuleBgLightColor = Color.BLACK;
        capsuleConfig.capsuleBgDarkColor = Color.WHITE;
        capsuleConfig.capsuleBorderLightColor = Color.parseColor("#88ffffff");
        capsuleConfig.capsuleBorderDarkColor = Color.parseColor("#a5a9b4");

        capsuleConfig.moreLightImage = R.mipmap.more_light;
        capsuleConfig.moreDarkImage = R.mipmap.more_dark;
        capsuleConfig.moreBtnWidth = 25f;
        capsuleConfig.moreBtnLeftMargin = 11f;

        capsuleConfig.closeLightImage = R.mipmap.close_light;
        capsuleConfig.closeDarkImage = R.mipmap.close_dark;
        capsuleConfig.closeBtnWidth = 25f;
        capsuleConfig.closeBtnLeftMargin = 9f;

        capsuleConfig.capsuleDividerLightColor = Color.parseColor("#88ffffff");
        capsuleConfig.capsuleDividerDarkColor = Color.parseColor("#a5a9b4");

        uiConfig.setCapsuleConfig(capsuleConfig);

        FinAppConfig config = new FinAppConfig.Builder()
                .setSdkKey(BuildConfig.APP_KEY)
                .setSdkSecret(BuildConfig.APP_SECRET)
                .setApiUrl(BuildConfig.API_URL)
                .setApiPrefix(BuildConfig.API_PREFIX)
                .setDebugMode(BuildConfig.DEBUG)
                .setUiConfig(uiConfig)
                .setEncryptionType(FinAppConfig.ENCRYPTION_TYPE_SM)
                .build();

        FinAppClient.INSTANCE.init(this, config, new FinCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                Toast.makeText(MopApplication.this, "SDK初始化成功", Toast.LENGTH_SHORT).show();
                // 注册自定义小程序API
                FinAppClient.INSTANCE.getExtensionApiManager().registerApi(new CustomApi(MopApplication.this));
                // 注册自定义H5 API
                FinAppClient.INSTANCE.getExtensionWebApiManager().registerApi(new CustomH5Api(MopApplication.this));
                // 设置IAppletHandler实现类
                FinAppClient.INSTANCE.setAppletHandler(new AppletHandler(getApplicationContext()));

                // 在主进程设置"小程序进程调用主进程"的处理方法
                // 开发者也可以选择在主进程其他合适的代码位置设置处理方法
                FinAppClient.INSTANCE.getAppletApiManager()
                        .setAppletProcessCallHandler(new IAppletApiManager.AppletProcessCallHandler() {
                            @Override
                            public void onAppletProcessCall(@NotNull String name,
                                                            @Nullable String params,
                                                            @Nullable FinCallback<String> callback) {
                                if (callback != null) {
                                    if (name.equals(LoginApi.API_NAME_LOGIN)) {
                                        // 从主进程获取登录信息，返回给小程序进程
                                        // 这里返回的是虚拟的用户登录信息，开发者请从APP里面自行获取用户登录信息
                                        JSONObject jsonObject = new JSONObject();
                                        try {
                                            jsonObject.put("userId", "123");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        callback.onSuccess(jsonObject.toString());
                                    }
                                }
                            }
                        });
            }

            @Override
            public void onError(int code, String error) {
                Toast.makeText(MopApplication.this, "SDK初始化失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int status, String error) {

            }
        });
    }
}