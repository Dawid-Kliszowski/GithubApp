package pl.dawidkliszowski.githubapp.screens.main.search

import android.content.Context
import pl.dawidkliszowski.githubapp.di.qualifier.ActivityContext
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.model.mappers.UserParcelMapper
import pl.dawidkliszowski.githubapp.mvp.MvpNavigator
import pl.dawidkliszowski.githubapp.screens.userdetails.UserDetailsActivity
import javax.inject.Inject

class SearchUsersNavigator @Inject constructor(
        @ActivityContext private val context: Context,
        private val userParcelMapper: UserParcelMapper
) : MvpNavigator {

    fun goToUserDetailsScreen(user: GithubUser) {
        val userParcel = userParcelMapper.mapToParcel(user)
        UserDetailsActivity.startActivity(context, userParcel)
    }
}