package example.com.article.models

import java.io.Serializable

class EmptyResponse : Serializable {
    var status: String? = null
    var code = 0
    var error_type: String? = null
    var message: String? = null
}
