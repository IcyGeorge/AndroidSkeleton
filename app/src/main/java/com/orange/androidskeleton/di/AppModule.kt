
package com.orange.androidskeleton.di

import android.app.Application
import androidx.room.Room
import com.orange.androidskeleton.repository.api.WebService
import com.orange.androidskeleton.repository.db.AppDb
import com.orange.androidskeleton.repository.db.RedditPostDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideWebService(): WebService {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
        return Retrofit.Builder()
            .baseUrl(WebService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(WebService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDb {
        return Room
            .databaseBuilder(app, AppDb::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRedditPostDao(db: AppDb): RedditPostDao {
        return db.redditPostDao()
    }

}
