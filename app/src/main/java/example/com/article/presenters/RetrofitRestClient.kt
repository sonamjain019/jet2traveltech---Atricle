package example.com.article.presenters

import example.com.article.models.Article
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface RetrofitRestClient {
    @GET("/jet2/api/v1/blogs")
    fun getArticleList(@QueryMap options: MutableMap<String?, Any?>?): Call<List<Article>>
}