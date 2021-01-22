package com.finogeeks.mop.demo;

import android.graphics.Color;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.finogeeks.lib.applet.client.FinAppClient;
import com.finogeeks.lib.applet.client.FinAppConfig;
import com.finogeeks.lib.applet.interfaces.FinCallback;
import com.finogeeks.mop.demo.customapi.CustomApi;
import com.finogeeks.mop.demo.customapi.CustomH5Api;

public class MopApplication extends MultiDexApplication {

    private static final String TAG = "SampleApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        if (FinAppClient.INSTANCE.isFinAppProcess(this)) {
            // 小程序进程不执行任何初始化操作
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