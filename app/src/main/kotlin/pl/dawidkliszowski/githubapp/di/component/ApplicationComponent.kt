package pl.dawidkliszowski.githubapp.di.component

import dagger.Component
import pl.dawidkliszowski.githubapp.di.module.ActivityModule
import pl.dawidkliszowski.githubapp.di.module.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent
    fun fragmentComponent(activityModule: ActivityModule): FragmentComponent
}