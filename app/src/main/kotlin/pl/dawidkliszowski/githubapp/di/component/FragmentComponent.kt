package pl.dawidkliszowski.githubapp.di.component

import pl.dawidkliszowski.githubapp.di.FragmentScope
import pl.dawidkliszowski.githubapp.di.module.ActivityModule
import dagger.Subcomponent
import pl.dawidkliszowski.githubapp.screens.main.search.SearchUsersFragment

@FragmentScope
@Subcomponent(modules = [ActivityModule::class])
interface FragmentComponent {

    fun inject(searchUsersFragment: SearchUsersFragment)
}