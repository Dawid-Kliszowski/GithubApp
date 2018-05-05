package pl.dawidkliszowski.githubapp.screens.userdetails

import android.os.Parcelable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.parcel.Parcelize
import pl.dawidkliszowski.githubapp.data.UsersRepository
import pl.dawidkliszowski.githubapp.data.model.GithubUser
import pl.dawidkliszowski.githubapp.screens.base.mvp.MvpPresenter
import pl.dawidkliszowski.githubapp.utils.ErrorHandler
import pl.dawidkliszowski.githubapp.utils.format
import javax.inject.Inject

class UserDetailsPresenter @Inject constructor(
        private val usersRepository: UsersRepository,
        private val errorHandler: ErrorHandler
) : MvpPresenter<UserDetailsView, UserDetailsNavigator>() {

    override val nullView = UserDetailsNullView
    private val disposables = CompositeDisposable()
    private var user: GithubUser? = null
    private var followersCount: Int? = null

    fun initWithUser(user: GithubUser) {
        this.user = user
        showViewState()
        fetchFollowersCount(user)
    }

    override fun attachView(view: UserDetailsView) {
        super.attachView(view)
        showViewState()
    }

    private fun showViewState() {
        showAvatar()
        showUsername()
        showScore()
        showFollowersCount()
    }

    private fun showAvatar() {
        user?.avatarUrl?.let {
            getView().showAvatar(it)
        }
    }

    private fun showUsername() {
        user?.let {
            getView().showUsername(it.login)
        }
    }

    private fun showScore() {
        user?.let {
            val scoreText = it.score.format(2)
            getView().showUserScore(scoreText)
        }
    }

    private fun fetchFollowersCount(user: GithubUser) {
        getView().showFollowersProgress()

        disposables += usersRepository
                .fetchFollowersCount(user.followersUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent { _, _ -> getView().hideFollowersProgress() }
                .subscribeBy(
                        onSuccess = ::onFollowersCountFetched,
                        onError = ::onError
                )
    }

    private fun onFollowersCountFetched(count: Int) {
        followersCount = count
        showFollowersCount()
    }

    private fun showFollowersCount() {
        followersCount?.let {
            getView().showFollowersCount(it.toString())
        }
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

    override fun saveState(): Parcelable? {
        return UserDetailsPresenterState(followersCount)
    }

    override fun restoreState(parcel: Parcelable?) {
        parcel?.let {
            followersCount = (it as UserDetailsPresenterState).followersCount
        }
    }
}

@Parcelize
class UserDetailsPresenterState(
        val followersCount: Int?
) : Parcelable