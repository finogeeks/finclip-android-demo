# 安卓工程 readme

<a name="ulvpb"></a>
## 🤩 FinClip是什么?
有没有想过，能把一个已经开发好的微信小程序，放到你自己的APP里面运行？<br />想象一下，你只需要开发一次，就能把这个业务模块同时放到微信、自有App<br />甚至！你都不用开发业务，直接拖下来一个做好的业务模块，放到自己App里面，嘿！跑起来了！<br />听起来是不是有点不可思议？<br />没关系，这就是FinClip，帮助你实现这个不可思议！<br />

<a name="y9LBK"></a>
## 🤔 你要怎么做？<br />
只需要三个步骤：

1. get一个小程序！你可以：<br />自己开发一个微信小程序<br />or 在我们的 [小程序生态圈](https://mp.finogeeks.com/#/ecosystem) 中挑一个小程序（支持直接下载代码包）<br />or 直接使用我们提供的项目：[https://github.com/finogeeks/miniprogram-demo](https://github.com/finogeeks/miniprogram-demo)）
1. 把finclip SDK集成到你的APP里面
1. 登录[FinClip小程序开放平台](https://finclip.com/#/home)，完成关联

然后，见证奇迹，看看这个微信小程序直接在你的App里面运行起来的效果吧！<br />

<a name="TaX3b"></a>
## 🔜 五行代码让你的App运行小程序
<a name="imw25"></a>
### 1、配置 build.gradle
在工程的`build.gradle`中添加maven仓库的地址：
```
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
<a name="62q3i"></a>
### 2、在gradle中依赖SDK
```
implementation 'com.finogeeks.lib:finapplet:+'
```
<a name="zeaRN"></a>
### 3、 配置混淆规则
集成SDK之后，为了避免SDK中部分不能被混淆的代码被混淆，需要在工程的混淆规则配置文件中增加以下配置：
```
-keep class com.finogeeks.** {*;}
```
<a name="C5AUZ"></a>
### 4、SDK初始化
我们强烈建议在`Application`中对SDK进行初始化，初始化SDK需要传入的各项参数如下：<br />❌ 不在application中初始化SDK也可以，但是一定要保证不在小程序进程中初始化小程序运行时SDK ❌
```
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
SDK采用多进程机制实现，每个小程序运行在独立的进程中，即一个小程序对应一个进程，在初始化SDK时，要特别注意的一点是：

- 小程序进程在创建的时候不需要执行任何初始化操作，即使是小程序SDK的初始化，也不需要在小程序进程中执行

**例如：**<br />应用使用了一些第三方库，这些库需要在应用启动时先初始化，那么在`Application`中执行初始化时，只有当前进程为宿主进程时才需要初始化这些第三方库，小程序进程是不需要初始化这些库的。<br />**因此**：在初始化SDK之前，一定要判断当前进程是哪一个进程，如果是小程序进程，就不进行任何操作了：
```
if (FinAppClient.INSTANCE.isFinAppProcess(this)) {
    return;
}
```
<a name="N63X1"></a>
### 5、打开小程序
```
FinAppClient.INSTANCE.getAppletApiManager().startApplet(this, "appid");
```
❌ 不要修改包名、key、secret、AppID ❌<br />
<br />但如果你需要把这些内容更改为自己的信息，你可以：

- **SDKKEY** 和 **Secret** 可以从[ FinClip开放平台](https://finclip.com/#/home) 获取，你也可以直接点击进入[注册页面](https://finclip.com/#/register)
- 进入平台后，在【应用管理】页面添加你自己的包名后，点击【复制】即可获得key\secret\apisever字段<br />
- **ApiUrl** 和 **apiPrefix **是固定字段，请直接参考本demo
- **小程序id** 为在管理后台上架的小程序AppID，需要在【小程序管理】中创建并在【应用管理】中关联 <br />（与微信小程序ID不一样哦！这里是特指finclip平台的ID）



<a name="GZU3P"></a>
## 📚 想要通关全程？这里是全程攻略
直接跑demo虽然快，不过快速通关总会留下各种遗憾。<br />来吧，跟随全程攻略，了解一下“让App运行小程序”的全貌吧：
<a name="Ri882"></a>
### 1、FinClip 平台是什么？

- Finclip平台是凡泰极客旗下的一款可私有化的小程序开放平台<br />
- 凡泰极客借鉴微信、支付宝等主流小程序平台技术，进一步打造出可私有化的小程序开放平台产品 —— FinClip，该平台主要由两个客户端组成，一个是运营端，负责审核小程序内容，确保小程序的内容符合合规要求；另一个是企业端，负责开发小程序及小程序上下架管理。
- FinClip 面向全行业发布，尤其适合金融业及其他需要自建数字化生态以及实现业务场景敏捷迭代的行业，帮助合作伙伴构建一个安全、合规、可控的小程序生态。
<a name="sx7EX"></a>
### 2、FinClip 平台的特色？

- 多端上线：同一小程序可以同步上线多个宿主端（即小程序可上线的 APP），为开发者节省大量的人力和时间。
- 合规引流：解决“行业应用嵌入第三方网络空间”的安全合规问题，合规引流，连接金融服务场景。
- 方便快捷：相较于 APP，小程序开发周期短，开发成本低等特性让更多的开发者能够轻松、快速的参与到开发过程中，实现快速上线，快速起量。
- 优质体验：小程序拥有优于现有 H5 页面的用户体验，帮助企业/机构获取更多渠道用户，同时节省获客成本。
- 部署方式：满足合规监管多种部署方式，支持私有化部署、混合部署、行业云部署。
<a name="l5pz3"></a>
### 3、FinClip 有哪些典型案例？
（如需了解更多案例，可以与小助手联系呀）

![图片.png](https://github.com/finogeeks/finclip-android-demo/blob/master/yippi.jpeg)
<a name="2VK7o"></a>

## ⛳️ 获得更多指引
✅ 部署一套私有化社区版：[https://www.finclip.com/mop/document/quickstart/Community-Edition.html](https://www.finclip.com/mop/document/quickstart/Community-Edition.html)<br />✅ 了解安卓相关API：[https://www.finclip.com/mop/document/runtime-sdk/sdk-api/android.html](https://www.finclip.com/mop/document/runtime-sdk/sdk-api/android.html)<br />✅ 了解更多安卓常见问题：[https://www.finclip.com/mop/document/faq/SDK/Android-SDK.html](https://www.finclip.com/mop/document/faq/SDK/Android-SDK.html)<br />

<a name="9K1zU"></a>
## ☎️ 与我们联系
如想进入FinClip小程序技术群交流探讨，或了解更多使用场景，请添加小助手微信。<br />![](https://github.com/finogeeks/finclip-android-demo/blob/master/demo_readme2.png)
