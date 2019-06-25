package com.orange.androidskeleton.ui.user

import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.orange.androidskeleton.AppExecutors
import com.orange.androidskeleton.R
import com.orange.androidskeleton.base.BaseListAdapter
import com.orange.androidskeleton.databinding.ItemUserBinding
import com.orange.androidskeleton.vo.User

/**
 * A RecyclerView adapter for [User] class.
 */
class UsersAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val userClickCallback: ((User) -> Unit)?
) : BaseListAdapter<User, ItemUserBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ItemUserBinding {
        val binding = DataBindingUtil.inflate<ItemUserBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_user,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.user?.let {
                userClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: ItemUserBinding, item: User) {
        binding.user = item
    }
}
