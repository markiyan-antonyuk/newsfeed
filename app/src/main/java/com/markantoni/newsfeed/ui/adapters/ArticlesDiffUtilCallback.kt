package com.markantoni.newsfeed.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.markantoni.newsfeed.repository.model.Article


class ArticlesDiffUtilCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem
}