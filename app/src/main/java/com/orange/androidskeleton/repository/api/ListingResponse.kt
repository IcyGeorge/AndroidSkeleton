package com.orange.androidskeleton.repository.api

import com.orange.androidskeleton.vo.RedditPost

class ListingResponse(val data: ListingData)

class ListingData(
    val children: List<RedditChildrenResponse>,
    val after: String?,
    val before: String?
)

data class RedditChildrenResponse(val data: RedditPost)