package example.com.article.apiservices

import android.content.Context
import example.com.article.BuildConfig
import example.com.article.presenters.RetrofitRestClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiServiceUtil private constructor() {
    private var sRestClient: RetrofitRestClient? = null

    fun getRestClient(context: Context): RetrofitRestClient? {
        if (sRestClient == null) {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            sRestClient = retrofit.create<RetrofitRestClient>(RetrofitRestClient::class.java)
        }
        return sRestClient
    }

    //isFormData? "multipart/form-data":"application/json")
    private val okHttpClient: OkHttpClient
        private get() {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Accept", "*/*")
                    .header(
                        "Content-Type",
                        "application/json"
                    ) //isFormData? "multipart/form-data":"application/json")

                    .method(original.method(), original.body())
                    .build()
                chain.proceed(request)
            }
            val interceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                interceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            httpClient.addInterceptor(interceptor)
            return httpClient
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .build()
        }


    companion object {
        var instance: ApiServiceUtil? = null
            get() {
                if (field == null) {
                    field = ApiServiceUtil()
                }
                return field
            }
            private set
    }
}