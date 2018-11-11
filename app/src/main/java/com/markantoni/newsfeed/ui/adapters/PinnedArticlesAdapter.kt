package com.markantoni.newsfeed.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.markantoni.newsfeed.R
import com.markantoni.newsfeed.repository.model.Article
import com.markantoni.newsfeed.util.inflate
import com.markantoni.newsfeed.util.loadImage
import kotlinx.android.synthetic.main.view_pinned_article.view.*

class PinnedArticlesAdapter(private val onArticleClicked: (Article, ImageView) -> Unit) :
    ListAdapter<Article, PinnedArticlesAdapter.ViewHolder>(ArticlesDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.view_pinned_article))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article: Article) {
            itemView.apply {
                articleImage.transitionName = "pin${article.id}"
                articleImage.loadImage(article.image)
                setOnClickListener { onArticleClicked(article, articleImage) }
            }
        }
    }
}