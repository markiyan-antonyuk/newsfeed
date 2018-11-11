package com.markantoni.newsfeed.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.markantoni.newsfeed.R
import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.repository.model.isEmpty
import com.markantoni.newsfeed.util.inflate
import com.markantoni.newsfeed.util.loadImage
import kotlinx.android.synthetic.main.view_home_article.view.*

class ArticlesAdapter(private val onArticleClicked: (Article, ImageView) -> Unit) :
    PagedListAdapter<Article, ArticlesAdapter.ViewHolder>(ArticlesDiffUtilCallback()) {

    var isFlattened = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.view_home_article))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article: Article?) {
            itemView.apply {
                articleImage.loadImage(article?.image)
                articleImage.transitionName = "home${article?.id}"
                articleTitle.text = article?.title
                articleTitle.isVisible = !isFlattened
                articleCategory.text = article?.category
                setOnClickListener { article?.let { if (!it.isEmpty()) onArticleClicked(it, articleImage) } }
            }
        }
    }
}