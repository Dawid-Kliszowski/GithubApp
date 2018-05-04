package pl.dawidkliszowski.githubapp.screens.userdetails

import pl.dawidkliszowski.githubapp.mvp.MvpView

interface UserDetailsView : MvpView {

    fun showAvatar(url: String)

    fun showUsername(username: String)

    fun showUserScore(score: String)

    fun showFollowersCount(count: String)
}