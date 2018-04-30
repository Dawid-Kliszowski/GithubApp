package pl.dawidkliszowski.githubapp.di.module

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import pl.dawidkliszowski.githubapp.api.ApiConstants
import pl.dawidkliszowski.githubapp.api.GithubApiService
import pl.dawidkliszowski.githubapp.di.qualifier.AppContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @AppContext
    fun provideAppContext(): Context = application

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        val gsonConverterFactory = GsonConverterFactory.create(gson)
        return Retrofit.Builder()
                .client(createOkHttpClient())
                .addConverterFactory(gsonConverterFactory)
                .baseUrl(ApiConstants.BASE_URL)
                .build()
    }

    @Provides
    @Singleton
    fun provideGithubApiService(retrofit: Retrofit): GithubApiService = retrofit.createService()

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .build()
    }
}

private inline fun <reified T> Retrofit.createService() = create(T::class.java)