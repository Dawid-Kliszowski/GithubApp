package pl.dawidkliszowski.githubapp.screens.userdetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import pl.dawidkliszowski.githubapp.BuildConfig
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.screens.model.parcel.GithubUserParcel
import pl.dawidkliszowski.githubapp.screens.base.mvp.MvpActivity
import pl.dawidkliszowski.githubapp.utils.ViewWrapper
import android.support.v4.util.Pair
import android.view.View.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_details.*
import pl.dawidkliszowski.githubapp.data.mappers.UserParcelMapper
import pl.dawidkliszowski.githubapp.utils.showToast
import javax.inject.Inject

private const val BUNDLE_KEY_USER = BuildConfig.APPLICATION_ID + ".bundle_key_user"

class UserDetailsActivity : MvpActivity<UserDetailsView, UserDetailsNavigator, UserDetailsPresenter>(), UserDetailsView {

    companion object {

        fun startActivity(activity: Activity, request: StartUserActivityRequest) {
            val intent = Intent(activity, UserDetailsActivity::class.java).apply {
                putExtra(BUNDLE_KEY_USER, request.user)
            }
            val options = buildViewTransitionsOptions(activity, request)

            activity.startActivity(intent, options)
        }

        private fun buildViewTransitionsOptions(activity: Activity, request: StartUserActivityRequest): Bundle? {
            val transitionViewsPairs = mutableListOf<Pair<View, String>>()

            with (request) {
                avatarImageView?.let {
                    transitionViewsPairs += Pair(it.view, activity.getString(R.string.transition_avatar_image))
                }
                usernameTextView?.let {
                    transitionViewsPairs += Pair(it.view, activity.getString(R.string.transition_username_text))
                }
                scoreTextView?.let {
                    transitionViewsPairs += Pair(it.view, activity.getString(R.string.transition_score_text))
                }
            }

            return ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, *transitionViewsPairs.toTypedArray())
                    .toBundle()
        }
    }

    @LayoutRes override val layoutResId: Int = R.layout.activity_user_details

    @Inject lateinit var userParcelMapper: UserParcelMapper
    @Inject lateinit var picasso: Picasso

    override fun injectDependencies() {
        activityComponent.inject(this)
    }

    override fun onInitPresenter() {
        super.onInitPresenter()
        initPresenterWithUser()
    }

    private fun initPresenterWithUser() {
        val githubUser = userParcelMapper.fromParcel(intent.getParcelableExtra(BUNDLE_KEY_USER))
        presenter.initWithUser(githubUser)
    }

    // UserDetailsView interface

    override fun showAvatar(url: String) {
        picasso.load(url).into(avatarImageView)
    }

    override fun showUsername(username: String) {
        usernameTextView.text = username
    }

    override fun showUserScore(score: String) {
        scoreTextView.text = score
    }

    override fun showFollowersCount(count: String) {
        followersTextView.text = count
    }

    override fun showFollowersProgress() {
        followersProgressBar.visibility = VISIBLE
    }

    override fun hideFollowersProgress() {
        followersProgressBar.visibility = GONE
    }

    override fun showError(message: String) {
        showToast(message)
    }
}

class StartUserActivityRequest private constructor(
        val user: GithubUserParcel,
        val avatarImageView: ViewWrapper?,
        val usernameTextView: ViewWrapper?,
        val scoreTextView: ViewWrapper?
){

    class Builder(private val user: GithubUserParcel) {

        private var avatarImageView: ViewWrapper? = null
        private var usernameTextView: ViewWrapper? = null
        private var scoreTextView: ViewWrapper? = null

        fun setAvatarImageView(view: ViewWrapper): Builder {
            avatarImageView = view
            return this
        }

        fun setUsernameTextView(view: ViewWrapper): Builder {
            usernameTextView = view
            return this
        }

        fun setScoreTextView(view: ViewWrapper): Builder {
            scoreTextView = view
            return this
        }

        fun build(): StartUserActivityRequest {
            return StartUserActivityRequest(user, avatarImageView, usernameTextView, scoreTextView)
        }
    }
}