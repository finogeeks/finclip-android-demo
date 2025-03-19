package com.finogeeks.testdemo.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
//import com.finogeeks.mop.share.FinShareWXEntry
//import com.finogeeks.mop.wechat.FinWeChatWXEntry

class WXEntryActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        FinWeChatWXEntry.handleWeChatIntent(this,intent)
//        FinShareWXEntry.handleWeChatIntent(this, intent)
        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
//        FinWeChatWXEntry.handleWeChatIntent(this,intent)
//        FinShareWXEntry.handleWeChatIntent(this, intent)
        finish()
    }
}