package pl.dawidkliszowski.githubapp.base

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import pl.dawidkliszowski.githubapp.GithubApplication
import pl.dawidkliszowski.githubapp.di.component.ActivityComponent
import pl.dawidkliszowski.githubapp.di.module.ActivityModule

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    protected val activityComponent: ActivityComponent by lazy {
        GithubApplication.get(this)
                .applicationComponent
                .activityComponent(ActivityModule(this))
    }
}