package pl.dawidkliszowski.githubapp.screens.userdetails

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import pl.dawidkliszowski.githubapp.data.UsersRepository
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.mvp.MvpPresenter
import pl.dawidkliszowski.githubapp.utils.ErrorHandler
import pl.dawidkliszowski.githubapp.utils.format
import javax.inject.Inject

class UserDetailsPresenter @Inject constructor(
        private val usersRepository: UsersRepository,
        private val errorHandler: ErrorHandler
) : MvpPresenter<UserDetailsView, UserDetailsNavigator>() {

    override val nullView = UserDetailsNullView
    private val disposables = CompositeDisposable()

    private lateinit var user: GithubUser
    private var followersCount: Int? = null

    fun initWithUser(user: GithubUser) {
        this.user = user
        showAvatar()
        showUsername()
        showScore()
        fetchFollowersCount()
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

    private fun fetchFollowersCount() {
        getView().showFollowersProgress()

        disposables += usersRepository
                .getFollowersCount(user.followersUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent { _, _ -> getView().hideFollowersProgress() }
                .subscribeBy(
                        onSuccess = ::onFollowersCountFetched,
                        onError = ::onError
                )
    }

    private fun onFollowersCountFetched(count: Int) {
        followersCount = count
        getView().showFollowersCount(count.toString())
    }

    private fun onError(throwable: Throwable) {
        if (errorHandler.isNonFatalError(throwable)) {
            val errorMessage = errorHandler.getMessageTextForNonFatalError(throwable)
            getView().showError(errorMessage)
        } else {
            throw throwable
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}