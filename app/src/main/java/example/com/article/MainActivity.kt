package example.com.article

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import example.com.article.fragments.ArticlesFragment
import example.com.article.utilities.CommonFunctions

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showHomeFragment()
    }

    fun showHomeFragment() {
        supportFragmentManager.executePendingTransactions()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ArticlesFragment.newInstance())
            .addToBackStack(ArticlesFragment.fragmentName)
            .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        CommonFunctions.getInstance()!!.hideSoftKeyBoard(this@MainActivity)
        val count = supportFragmentManager.backStackEntryCount
        if (count == 1) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}
