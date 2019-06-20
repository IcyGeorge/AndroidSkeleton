package com.orange.androidskeleton.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.orange.androidskeleton.ViewModelFactory

import com.orange.androidskeleton.di.ViewModelKey

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
//    @Binds
//    @IntoMap
//    @ViewModelKey(UserViewModel::class)
//    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
