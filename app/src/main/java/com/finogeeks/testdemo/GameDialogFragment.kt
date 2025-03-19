import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.* // Import all view-related classes
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.finogeeks.lib.applet.sdk.component.ComponentHolder
import com.finogeeks.testdemo.R
import org.json.JSONObject

class GameDialogFragment : DialogFragment() {

    private var gameView: View? = null
    private var gameComponentHolder: ComponentHolder? = null // Replace 'Any' with actual type
    private lateinit var config: JSONObject
    private var cancenCallback: (() -> Unit)? = null

    companion object {
        private const val TAG = "GameDialogFragment"
        private const val CONFIG_KEY = "config"

        fun newInstance(config: JSONObject): GameDialogFragment {
            val args = Bundle()
            args.putString(CONFIG_KEY, config.toString())
            val fragment = GameDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)  // Optional: Prevent dismiss on outside touch

        return dialog
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        config = JSONObject(arguments?.getString(CONFIG_KEY)!!) // !! is generally unsafe, handle nulls properly if config might be missing

        return inflater.inflate(R.layout.game_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val closeButton = view.findViewById<Button>(R.id.close_popup_button)
        val gameContainer = view.findViewById<LinearLayout>(R.id.game_container)

        closeButton.setOnClickListener { dismiss() }

        Log.d(TAG, "onGameViewCreated $config")
        gameContainer.addView(gameView)
        gameView?.visibility = View.VISIBLE


    }

    override fun onStart() {
        super.onStart()
//        val height = (resources.displayMetrics.heightPixels * 0.50)
//        dialog?.window?.setLayout(
//            (height / 16 * 9).toInt(),
//            (height.toInt())
//        )
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.50).toInt(),
            (resources.displayMetrics.heightPixels * 0.50).toInt()
        )
        dialog?.window?.setGravity(Gravity.CENTER)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        gameComponentHolder?.destroy()
        gameComponentHolder = null
        cancenCallback?.let { it() }
    }



    fun setGameView(view: View) {
        this.gameView = view
    }

    fun setGameComponentHolder(holder: ComponentHolder?) { // Replace 'Any' with actual type
        this.gameComponentHolder = holder
    }

    fun setCancelCallback(function: () -> Unit) {
        cancenCallback = function
    }
}