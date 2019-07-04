package com.orange.androidskeleton.ui.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.orange.androidskeleton.AppExecutors
import com.orange.androidskeleton.R
import com.orange.androidskeleton.base.BaseListAdapter
import com.orange.androidskeleton.databinding.ItemPostBinding
import com.orange.androidskeleton.vo.RedditPost

/**
 * A RecyclerView adapter for [RedditPost] class.
 */
class PostsAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val userClickCallback: ((RedditPost) -> Unit)?
) : BaseListAdapter<RedditPost, ItemPostBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<RedditPost>() {
        override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return oldItem.name == newItem.name
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ItemPostBinding {
        val binding = DataBindingUtil.inflate<ItemPostBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_post,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.post?.let {
                userClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: ItemPostBinding, item: RedditPost) {
        binding.post = item
    }
}
