package pl.dawidkliszowski.githubapp.screens.userdetails

import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.mvp.MvpPresenter
import pl.dawidkliszowski.githubapp.utils.format
import javax.inject.Inject

class UserDetailsPresenter @Inject constructor() : MvpPresenter<UserDetailsView, UserDetailsNavigator>() {

    override val nullView = UserDetailsNullView

    private lateinit var user: GithubUser

    fun initWithUser(user: GithubUser) {
        this.user = user
        showAvatar()
        showUsername()
        showScore()
        showFollowersCount()
    }

    private fun showAvatar() {
        user.avatarUrl?.let { getView().showAvatar(it) }
    }

    private fun showUsername() {
        getView().showUsername(user.login)
    }

    private fun showScore() {
        val scoreText = user.score.format(2)
        getView().showUserScore(scoreText)
    }

    private fun showFollowersCount() {
        //todo implement
    }
}