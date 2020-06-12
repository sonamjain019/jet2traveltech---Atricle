package example.com.article.viewmodels

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import example.com.article.MainActivity
import example.com.article.R
import example.com.article.apiservices.ApiServices
import example.com.article.models.Article
import example.com.article.presenters.ArticlesPresenter
import example.com.article.presenters.RestClientResponse
import example.com.article.utilities.CommonFunctions
import java.util.*


class ArticlesViewModel private constructor(
    context: MainActivity,
    private val presenter: ArticlesPresenter
) :
    AndroidViewModel(context.application) {
    var articleList: List<Article>? = null;
    var isPaginationInProgress = false
    var isPaginationEnd = false
    var pageNum = 1

    /*
     * method to get articles list from api
     */
    fun getArticlesList(
        context: Context,
        apiCallIndex: Int,
        isNextApiCall: Boolean
    ) {
        if (CommonFunctions.getInstance()?.isOffline(context)!!) {
            presenter.toastMsg(context.getString(R.string.error_network_unavailable))
            return
        }
        isPaginationInProgress = true
        when (apiCallIndex) {
            -1 -> {
            }
            1 -> if (isNextApiCall) pageNum = pageNum + 1
        }
        val queryMap: MutableMap<String?, Any?> =
            HashMap()
        queryMap["page"] = pageNum
        queryMap["limit"] = 10
        ApiServices().getArticleList(context, queryMap, object : RestClientResponse {
            override fun onSuccess(response: Any?, statusCode: Int) {
                isPaginationInProgress = false
                var article1: MutableList<Article> =
                    response as MutableList<Article>
                if (article1 == null || article1.isEmpty()) {
                    isPaginationEnd = true;
                }
                if (isNextApiCall && articleList != null && !articleList!!
                        .isEmpty()
                ) {
                    val previousArticle: MutableList<Article> =
                        articleList as MutableList<Article>
                    if (article1 != null && !article1
                            .isEmpty()
                    ) {
                        previousArticle.addAll(article1)
                        article1 = previousArticle
                    }else{
                        article1 = previousArticle
                    }
                }
                articleList = article1
                if (article1 != null) {
                    presenter.setAdapter(article1)
                }
            }

            override fun onFailure(errorMessage: String?, statusCode: Int) {
                isPaginationInProgress = false
                presenter.toastMsg(errorMessage)
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
    }

    class ArticleListModelFactory(
        private val context: MainActivity,
        private val presenter: ArticlesPresenter
    ) :
        NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticlesViewModel(context, presenter) as T
        }

    }
}
