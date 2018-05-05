package pl.dawidkliszowski.githubapp.screens.userdetails

import pl.dawidkliszowski.githubapp.screens.base.mvp.MvpView

interface UserDetailsView : MvpView {

    fun showAvatar(url: String)

    fun showUsername(username: String)

    fun showUserScore(score: String)

    fun showFollowersCount(count: String)

    fun showFollowersProgress()

    fun hideFollowersProgress()

    fun showError(message: String)
}