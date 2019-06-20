package com.orange.androidskeleton.di

import com.orange.androidskeleton.MainActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [MainActivityFragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
