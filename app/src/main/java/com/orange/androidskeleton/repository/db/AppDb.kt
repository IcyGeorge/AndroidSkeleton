package com.orange.androidskeleton.repository.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.orange.androidskeleton.vo.RedditPost

/**
 * Main database description.
 */
@Database(
    entities = [
        RedditPost::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun redditPostDao(): RedditPostDao

}
