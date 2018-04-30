package pl.dawidkliszowski.githubapp.di.component

import pl.dawidkliszowski.githubapp.di.ActivityScope
import pl.dawidkliszowski.githubapp.di.module.ActivityModule
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent
