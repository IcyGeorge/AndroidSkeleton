
package com.orange.androidskeleton.vo

import androidx.room.Entity
import androidx.room.TypeConverters
import com.orange.androidskeleton.repository.db.AppTypeConverters

@Entity(primaryKeys = ["query"])
@TypeConverters(AppTypeConverters::class)
data class RepoSearchResult(
    val query: String,
    val repoIds: List<Int>,
    val totalCount: Int,
    val next: Int?
)
