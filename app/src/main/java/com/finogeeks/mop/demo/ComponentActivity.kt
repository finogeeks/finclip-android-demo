package com.finogeeks.mop.demo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.finogeeks.lib.applet.client.FinAppClient
import com.finogeeks.lib.applet.sdk.api.request.IFinAppletRequest
import com.finogeeks.lib.applet.sdk.component.ComponentCallback
import com.finogeeks.lib.applet.sdk.component.ComponentHolder
import kotlinx.android.synthetic.main.activity_component.componentContainer
import kotlinx.android.synthetic.main.activity_component.errMsgTv
import kotlinx.android.synthetic.main.activity_component.errorView
import kotlinx.android.synthetic.main.activity_component.loadingView
import kotlinx.android.synthetic.main.activity_component.removeComponentBtn

class ComponentActivity : AppCompatActivity() {

    private var componentHolder: ComponentHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 如果不是要求在Activity关闭之前移除小组件，则不需要手动移除小组件。
        removeComponentBtn.visibility = View.GONE
        removeComponentBtn.setOnClickListener {
            destroyComponent()
        }

        startComponent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 打开小组件
     */
    private fun startComponent() {
        // 打开小组件前先展示Loading
        showLoading()

        // 构造request对象
        val request = IFinAppletRequest.Companion.fromAppId("65e82e0778df2c00015c21a4")
        // 可以通过request设置其他启动参数
        // ...

        FinAppClient.appletApiManager.startComponent(
            this,
            request,
            object : ComponentCallback {
                override fun onSuccess(result: String?) {
                    // 小组件加载成功
                }

                override fun onError(code: Int, error: String?) {
                    // 小组件加载失败，隐藏Loading，显示错误信息。
                    hideLoading()
                    showError(code, error.orEmpty())
                }

                override fun onComponentCreated(componentHolder: ComponentHolder) {
                    // 小组件创建成功, 将小组件视图添加到宿主Activity视图内
                    componentContainer.addView(
                        componentHolder.view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    this@ComponentActivity.componentHolder = componentHolder
                }

                override fun onContentLoaded() {
                    // 小组件内容加载完成，隐藏Loading
                    hideLoading()
                }
            }
        )
    }

    /**
     * 手动触发小组件销毁。
     * 如果需要在宿主Activity finish之前移除小组件。需要调用此方法触发小组件的销毁。
     * 如果小组件跟随宿主Activity销毁。则不需要调用此方法。
     */
    private fun destroyComponent() {
        hideError()
        hideLoading()
        componentHolder?.destroy()
        componentHolder = null
    }

    private fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingView.visibility = View.GONE
    }

    private fun showError(code: Int, message: String) {
        errMsgTv.text = "$message($code)"
        errorView.visibility = View.VISIBLE
    }

    private fun hideError() {
        errorView.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        componentHolder?.onActivityResult(requestCode, resultCode, data)
    }

}