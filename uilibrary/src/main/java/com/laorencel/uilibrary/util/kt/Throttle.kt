import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * 节流函数
 */
inline fun <T : View> T.throttleClick(
    interval: Long = 800,
    tip: String? = null,
    crossinline block: (T) -> Unit
) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > interval) {
            lastClickTime = currentTimeMillis
            block(this)
        } else {
            tip?.let { Snackbar.make(this, tip, Snackbar.LENGTH_LONG).show() }
        }
    }
}

//兼容点击事件设置为this的情况
fun <T : View> T.throttleClick(
    onClickListener: View.OnClickListener,
    interval: Long = 800,
    tip: String? = null,
) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > interval) {
            lastClickTime = currentTimeMillis
            onClickListener.onClick(this)
        } else {
            tip?.let { Snackbar.make(this, tip, Snackbar.LENGTH_LONG).show() }
        }
    }
}

var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0