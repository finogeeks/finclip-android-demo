package com.finogeeks.mop.demo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.finogeeks.lib.applet.client.FinAppClient
import com.finogeeks.lib.applet.interfaces.FinCallback
import com.finogeeks.lib.applet.sdk.api.request.IFinAppletRequest
import kotlinx.android.synthetic.main.activity_scan_start_applet.*

/**
 * 扫码启动小程序的页面
 */
@SuppressLint("Registered")
open class ScanStartAppletActivity : AppCompatActivity() {

    private var isStartingApplet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_start_applet)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnScan.setOnClickListener { scanQRCode() }
    }

    private fun scanQRCode() {
        startActivityForResult(Intent(this, ScanQRCodeActivity::class.java), REQ_CODE_SCAN_QR_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_SCAN_QR_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                if (!isStartingApplet)
                    FinAppClient.appletApiManager.startApplet(this, IFinAppletRequest.fromQrCode(data.getStringExtra(ScanQRCodeActivity.EXTRA_RESULT)),
                        object : FinCallback<String> {
                            override fun onSuccess(result: String?) {
                                isStartingApplet = false
                                runOnUiThread {
                                    Toast.makeText(this@ScanStartAppletActivity, "扫码成功", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onError(code: Int, error: String?) {
                                isStartingApplet = false
                                runOnUiThread {
                                    Toast.makeText(this@ScanStartAppletActivity, error, Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onProgress(status: Int, info: String?) {
                            }
                        })
                    /*FinAppClient.appletApiManager.startAppletByQrcode(this, data.getStringExtra(ScanQRCodeActivity.EXTRA_RESULT),
                            object : FinCallback<String> {
                                override fun onSuccess(result: String?) {
                                    isStartingApplet = false
                                    runOnUiThread {
                                        Toast.makeText(this@ScanStartAppletActivity, "扫码成功", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onError(code: Int, error: String?) {
                                    isStartingApplet = false
                                    runOnUiThread {
                                        Toast.makeText(this@ScanStartAppletActivity, error, Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onProgress(status: Int, info: String?) {
                                }
                            })*/
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val TAG = "StartAppletActivity"

        private const val REQ_CODE_SCAN_QR_CODE = 0x01
    }
}