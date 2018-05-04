package pl.dawidkliszowski.githubapp.di.component

import pl.dawidkliszowski.githubapp.di.ActivityScope
import pl.dawidkliszowski.githubapp.di.module.ActivityModule
import dagger.Subcomponent
import pl.dawidkliszowski.githubapp.screens.userdetails.UserDetailsActivity

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(userDetailsActivity: UserDetailsActivity)
}
