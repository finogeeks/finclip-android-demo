package com.finogeeks.mop.demo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.finogeeks.lib.applet.modules.permission.checkPermissions
import kotlinx.android.synthetic.main.activity_scan_qr_code.*

/**
 * 扫描二维码页面
 */
class ScanQRCodeActivity : AppCompatActivity(), QRCodeView.Delegate {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr_code)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        checkPermissions(Manifest.permission.CAMERA, granted = {
            zBarView.visibility = View.VISIBLE
            zBarView.setDelegate(this)
            zBarView.startCamera()
            zBarView.startSpotAndShowRect()
        })
    }

    override fun onStop() {
        zBarView.stopCamera()
        super.onStop()
    }

    override fun onDestroy() {
        zBarView.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        zBarView.showScanRect()
    }

    override fun onScanQRCodeSuccess(result: String?) {
        vibrate()
        zBarView.startSpot()
        setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_RESULT, result))
        finish()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        var tipText: String = zBarView.scanBoxView.tipText
        val ambientBrightnessTip = "\n环境过暗，请打开闪光灯"
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                zBarView.scanBoxView.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                zBarView.scanBoxView.tipText = tipText
            }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "打开相机出错", Toast.LENGTH_SHORT).show()
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            @Suppress("DEPRECATION")
            vibrator.vibrate(200)
        } else {
            try {
                val effect = VibrationEffect.createOneShot(
                    200,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
                vibrator.vibrate(effect, null)
            } catch (iae: IllegalArgumentException) {
                Log.e(TAG, "Failed to create VibrationEffect", iae)
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

        private const val TAG = "ScanQRCodeActivity"

        const val EXTRA_RESULT = "result"
    }
}