package com.orange.androidskeleton.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.orange.androidskeleton.vo.Contributor
import com.orange.androidskeleton.vo.User
import io.reactivex.Flowable

/**
 * Interface for database access for User related operations.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: List<User>)

    @Query("SELECT * FROM user")
    fun loadUsers(): Flowable<List<User>>

}
