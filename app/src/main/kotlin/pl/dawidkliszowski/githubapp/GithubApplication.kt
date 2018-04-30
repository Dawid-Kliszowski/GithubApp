package pl.dawidkliszowski.githubapp

import android.app.Application
import android.content.Context
import pl.dawidkliszowski.githubapp.di.component.ApplicationComponent
import pl.dawidkliszowski.githubapp.di.component.DaggerApplicationComponent
import pl.dawidkliszowski.githubapp.di.module.ApplicationModule

class GithubApplication : Application() {

    companion object {
        fun get(context: Context) = context.applicationContext as GithubApplication
    }

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}