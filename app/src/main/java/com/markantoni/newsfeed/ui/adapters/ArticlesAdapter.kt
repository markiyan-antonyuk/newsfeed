package com.markantoni.newsfeed.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.markantoni.newsfeed.R
import com.markantoni.newsfeed.extensions.inflate
import com.markantoni.newsfeed.extensions.loadImage
import com.markantoni.newsfeed.repository.model.Article
import kotlinx.android.synthetic.main.row_home_article.view.*

class ArticlesAdapter : ListAdapter<Article, ArticlesAdapter.ViewHolder>(ArticlesDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.row_home_article))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article: Article) {
            itemView.apply {
                articleImage.loadImage(article.image)
                articleTitle.text = article.title
                articleCategory.text = article.category
            }
        }
    }
}

private class ArticlesDiffUtilCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem
}