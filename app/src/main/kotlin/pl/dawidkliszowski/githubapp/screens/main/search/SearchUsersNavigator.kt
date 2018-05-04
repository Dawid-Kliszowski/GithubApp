package pl.dawidkliszowski.githubapp.screens.main.search

import android.app.Activity
import android.content.Context
import android.view.View
import pl.dawidkliszowski.githubapp.di.qualifier.ActivityContext
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.model.mappers.UserParcelMapper
import pl.dawidkliszowski.githubapp.mvp.MvpNavigator
import pl.dawidkliszowski.githubapp.screens.userdetails.StartUserActivityRequest
import pl.dawidkliszowski.githubapp.screens.userdetails.UserDetailsActivity
import pl.dawidkliszowski.githubapp.utils.ViewWrapper
import javax.inject.Inject

class SearchUsersNavigator @Inject constructor(
        private val activity: Activity,
        private val userParcelMapper: UserParcelMapper
) : MvpNavigator {

    fun goToUserDetailsScreen(
            user: GithubUser,
            avatarImageView: ViewWrapper,
            usernameTextView: ViewWrapper,
            scoreTextView: ViewWrapper
    ) {
        val userParcel = userParcelMapper.mapToParcel(user)
        val startActivityRequest = StartUserActivityRequest.Builder(userParcel)
                .setAvatarImageView(avatarImageView)
                .setUsernameTextView(usernameTextView)
                .setScoreTextView(scoreTextView)
                .build()

        UserDetailsActivity.startActivity(activity, startActivityRequest)
    }
}