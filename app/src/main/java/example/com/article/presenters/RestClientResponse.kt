package example.com.article.presenters

interface RestClientResponse {
    fun onSuccess(response: Any?, statusCode: Int)
    fun onFailure(errorMessage: String?, statusCode: Int)
}