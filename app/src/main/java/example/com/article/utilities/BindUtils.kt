package example.com.article.utilities

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import example.com.article.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds


object BindUtils {
    /*
        companion object {*/
    /*
 * Load image from url.
 */
    @BindingAdapter("src_avatar")
    @JvmStatic
    fun loadAvatarImageWithGlide(
        imageView: ImageView,
        srcAvatar: String?
    ) {
        if (imageView.context != null) {
            Glide.with(imageView).clear(imageView)
            imageView.setImageDrawable(null)
            if (srcAvatar != null && !srcAvatar.isEmpty()) {
                Glide.with(imageView.context)
                    .load(srcAvatar)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.shape_user_default_avatar_background)
                    .circleCrop()
                    .into(imageView)
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        imageView.context,
                        R.drawable.shape_user_default_avatar_background
                    )
                )
            }
        }
    }

    /*
 * Load image from url.
 */
    @BindingAdapter("src_article")
    @JvmStatic
    fun loadArticleImageWithGlide(
        imageView: ImageView,
        srcArticle: String?
    ) {
        if (imageView.context != null) {
            Glide.with(imageView).clear(imageView)
            imageView.setImageDrawable(null)
            if (srcArticle != null && !srcArticle.isEmpty()) {
                Glide.with(imageView.context)
                    .load(srcArticle)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.shape_default_article_background)
                    .into(imageView)
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        imageView.context,
                        R.drawable.shape_default_article_background
                    )
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    @BindingAdapter("article_time")
    @JvmStatic
    fun setArticleTime(
        textView: TextView,
        articleTime: String?
    ) {
        if (textView.context != null && articleTime != null) {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            try {
                val time = sdf.parse(articleTime).time
                val now = System.currentTimeMillis()
//                val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                val ago =
                    getTimeAgo(now.milliseconds.toLongMilliseconds() - time.milliseconds.toLongMilliseconds())
                textView.text = ago
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    fun getTimeAgo(durationInMilliseconds: Long): String? {
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(durationInMilliseconds)
        val minutes =
            TimeUnit.MILLISECONDS.toMinutes(durationInMilliseconds)
        val hours =
            TimeUnit.MILLISECONDS.toHours(durationInMilliseconds)
        val days =
            TimeUnit.MILLISECONDS.toDays(durationInMilliseconds)
        var s: String
        if (seconds < 60) {
            s = if (seconds == 1L) {
                "Just now"
            } else {
                "$seconds sec"
            }
        } else if (minutes < 60) {
            s = if (minutes == 1L) {
                "$minutes min"
            } else {
                "$minutes mins"
            }
        } else if (hours < 24) {
            s = if (hours == 1L) {
                "$hours hr"
            } else {
                "$hours hrs"
            }
        } else {
            s = if (days == 1L) {
                "$days Day"
            } else {
                if (days >= 365) {
                    val yr = (days / 365).toInt()
                    if (yr == 1) {
                        "$yr Year"
                    } else {
                        "$yr Years"
                    }
                } else if (days >= 30) {
                    val month = (days / 30).toInt()
                    if (month == 1) {
                        "$month Month"
                    } else {
                        "$month Months"
                    }
                } else if (days >= 7) {
                    val week = (days / 7).toInt()
                    if (week == 1) {
                        "$week week ago"
                    } else {
                        "$week weeks ago"
                    }
                } else {
                    "$days Days"
                }
            }
        }
        return s
    }

    @BindingAdapter(value = ["count", "append_text"])
    @JvmStatic
    fun setLikesAndComments(
        textView: TextView,
        count: Long?,
        appendText: String?
    ) {
        if (textView.context != null && count != null) {
            textView.text =
                getFormattedCount(count) + " " + appendText;
        }
    }

    fun getFormattedCount(count: Long): String? {
        val formattedCount: String
        val roundOffValue: Double
        if (count <= 999) {
            formattedCount = count.toString()
        } else if (count <= 999999) {
            roundOffValue = count.toDouble() / 1000
            formattedCount = roundUptoOneDecimalValue(roundOffValue) + "k"
        } else if (count <= 999999999) {
            roundOffValue = count.toDouble() / 1000000
            formattedCount = roundUptoOneDecimalValue(roundOffValue) + "m"
        } else if (count <= 99999999999L) {
            roundOffValue = count.toDouble() / 1000000000
            formattedCount = roundUptoOneDecimalValue(roundOffValue) + "b"
        } else {
            formattedCount = "100b+"
        }
        return formattedCount
    }

    fun roundUptoOneDecimalValue(d: Double): String {
        val count = ((d * 1e1).toLong() / 1e1).toString()
        //Long typecast will remove the decimals
        return if (count.contains(".0")) {
            count.split("\\.").toTypedArray()[0]
        } else count
    }
//    }

}