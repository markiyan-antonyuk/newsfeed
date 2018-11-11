package com.markantoni.newsfeed.repository.db

import androidx.room.*
import com.markantoni.newsfeed.repository.model.Article

@Entity(tableName = "article")
data class ArticleModel(
    @PrimaryKey val id: String,
    @ColumnInfo val title: String,
    @ColumnInfo val category: String,
    @ColumnInfo val image: String,
    @ColumnInfo val description: String?,
    @ColumnInfo val timestamp: Long
)

@Dao
interface ArticlesDao {
    @Query("SELECT * FROM article")
    fun selectAllArticles(): List<ArticleModel>

    @Query("SELECT * FROM article WHERE id = (:id) ")
    fun findArticle(id: String): ArticleModel?

    @Insert
    fun saveArticle(article: ArticleModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateArticle(article: ArticleModel)
}

fun ArticleModel.toArticle() = Article(id, title, category, image, description, timestamp, isSaved = true)
fun Article.toArticleModel() = ArticleModel(id, title, category, image, description, timestamp)