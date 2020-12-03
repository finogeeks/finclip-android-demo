package com.finogeeks.mop.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.finogeeks.lib.applet.page.view.moremenu.MoreMenuItem;
import com.finogeeks.lib.applet.rest.model.GrayAppletVersionConfig;
import com.finogeeks.lib.applet.sdk.api.IAppletHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link IAppletHandler}实现类，用于实现一些业务场景，例如注册"更多"菜单项，转发小程序等。
 */
public class AppletHandler implements IAppletHandler {

    @NonNull
    private Context mContext;

    private AppletHandler() {
    }

    public AppletHandler(@NonNull Context context) {
        this.mContext = context;
    }

    @Nullable
    @Override
    public List<GrayAppletVersionConfig> getGrayAppletVersionConfigs(@NotNull String appId) {
        return null;
    }

    @Nullable
    @Override
    public List<MoreMenuItem> getRegisteredMoreMenuItems(@NotNull String appId) {
        List<MoreMenuItem> moreMenuItems = new ArrayList<>();
        moreMenuItems.add(new MoreMenuItem(0x01, android.R.drawable.star_big_on, "菜单一", true));
        moreMenuItems.add(new MoreMenuItem(0x02, android.R.drawable.star_big_off, "菜单二", true));
        moreMenuItems.add(new MoreMenuItem(0x03, android.R.drawable.star_big_on, "菜单三菜单三菜单三菜单三菜单三", true));
        moreMenuItems.add(new MoreMenuItem(0x04, android.R.drawable.star_big_off, "菜单四", true));
        moreMenuItems.add(new MoreMenuItem(0x05, android.R.drawable.star_big_on, "菜单五", true));
        moreMenuItems.add(new MoreMenuItem(0x06, android.R.drawable.star_big_off, "菜单六", true));
        moreMenuItems.add(new MoreMenuItem(0x07, android.R.drawable.star_big_on, "菜单七", true));
        moreMenuItems.add(new MoreMenuItem(0x08, android.R.drawable.star_big_off, "菜单八", true));
        moreMenuItems.add(new MoreMenuItem(0x09, android.R.drawable.star_big_on, "菜单九", true));
        return moreMenuItems;
    }

    @Nullable
    @Override
    public Map<String, String> getUserInfo() {
        return null;
    }

    @Override
    public void onNavigationBarCloseButtonClicked(@NotNull String appId) {
        Toast.makeText(mContext, "点击了小程序 " + appId + " 的导航栏关闭按钮", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisteredMoreMenuItemClicked(@NotNull String appId, int menuItemId) {
        Toast.makeText(mContext, "点击了小程序 " + appId + " 注入的菜单 ：" + menuItemId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void shareAppMessage(@NotNull String appInfo, @Nullable Bitmap bitmap, @NotNull IAppletCallback iAppletCallback) {
        Toast.makeText(mContext, "点击了转发按钮，去实现您的转发/分享逻辑吧", Toast.LENGTH_SHORT).show();
    }
}