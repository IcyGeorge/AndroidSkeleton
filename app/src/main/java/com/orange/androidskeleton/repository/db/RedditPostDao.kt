package com.orange.androidskeleton.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.orange.androidskeleton.vo.RedditPost
import io.reactivex.Flowable

/**
 * Interface for database access for User related operations.
 */
@Dao
interface RedditPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<RedditPost>)

    @Query("SELECT * FROM posts WHERE subreddit = :subreddit")
    fun loadPosts(subreddit: String): Flowable<List<RedditPost>>

}
