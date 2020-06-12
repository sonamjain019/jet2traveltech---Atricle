package example.com.article.presenters

import example.com.article.models.Article


interface ArticlesPresenter {

    fun toastMsg(msg: String?)

    fun hideSoftKeyBoard()

    fun setAdapter(articles: List<Article>?)
}