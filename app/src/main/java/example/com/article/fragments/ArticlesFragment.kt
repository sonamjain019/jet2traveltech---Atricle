package example.com.article.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import example.com.article.MainActivity
import example.com.article.R
import example.com.article.adapters.ArticleListAdapter
import example.com.article.databinding.FragmentArticlesBinding
import example.com.article.models.Article
import example.com.article.presenters.ArticlesPresenter
import example.com.article.utilities.CommonFunctions
import example.com.article.viewmodels.ArticlesViewModel

class ArticlesFragment : Fragment(), ArticlesPresenter {
    private lateinit var activity: MainActivity
    private lateinit var binding: FragmentArticlesBinding
    private var adapter: ArticleListAdapter? = null
    private var viewModel: ArticlesViewModel? = null
    private var isFromBackStack = false
    private lateinit var layoutManager: LinearLayoutManager
    private var mDialog: AlertDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_articles, container, false)
        activity = getActivity() as MainActivity
        if (viewModel == null) {
            viewModel = ViewModelProvider(
                this,
                ArticlesViewModel.ArticleListModelFactory(activity, this)
            ).get(
                ArticlesViewModel::class.java
            )
        } else {
            isFromBackStack = true
        }
        binding.setViewModel(viewModel)
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recycleViewArticle.setLayoutManager(layoutManager)
        if (adapter != null) {
            binding.recycleViewArticle.setAdapter(adapter)
            binding.recycleViewArticle.addOnScrollListener(onGridListScrollListener)
        }
        if (!isFromBackStack) {
            viewModel!!.getArticlesList(activity, 0, false)
        }
        return binding.getRoot()
    }

    /*
     * listener to listen list scrolling to call next page api
     */
    private val onGridListScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    try {
                        val lastVisibleItemPosition =
                            layoutManager.findLastVisibleItemPosition()
                        //                    int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
                        if (viewModel!!.articleList != null && viewModel!!.articleList != null && !viewModel!!.articleList
                                ?.isEmpty()!!
                            && adapter != null && !viewModel!!.isPaginationInProgress
                        ) {
                            if ((viewModel!!.articleList
                                    ?.size
                                    ?: 0) - lastVisibleItemPosition <= 1 && !viewModel!!.isPaginationEnd
                            ) {
                                viewModel!!.getArticlesList(activity, 1, true)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
            }
        }

    /*
     * set adapter to article list
     */
    override fun setAdapter(articles: List<Article>?) {
        if (articles != null && !articles
                .isEmpty()
        ) {
            if (adapter == null) {
                adapter = ArticleListAdapter(articles, this)
                binding.recycleViewArticle.setAdapter(adapter)
                binding.recycleViewArticle.addOnScrollListener(onGridListScrollListener)
            } else {
                adapter!!.setData(articles)
            }
        } else {
            adapter?.setData(null)
        }
    }

    override fun toastMsg(msg: String?) {
        CommonFunctions.getInstance()!!.showToastMessage(activity, msg, false)
    }

    private fun dismissAlertDialog() {
        try {
            if (mDialog != null) {
                mDialog!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mDialog = null
    }

    override fun hideSoftKeyBoard() {
        CommonFunctions.getInstance()!!.hideSoftKeyBoard(activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissAlertDialog()
        CommonFunctions.getInstance()!!.hideSoftKeyBoard(activity)
    }

    companion object {
        const val fragmentName = "ArticleListFragment"
        fun newInstance(): ArticlesFragment {
            return ArticlesFragment();
        }
    }
}