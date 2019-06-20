package com.orange.androidskeleton.di


import com.orange.androidskeleton.ui.user.UserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeUserFragment(): UserFragment
}
