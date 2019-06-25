package com.orange.androidskeleton.ui.user

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
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.orange.androidskeleton.AppExecutors
import com.orange.androidskeleton.R
import com.orange.androidskeleton.binding.FragmentDataBindingComponent
import com.orange.androidskeleton.databinding.FragmentUserBinding
import com.orange.androidskeleton.di.Injectable
import com.orange.androidskeleton.ui.common.RetryCallback
import com.orange.androidskeleton.util.autoCleared
import javax.inject.Inject


class UserFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<FragmentUserBinding>()

    private var adapter by autoCleared<UsersAdapter>()

    private fun initUserList(viewModel: UserViewModel) {
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
        val dataBinding = DataBindingUtil.inflate<FragmentUserBinding>(
            inflater,
            R.layout.fragment_user,
            container,
            false
        )

        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(UserViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = UsersAdapter(dataBindingComponent, appExecutors) {
                user -> {}
        }
        this.adapter = adapter
        binding.userList.adapter = adapter
        postponeEnterTransition()
        initUserList(userViewModel)
    }
}
