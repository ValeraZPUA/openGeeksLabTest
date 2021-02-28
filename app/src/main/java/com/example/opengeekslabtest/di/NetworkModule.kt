package com.example.opengeekslabtest.di

import com.example.opengeekslabtest.network.Requests
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(private var baseURL: String) {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return RetrofitUrlManager.getInstance().with(OkHttpClient
            .Builder())
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    @Provides
    @Singleton
    fun providesService(retrofit: Retrofit): Requests {
        return retrofit.create(Requests::class.java)
    }
}