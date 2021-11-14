<p align="center">
    <a href="https://www.finclip.com?from=github">
    <img width="auto" src="https://www.finclip.com/mop/document/images/logo.png">
    </a>
</p>

<p align="center"> 
    <strong>FinClip Android DEMO</strong></br>
<p>
<p align="center"> 
        本项目提供在 Android 环境中运行小程序的 DEMO 样例
<p>

<p align="center"> 
	👉 <a href="https://www.finclip.com?from=github">https://www.finclip.com/</a> 👈
</p>

<div align="center">

<a href="#"><img src="https://img.shields.io/badge/%E4%B8%93%E5%B1%9E%E5%BC%80%E5%8F%91%E8%80%85-20000%2B-brightgreen"></a>
<a href="#"><img src="https://img.shields.io/badge/%E5%B7%B2%E4%B8%8A%E6%9E%B6%E5%B0%8F%E7%A8%8B%E5%BA%8F-6000%2B-blue"></a>
<a href="#"><img src="https://img.shields.io/badge/%E5%B7%B2%E9%9B%86%E6%88%90%E5%B0%8F%E7%A8%8B%E5%BA%8F%E5%BA%94%E7%94%A8-75%2B-yellow"></a>
<a href="#"><img src="https://img.shields.io/badge/%E5%AE%9E%E9%99%85%E8%A6%86%E7%9B%96%E7%94%A8%E6%88%B7-2500%20%E4%B8%87%2B-orange"></a>

<a href="https://www.zhihu.com/org/finchat"><img src="https://img.shields.io/badge/FinClip--lightgrey?logo=zhihu&style=social"></a>
<a href="https://www.finclip.com/blog/"><img src="https://img.shields.io/badge/FinClip%20Blog--lightgrey?logo=ghost&style=social"></a>



</div>

<p align="center">

<div align="center">

[官方网站](https://www.finclip.com/) | [示例小程序](https://www.finclip.com/#/market) | [开发文档](https://www.finclip.com/mop/document/) | [部署指南](https://www.finclip.com/mop/document/introduce/quickStart/cloud-server-deployment-guide.html) | [SDK 集成指南](https://www.finclip.com/mop/document/introduce/quickStart/intergration-guide.html) | [API 列表](https://www.finclip.com/mop/document/develop/api/overview.html) | [组件列表](https://www.finclip.com/mop/document/develop/component/overview.html) | [隐私承诺](https://www.finclip.com/mop/document/operate/safety.html)

</div>

-----
## 🤔 FinClip 是什么?

有没有**想过**，开发好的微信小程序能放在自己的 APP 里直接运行，只需要开发一次小程序，就能在不同的应用中打开它，是不是很不可思议？

有没有**试过**，在自己的 APP 中引入一个 SDK ，应用中不仅可以打开小程序，还能自定义小程序接口，修改小程序样式，是不是觉得更不可思议？

这就是 FinClip ，就是有这么多不可思议！

## ⚙️ 操作步骤
### 第一步 配置 build.gradle 文件
在工程的 `build.gradle` 中添加 maven 仓库的地址：
```bash
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:3.5.2"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.60"
    }
}
allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }
        maven {
            url "https://gradle.finogeeks.club/repository/applet/"
            credentials {
                username "applet"
                password "123321"
            }
        }
    }
}
```
### 第二步 在 gradle 中依赖 SDK
`implementation 'com.finogeeks.lib:finapplet:+'`

### 第三步 配置混淆规则
集成 SDK 之后，为了避免 SDK 中部分不能被混淆的代码被混淆，需要在工程的混淆规则配置文件中增加以下配置：

``-keep class com.finogeeks.** {*;}``

### 第四步 SDK初始化
我们强烈建议在 `Application` 中对SDK进行初始化，初始化 SDK 需要传入的各项参数如下：
```java
FinAppConfig config = new FinAppConfig.Builder()
        .setAppKey("SDKKEY")
	      .setAppSecret("SECRET")
        .setApiUrl("https://api.finclip.com")
        .setApiPrefix("/api/v1/mop/")
        .setGlideWithJWT(false)
        .build();
FinCallback<Object> callback = new FinCallback<Object>() {
    @Override
    public void onSuccess(Object result) {
        // SDK初始化成功
    }

    @Override
    public void onError(int code, String error) {
        // SDK初始化失败
        Toast.makeText(AppletApplication.this, "SDK初始化失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgress(int status, String error) {

    }
};
FinAppClient.INSTANCE.init(this, config, callback);
```
 SDK 采用多进程机制实现，每个小程序运行在独立的进程中，即一个小程序对应一个进程。在初始化SDK时，要特别注意的一点是：**小程序进程在创建的时候,不需要执行任何初始化操作，即使是小程序SDK的初始化，也不需要在小程序进程中执行。**

> 举个例子🌰<br>
    应用使用了一些第三方库，这些库需要在应用启动时先初始化，那么在 Application 中执行初始化时，只有当前进程为宿主进程时才需要初始化这些第三方库，小程序进程是不需要初始化这些库的。<br>
    因此，在初始化SDK之前，一定要判断当前进程是哪一个进程，如果是小程序进程，就不进行任何操作了：

```java
if (FinAppClient.INSTANCE.isFinAppProcess(this)) {
    return;
}
```

### 第五步 打开小程序
```java
FinAppClient.INSTANCE.getAppletApiManager().startApplet(this, "appid");
```


- **SDK KEY** 和 **SDK SECRET** 可以从 [FinClip](https://finclip.com/#/home)  获取，点 [这里](https://finclip.com/#/register) 注册账号；
- 进入平台后，在「应用管理」页面添加你自己的包名后，点击「复制」即可获得  key\secret\apisever 字段；
- **apiServer** 和 **apiPrefix** 是固定字段，请直接参考本 DEMO ；
- **小程序 ID** 是管理后台上架的小程序 APP ID，需要在「小程序管理」中创建并在「应用管理」中关联；
> 小程序 ID 与 微信小程序ID 不一样哦！（这里是特指 FinClip 平台的 ID ）


## 📋 集成文档
[点击这里](https://www.finclip.com/mop/document/introduce/quickStart/intergration-guide.html#_2-android-%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90) 查看 Android 快速集成文档

## 🔗 常用链接
以下内容是您在 FinClip 进行开发与体验时，常见的问题与指引信息

- [FinClip 官网](https://www.finclip.com/#/home)
- [示例小程序](https://www.finclip.com/#/market)
- [文档中心](https://www.finclip.com/mop/document/)
- [SDK 部署指南](https://www.finclip.com/mop/document/introduce/quickStart/intergration-guide.html)
- [小程序代码结构](https://www.finclip.com/mop/document/develop/guide/structure.html)
- [iOS 集成指引](https://www.finclip.com/mop/document/runtime-sdk/ios/ios-integrate.html)
- [Android 集成指引](https://www.finclip.com/mop/document/runtime-sdk/android/android-integrate.html)
- [Flutter 集成指引](https://www.finclip.com/mop/document/runtime-sdk/flutter/flutter-integrate.html)

## ☎️ 联系我们
微信扫描下面二维码，关注官方公众号 **「凡泰极客」**，获取更多精彩内容。<br>
<img width="150px" src="https://www.finclip.com/mop/document/images/ic_qr.svg">

微信扫描下面二维码，邀请进官方微信交流群（加好友备注：finclip 咨询），获取更多精彩内容。<br>
<img width="150px" src="https://finclip-homeweb-1251849568.cos.ap-guangzhou.myqcloud.com/images/ldy111.jpg">

## Stargazers
[![Stargazers repo roster for @finogeeks/finclip-android-demo](https://reporoster.com/stars/finogeeks/finclip-android-demo)](https://github.com/finogeeks/finclip-android-demo/stargazers)

## Forkers
[![Forkers repo roster for @finogeeks/finclip-android-demo](https://reporoster.com/forks/finogeeks/finclip-android-demo)](https://github.com/finogeeks/finclip-android-demo/network/members)
