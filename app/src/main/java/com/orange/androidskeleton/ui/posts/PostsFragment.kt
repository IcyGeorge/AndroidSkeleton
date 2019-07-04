package com.orange.androidskeleton.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.orange.androidskeleton.AppExecutors
import com.orange.androidskeleton.R
import com.orange.androidskeleton.binding.FragmentDataBindingComponent
import com.orange.androidskeleton.databinding.FragmentPostsBinding
import com.orange.androidskeleton.di.Injectable
import com.orange.androidskeleton.util.autoCleared
import javax.inject.Inject


class PostsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var postsViewModel: PostsViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<FragmentPostsBinding>()

    private var adapter by autoCleared<PostsAdapter>()

    private fun initUserList(viewModel: PostsViewModel) {
        viewModel.users.observe(viewLifecycleOwner, Observer { listResource ->
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource?.data != null) {
                adapter.submitList(listResource.data)
            } else {
                adapter.submitList(emptyList())
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentPostsBinding>(
            inflater,
            R.layout.fragment_posts,
            container,
            false
        )

        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postsViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(PostsViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = PostsAdapter(dataBindingComponent, appExecutors) {
                item -> {}
        }
        this.adapter = adapter
        binding.postList.adapter = adapter
        initUserList(postsViewModel)
    }
}
