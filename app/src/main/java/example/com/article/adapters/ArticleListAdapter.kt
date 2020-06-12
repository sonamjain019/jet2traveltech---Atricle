package example.com.article.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import example.com.article.R
import example.com.article.databinding.ArticleListItemBinding
import example.com.article.models.Article
import example.com.article.presenters.ArticlesPresenter

open class ArticleListAdapter(
    articleList: List<Article>?,
    clickListener: ArticlesPresenter
) :
    RecyclerView.Adapter<ArticleListAdapter.MyViewHolder>() {
    private var articleList: ArrayList<Article> =
        ArrayList<Article>()
    private val clickListener: ArticlesPresenter
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding: ArticleListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.article_list_item,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    init {
        setHasStableIds(true)
        this.articleList = ArrayList<Article>(articleList)
        this.clickListener = clickListener
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val binding: ArticleListItemBinding = holder.getBinding()
        binding.article = articleList[position]
        var media: Article.Media = Article.Media()
        binding.isMediaAvailable = false
        if (articleList[position].media != null && !articleList[position].media?.isEmpty()!!) {
            media = articleList[position].media!![0]!!
            binding.isMediaAvailable = true
        }
        binding.media = media
        var user: Article.User = Article.User()
        if (articleList[position].user != null && !articleList[position].user?.isEmpty()!!) {
            user = articleList[position].user!![0]!!
        }
        binding.user = user
    }

    override fun getItemCount(): Int {
        return articleList!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setData(results: List<Article>?) {
        articleList =
            if (results == null || results.isEmpty()) ArrayList<Article>() else ArrayList<Article>(
                results
            )
        notifyDataSetChanged()
    }

    inner class MyViewHolder(binding: ArticleListItemBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        private val binding: ArticleListItemBinding
        fun getBinding(): ArticleListItemBinding {
            return binding
        }

        init {
            this.binding = binding
        }
    }
}