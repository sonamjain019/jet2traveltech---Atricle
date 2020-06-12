package example.com.article.apiservices

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import example.com.article.R
import example.com.article.models.Article
import example.com.article.models.EmptyResponse
import example.com.article.presenters.RestClientResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.QueryMap
import java.net.ConnectException
import java.net.SocketTimeoutException

class ApiServices {
    private val INTERNAL_SERVER_ERROR_CODE = 999

    fun getArticleList(
        context: Context,
        @QueryMap options: MutableMap<String?, Any?>,
        restClientResponse: RestClientResponse
    ) {
        val call: Call<List<Article>>? = ApiServiceUtil.instance?.getRestClient(context)
            ?.getArticleList(options)
        call?.enqueue(object : Callback<List<Article>?> {
            override fun onResponse(
                call: Call<List<Article>?>,
                response: Response<List<Article>?>
            ) {
                if (response.isSuccessful()) {
                    restClientResponse.onSuccess(response.body(), response.code())
                } else {
                    if (response.errorBody() != null) {
                        try {
                            //TypeAdapter<LoginResponseDTO> adapter = new Gson().getAdapter(LoginResponseDTO.class);
                            val emptyResponse: EmptyResponse = Gson().getAdapter<EmptyResponse>(
                                EmptyResponse::class.java
                            )
                                .fromJson(response.errorBody()!!.string())
                            restClientResponse.onSuccess(emptyResponse, response.code())
                        } catch (e: Exception) {
                            restClientResponse.onFailure(
                                context.getString(R.string.error_server_connect_error),
                                response.code()
                            )
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Article>?>, t: Throwable) {
                try {
                    if (restClientResponse != null) {
                        if (t is SocketTimeoutException || t is ConnectException) {
                            restClientResponse.onFailure(
                                context.getString(R.string.error_server_connect_error),
                                INTERNAL_SERVER_ERROR_CODE
                            )
                            // CommonFunctions.getInstance().checkNetworkStrength(context, handler);
                        } else if (t is JsonSyntaxException) {
                            restClientResponse.onFailure(
                                context.getString(R.string.error_invalid_server_data),
                                INTERNAL_SERVER_ERROR_CODE
                            )
                            //CommonFunctions.getInstance().checkNetworkStrength(context, handler);
                        } else {
                            restClientResponse.onFailure(
                                context.getString(R.string.error_internal_server_error)
                                    .toString() + " " + t.message, INTERNAL_SERVER_ERROR_CODE
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }
}