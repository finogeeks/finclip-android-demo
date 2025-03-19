package com.finogeeks.testdemo

import GameDialogFragment
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.finogeeks.lib.applet.client.FinAppClient
import com.finogeeks.lib.applet.modules.barcode.zxing.integration.android.IntentIntegrator
import com.finogeeks.lib.applet.modules.permission.askForPermissions
import com.finogeeks.lib.applet.sdk.api.request.IFinAppletRequest
import com.finogeeks.lib.applet.sdk.api.request.RemoteFinAppletRequest
import com.finogeeks.lib.applet.sdk.component.ComponentHolder
import com.finogeeks.lib.applet.sdk.component.GameCallback
import com.finogeeks.testdemo.R
import kotlinx.android.synthetic.main.activity_main.appBtn
import kotlinx.android.synthetic.main.activity_main.appid
import kotlinx.android.synthetic.main.activity_main.checkbox
import kotlinx.android.synthetic.main.activity_main.dialog_checkbox
import kotlinx.android.synthetic.main.activity_main.game_type
import kotlinx.android.synthetic.main.activity_main.qrcodeBtn
import org.json.JSONObject


class MainActivity : FragmentActivity() {
    private var hasGameRunning = false
    companion object {
        private const val REQ_CODE_SCAN_QR_CODE = 0x01

        private const val TAG = "MainActivity"
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }
    fun removeBtn(btn: Button){
        val layout = btn.parent as ViewGroup?
        layout?.removeView(btn)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {
        val mainActicty = this
        appBtn.setOnClickListener {
            startAppletById(appid.text.toString(), mainActicty)
        }
        qrcodeBtn.setOnClickListener {
            askForPermissions(Manifest.permission.CAMERA)
                .onGranted {
                    val intent = IntentIntegrator(this)
                        // 仅识别QR_CODE，防止因识别的类型太多导致容易识别到错误信息
                        .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                        .createScanIntent()
                    startActivityForResult(intent, 0x01)
                }
                .go()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_SCAN_QR_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val result = data.getStringExtra("result")
                result?.let { openApp(it) }
            }
        }
    }
    private fun openApp(resultData: String) {
        val request = IFinAppletRequest.fromQrCode(resultData)
        .apply {
            setSingleProcess(checkbox.isChecked)
        }
        FinAppClient.appletApiManager.startApplet(this, request)
    }
    private fun startAppletById(
        appid: String,
        mainActicty: MainActivity
    ) {
        val request = IFinAppletRequest.fromAppId(appid).apply {
            setSingleProcess(checkbox.isChecked)
        }
        if (game_type.isChecked) {
            request.setGameType(IFinAppletRequest.GameType.Native)
        }
        if (dialog_checkbox.isChecked) {
            request.setProcessMode(IFinAppletRequest.ProcessMode.SINGLE)
            request.setTaskMode(IFinAppletRequest.TaskMode.MULTI)
            startDialog(request, mainActicty)
        } else {
            FinAppClient.appletApiManager.startApplet(this, request)
        }
    }

    @Synchronized private fun startDialog(
        request: RemoteFinAppletRequest,
        mainActicty: MainActivity
    ) {
        FinAppClient.appletApiManager.startComponent(
            this,
            request,
            object : GameCallback {
                var gameComponentHolder: ComponentHolder? = null
                override fun onSuccess(result: String?) {
                    Log.e(TAG, "goldBtn 2")
                }

                override fun onError(code: Int, error: String?) {
                    Log.e(TAG, "goldBtn $code $error")
                }

                override fun onComponentCreated(componentHolder: ComponentHolder) {
                    gameComponentHolder = componentHolder
                }

                override fun onContentLoaded() {
                    Log.e(TAG, "goldBtn 4")
                }

                override fun onGameViewCreated(view: View, config: JSONObject) {
                    val config = JSONObject() // Your config
                    val dialogFragment = GameDialogFragment.newInstance(config)
                    dialogFragment.setGameView(view) // The View from onGameViewCreated
                    dialogFragment.setGameComponentHolder(gameComponentHolder) // Your component holder
                    dialogFragment.setCancelCallback { hasGameRunning = false }
                    dialogFragment.show(supportFragmentManager, "game_dialog")
                    return
                }

                override fun onGameRunning() {
                    Log.d(TAG, "dialog game onGameRunning")
                }

                override fun onGameDrawFrame() {
                    Log.d(TAG, "dialog game onGameDrawFrame")
                }

                override fun onGameDestroy() {
                    Log.d(TAG, "dialog game onGameDestroy")
                }

                override fun onGameDownloadProgress(totalLen: Int, writtenLen: Int) {
                    Log.d(TAG, "dialog game onGameStartProgress total $totalLen written $writtenLen")
                }

                override fun onGameDownloadSuccess() {
                    Log.d(TAG, "dialog game onGameDownloadSuccess")
                }

                override fun onGameDownloadError(status: Int, info: String?) {
                    Log.d(TAG, "dialog game onGameDownloadError $status $info")
                }
            }
        )
    }

}