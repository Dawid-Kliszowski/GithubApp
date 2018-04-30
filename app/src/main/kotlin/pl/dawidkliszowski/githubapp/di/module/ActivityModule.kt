package pl.dawidkliszowski.githubapp.di.module

import android.app.Activity
import android.content.Context
import pl.dawidkliszowski.githubapp.di.qualifier.ActivityContext
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @ActivityContext
    fun provideActivityContext(): Context = activity

    @Provides
    fun provideActivity(): Activity = activity
}
