package example.com.article.models

import java.io.Serializable

class Article : Serializable {
    var id: String? = null
    var createdAt: String? = null
    var content: String? = null
    var comments: Long? = null
    var likes: Long? = null
    var media: List<Media?>? = null
    var user: List<User?>? = null

    class Media : Serializable {
        var id: String? = null
        var blogId: String? = null
        var createdAt: String? = null
        var image: String? = null
        var title: String? = null
        var url: String? = null
    }

    class User : Serializable {
        var id: String? = null
        var blogId: String? = null
        var createdAt: String? = null
        var name: String? = null
        var avatar: String? = null
        var lastname: String? = null
        var city: String? = null
        var designation: String? = null
        var about: String? = null
    }
}
