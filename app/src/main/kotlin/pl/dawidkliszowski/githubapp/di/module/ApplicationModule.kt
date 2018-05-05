package pl.dawidkliszowski.githubapp.di.module

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import pl.dawidkliszowski.githubapp.data.api.ApiConstants
import pl.dawidkliszowski.githubapp.data.api.UsersApiService
import pl.dawidkliszowski.githubapp.di.qualifier.AppContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor
import pl.dawidkliszowski.githubapp.data.api.ReposApiService
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ApiConstants.BASE_URL)
                .build()
    }

    @Provides
    @Singleton
    fun provideUsersApiService(retrofit: Retrofit): UsersApiService = retrofit.createService()

    @Provides
    @Singleton
    fun provideReposApiService(retrofit: Retrofit): ReposApiService = retrofit.createService()

    private fun createOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    @Provides
    fun provideResources(@AppContext context: Context): Resources = context.resources

    @Provides
    fun providePicasso(@AppContext context: Context): Picasso = Picasso.with(context)
}

private inline fun <reified T> Retrofit.createService() = create(T::class.java)