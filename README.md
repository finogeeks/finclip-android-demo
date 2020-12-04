# 五行代码让APP运行小程序

## 1、在工程的build.gradle中需要配置的内容

在工程的`build.gradle`中添加maven仓库的地址：

```groovy
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
## 2、在gradle中依赖SDK

```groovy
implementation 'com.finogeeks.lib:finapplet:2.12.39'
```

## 3、 配置混淆规则

集成SDK之后，为了避免SDK中部分不能被混淆的代码被混淆，需要在工程的混淆规则配置文件中增加以下配置：

```properties
-keep class com.finogeeks.** {*;}
```
## 4、SDK初始化

我们强烈建议在`Application`中对SDK进行初始化，初始化SDK需要传入的各项参数如下：
```java
FinAppConfig config = new FinAppConfig.Builder()
        .setSdkKey("22LyZEib0gLTQdU3MUauATBwgfnTCJjdr7FCnywmAEM=") // 需替换为自己的SDK Key
	    .setSdkSecret("bdfd76cae24d4313") // 需替换为自己的SDK Secret
        .setApiUrl("https://mp.finogeeks.com") // 需替换为对应后端服务的地址
        .setApiPrefix("/api/v1/mop/") // 需替换为对应后端服务的接口请求路由前缀
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

SDK采用多进程机制实现，每个小程序运行在独立的进程中，即一个小程序对应一个进程，在初始化SDK时，要特别注意的一点是：小程序进程在创建的时候不需要执行任何初始化操作，即使是小程序SDK的初始化，也不需要在小程序进程中执行。例如：应用使用了一些第三方库，这些库需要在应用启动时先初始化，那么在`Application`中执行初始化时，只有当前进程为宿主进程时才需要初始化这些第三方库，小程序进程是不需要初始化这些库的。

因此，在初始化SDK之前，一定要判断当前进程是哪一个进程，如果是小程序进程，就不进行任何操作了：

```java
if (FinAppClient.INSTANCE.isFinAppProcess(this)) {
    return;
}
```

* **SDK Key** 和 **SDK Secret** 可以从免费一键部署的社区版的管理后台获取。
* **apiUrl** 为小程平台后端服务的地址。
* **apiPrefix** 为小程平台后端服务的接口请求路由前缀。
* **小程序id** 为在管理后台上架的小程序appid。
* 上述的参数可以在前文服务器部署的后台界面上获取，亦可以在没有部署服务端的情况下在[https://mp.finogeeks.com](https://mp.finogeeks.com)快速注册，免费获取。
* 具体的操作方法请参考 [Android集成](https://mp.finogeeks.com/mop/document/runtime-sdk/sdk-integrate/android.html)
