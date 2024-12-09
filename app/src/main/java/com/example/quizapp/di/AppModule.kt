package com.example.quizapp.di

import android.content.Context
import androidx.room.Room
import com.example.quizapp.data.api.ApiService
import com.example.quizapp.data.dao.BookmarkDao
import com.example.quizapp.data.dao.BookmarkDatabase
import com.example.quizapp.data.repoImpl.RepositoryImpl
import com.example.quizapp.domain.repository.Repository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().setLenient().disableHtmlEscaping().serializeNulls().create()
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(apiService: ApiService, bookmarkDao: BookmarkDao): Repository {
        return RepositoryImpl(apiService, bookmarkDao)
    }


    @Singleton
    @Provides
    fun providesRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient).baseUrl("https://run.mocky.io/").build();
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): BookmarkDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BookmarkDatabase::class.java,
            "bookmark_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideBookmarkDao(database: BookmarkDatabase): BookmarkDao {
        return database.bookmarkDao()
    }
}