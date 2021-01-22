package com.finogeeks.mop.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.finogeeks.lib.applet.page.view.moremenu.MoreMenuItem;
import com.finogeeks.lib.applet.page.view.moremenu.MoreMenuType;
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

    /**
     * 获取灰度发布配置参数
     *
     * @param appId 小程序ID
     * @return 灰度发布配置参数
     */
    @Nullable
    @Override
    public List<GrayAppletVersionConfig> getGrayAppletVersionConfigs(@NotNull String appId) {
        return null;
    }

    /**
     * 获取注册的"更多"菜单项
     *
     * @param appId 小程序ID
     * @return 注册的"更多"菜单项
     */
    @Nullable
    @Override
    public List<MoreMenuItem> getRegisteredMoreMenuItems(@NotNull String appId) {
        List<MoreMenuItem> items = new ArrayList<>();
        MoreMenuItem item0 = new MoreMenuItem("WXShareAPPFriends", "微信好朋友", MoreMenuType.ON_MINI_PROGRAM);
        items.add(item0);
        MoreMenuItem item1 = new MoreMenuItem("WXShareAPPMoments", "微信朋友圈", MoreMenuType.ON_MINI_PROGRAM, true);
        items.add(item1);
        MoreMenuItem item2 = new MoreMenuItem("ShareSinaWeibo", "新浪微博", MoreMenuType.ON_MINI_PROGRAM);
        items.add(item2);
        MoreMenuItem item3 = new MoreMenuItem("ShareQQFirends", "QQ", MoreMenuType.ON_MINI_PROGRAM);
        items.add(item3);
        MoreMenuItem item4 = new MoreMenuItem("ShareDingDing", "Dingding", MoreMenuType.ON_MINI_PROGRAM);
        items.add(item4);
        MoreMenuItem item5 = new MoreMenuItem("ShareLinks", "标题以后端配置为准", MoreMenuType.ON_MINI_PROGRAM);
        items.add(item5);
        MoreMenuItem item6 = new MoreMenuItem("SharePicture", "SharePicture", MoreMenuType.ON_MINI_PROGRAM);
        items.add(item6);
        MoreMenuItem item7 = new MoreMenuItem("Restart", "Restart", MoreMenuType.COMMON);
        items.add(item7);
        MoreMenuItem item8 = new MoreMenuItem("Desktop", "Desktop", MoreMenuType.COMMON);
        items.add(item8);
        return items;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息[Map]
     */
    @Nullable
    @Override
    public Map<String, String> getUserInfo() {
        return null;
    }

    /**
     * 小程序导航栏中的"关闭"按钮被点击
     *
     * @param appId 小程序ID
     */
    @Override
    public void onNavigationBarCloseButtonClicked(@NotNull String appId) {
        Toast.makeText(mContext, "点击了小程序 " + appId + " 的导航栏关闭按钮", Toast.LENGTH_SHORT).show();
    }

    /**
     * 注册的"更多"菜单项被点击
     *
     * @param appId      小程序ID
     * @param path       小程序页面路径
     * @param menuItemId 被点击的菜单条目的ID
     * @param appInfo    小程序信息，是一串json，包含了小程序id、小程序名称、小程序图标、用户id、转发的数据内容等信息。
     *                   [appInfo]的内容格式如下：
     *                   {
     *                   "appTitle": "凡泰小程序",
     *                   "appAvatar": "https:\/\/www.finogeeks.club\/statics\/images\/swan_mini\/swan_logo.png",
     *                   "appId": "5df36b3f687c5c00013e9fd1",
     *                   "userId": "finogeeks",
     *                   "params": {
     *                   "title": "apt-test-tweet-接口测试发布的动态！@#￥%……&*（",
     *                   "desc": "您身边的服务专家",
     *                   "imageUrl": "finfile:\/\/tmp_fc15edd8-2ff6-4c54-9ee9-fe5ee034033d1576550313667.png",
     *                   "path": "pages\/tweet\/tweet-detail.html?fcid=%40staff_staff1%3A000000.finogeeks.com&timelineId=db0c2098-031e-41c4-b9c6-87a5bbcf681d&shareId=3dfa2f78-19fc-42fc-b3a9-4779a6dac654",
     *                   "appInfo": {
     *                   "weixin": {
     *                   "path": "\/studio\/pages\/tweet\/tweet-detail",
     *                   "query": {
     *                   "fcid": "@staff_staff1:000000.finogeeks.com",
     *                   "timelineId": "db0c2098-031e-41c4-b9c6-87a5bbcf681d"
     *                   }
     *                   }
     *                   }
     *                   }
     *                   }
     * @param bitmap     小程序封面图片。如果[appInfo].params.imageUrl字段为http、https的链接地址，那么小程序封面图片
     *                   就取[appInfo].params.imageUrl对应的图片，否则小程序的封面图片取[bitmap]。
     * @param callback   转发小程序结果回调。
     */
    @Override
    public void onRegisteredMoreMenuItemClicked(@NotNull String appId, @NotNull String path, @NotNull String menuItemId, @Nullable String appInfo, @Nullable Bitmap bitmap, @NotNull IAppletCallback callback) {
        Toast.makeText(mContext, "小程序" + appId + "的" + path + "页面的菜单" + menuItemId + "被点击了，appInfo : " + appInfo + " bitmap : " + bitmap, Toast.LENGTH_SHORT).show();
        callback.onSuccess(null);
    }

    /**
     * 转发小程序
     *
     * @param appInfo  小程序信息，是一串json，包含了小程序id、小程序名称、小程序图标、用户id、转发的数据内容等信息。
     *                 [appInfo]的内容格式如下：
     *                 {
     *                 "appTitle": "凡泰小程序",
     *                 "appAvatar": "https:\/\/www.finogeeks.club\/statics\/images\/swan_mini\/swan_logo.png",
     *                 "appId": "5df36b3f687c5c00013e9fd1",
     *                 "userId": "finogeeks",
     *                 "params": {
     *                 "title": "apt-test-tweet-接口测试发布的动态！@#￥%……&*（",
     *                 "desc": "您身边的服务专家",
     *                 "imageUrl": "finfile:\/\/tmp_fc15edd8-2ff6-4c54-9ee9-fe5ee034033d1576550313667.png",
     *                 "path": "pages\/tweet\/tweet-detail.html?fcid=%40staff_staff1%3A000000.finogeeks.com&timelineId=db0c2098-031e-41c4-b9c6-87a5bbcf681d&shareId=3dfa2f78-19fc-42fc-b3a9-4779a6dac654",
     *                 "appInfo": {
     *                 "weixin": {
     *                 "path": "\/studio\/pages\/tweet\/tweet-detail",
     *                 "query": {
     *                 "fcid": "@staff_staff1:000000.finogeeks.com",
     *                 "timelineId": "db0c2098-031e-41c4-b9c6-87a5bbcf681d"
     *                 }
     *                 }
     *                 }
     *                 }
     *                 }
     * @param bitmap   小程序封面图片。如果[appInfo].params.imageUrl字段为http、https的链接地址，那么小程序封面图片
     *                 就取[appInfo].params.imageUrl对应的图片，否则小程序的封面图片取[bitmap]。
     * @param callback 转发小程序结果回调。
     */
    @Override
    public void shareAppMessage(@NotNull String appInfo, @Nullable Bitmap bitmap, @NotNull IAppletCallback callback) {
        Toast.makeText(mContext, "点击了转发按钮，去实现您的转发/分享逻辑吧", Toast.LENGTH_SHORT).show();
    }
}