package com.finogeeks.testdemo

//import com.finclip.webview.XWalkPreferences
//import com.finogeeks.mop.plugins.maps.map.MapSDKInitializer
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import androidx.annotation.Nullable
import com.finogeeks.lib.applet.api.BaseApi
import com.finogeeks.lib.applet.api.SyncApi
import com.finogeeks.lib.applet.client.FinAppClient
import com.finogeeks.lib.applet.client.FinAppConfig
import com.finogeeks.lib.applet.client.FinAppProcessClient
import com.finogeeks.lib.applet.client.FinStoreConfig
import com.finogeeks.lib.applet.interfaces.FinCallback
import com.finogeeks.lib.applet.interfaces.IApi
import com.finogeeks.lib.applet.interfaces.ICallback
import com.finogeeks.xlog.XLogLevel
import org.json.JSONObject


class CustomSyncApi(context: Context?) : SyncApi(context) {
    override fun apis(): Array<String> {
        return arrayOf("b222")
    }

    @Nullable
    override fun invoke(event: String, param: JSONObject): String? {
        // {"errMsg": "customApi:ok" , "data": "1"}  // 返回的数据格式是固定的，必须是个json格式且包含"errMsg": "customApi:ok" 这一段
        if (event == "b222") {
            if (param.has("a")) {
                val a = param.getInt("a")
                if (a == 0) {
                    return getSuccessRes(event).put("data", "0").toString()
                }
            }
            return getFailureRes(event, "should fail");
        }
        return getSuccessRes(event).put("data", "1").toString() // 可以借住sdk提供的api完成对数据的组装
        // return getFailureRes(event,"token miss").toString()
    }
}


class CustomApi(context: Context) : BaseApi(context) {

    override fun apis(): Array<String> {
        return arrayOf(
            "mock_login",
            "gameSave",
            "login",
            "adIsReady",
            "getGameSave",
            "showAd",
            "isLogin"
        ) // api名称!
    }

    @SuppressLint("HardwareIds")
    override fun invoke(event: String, param: JSONObject, callback: ICallback) {
        val result = JSONObject()

        if (event == "mock_login") {
            if (param.has("a")) {
                val a = param.getInt("a")
                if (a == 0) {
                    callback.onSuccess(result)
                    return
                }
            }
            callback.onFail()
            return
        } else if (event == "getAppVersionInfo") {
            if (result.length() > 0) {
                callback.onSuccess(result)
                return
            }
            callback.onFail()
        } else if (event == "isLogin") {
            result.put(
                "isLogin", Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            )
        } else {
            callback.onSuccess(result)
        }
    }
}

class FinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val start = SystemClock.uptimeMillis()
//        if (FinAppClient.isFinAppProcess(applicationContext)) {
        FinAppProcessClient.callback = object : FinAppProcessClient.Callback {
            override fun getRegisterExtensionApis(activity: Activity): List<IApi>? {
                // 在小程序进程中注册小程序扩展API
                val apis: MutableList<IApi> = ArrayList()
                val customApi: IApi = CustomSyncApi(activity)
                apis.add(customApi)
                return apis
            }

            override fun getRegisterExtensionWebApis(activity: Activity): List<IApi>? {
                // 在小程序进程中注册小程序网页调原生API
                val apis: MutableList<IApi> = ArrayList()
//                    val customH5Api: IApi = CustomH5Api(activity)
//                    apis.add(customH5Api)
                return apis

            }
        }
//        }
        initFinClipSDK()
    }

    private fun initFinClipSDK() {
        //XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
//        WebView.setWebContentsDebuggingEnabled(true)
//        WebView.setViewType(0);
        if (FinAppClient.isFinAppProcess(this)) {
            return
        }

        val config = FinAppConfig.Builder()
            .setFinStoreConfigs(
                listOf(
                    FinStoreConfig(
                        "",
                        "",
                        "",
                        "",
                        "/api/v1/mop/",
                        "",
                        FinAppConfig.ENCRYPTION_TYPE_SM
                    )
                )
            )
            .setLogLevel(XLogLevel.LEVEL_VERBOSE)
            .setEnableLog(true)
            .setDebugMode(true)
            .setDisableTbs(true)
            .setAppletDebugMode(FinAppConfig.AppletDebugMode.appletDebugModeDisable)
            .build()
        val self = this
        FinAppClient.init(this, config, object : FinCallback<Any?> {
            override fun onSuccess(result: Any?) {
                FinAppClient.extensionApiManager.registerApi(CustomApi(self))

            }

            override fun onProgress(status: Int, info: String?) {
                Log.e("FinApplication", "onProgress $status $info")
            }

            override fun onError(code: Int, error: String?) {
            }
        })

    }
}