package pl.dawidkliszowski.githubapp.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import pl.dawidkliszowski.githubapp.di.qualifier.AppContext

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @AppContext
    fun provideAppContext(): Context = application
}