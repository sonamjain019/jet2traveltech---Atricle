package example.com.article.utilities

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

class CommonFunctions {

    companion object {

        private var instance: CommonFunctions? = null

        fun getInstance(): CommonFunctions? {
            if (instance == null) {
                instance =
                    CommonFunctions()
            }
            return instance
        }
    }

    /*
     * method used to check network.
     */
    fun isOffline(context: Context?): Boolean {
        try {
            if (context != null) {
                val connectivityManager = context.applicationContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var networkInfo: NetworkInfo? = null
                if (connectivityManager != null) {
                    networkInfo = connectivityManager.activeNetworkInfo
                }
                return networkInfo == null || !networkInfo.isConnected
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return true
    }

    /*
     * method used to hide SoftKeyBoard.
     */
    fun hideSoftKeyBoard(activity: Activity?) {
        try {
            if (activity == null) {
                return
            }
            hideSoftKeyBoard(activity, activity.currentFocus)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /*
     * method used to hide SoftKeyBoard.
     */
    fun hideSoftKeyBoard(activity: Activity?, view: View?) {
        try {
            if (activity == null) {
                return
            }
            activity.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            )
            if (view != null) {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /*
     * method to show toast message
     */
    fun showToastMessage(
        context: Context?,
        message: String?,
        isLongMessage: Boolean
    ) {
        var toastLength = Toast.LENGTH_SHORT
        if (isLongMessage) {
            toastLength = Toast.LENGTH_LONG
        }
        val toast = Toast.makeText(context, message, toastLength)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}