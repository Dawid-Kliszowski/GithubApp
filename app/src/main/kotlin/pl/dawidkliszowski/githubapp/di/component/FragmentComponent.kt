package pl.dawidkliszowski.githubapp.di.component

import pl.dawidkliszowski.githubapp.di.FragmentScope
import pl.dawidkliszowski.githubapp.di.module.ActivityModule
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [ActivityModule::class])
interface FragmentComponent