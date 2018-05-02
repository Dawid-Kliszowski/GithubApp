package pl.dawidkliszowski.githubapp.screens.userdetails

import android.content.Context
import android.content.Intent
import android.support.annotation.LayoutRes
import pl.dawidkliszowski.githubapp.BuildConfig
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.base.BaseActivity
import pl.dawidkliszowski.githubapp.model.parcel.GithubUserParcel

private const val BUNDLE_KEY_USER = BuildConfig.APPLICATION_ID + ".bundle_key_user"

class UserDetailsActivity : BaseActivity() {

    companion object {

        fun startActivity(context: Context, user: GithubUserParcel) {
            val intent = Intent(context, UserDetailsActivity::class.java).apply {
                putExtra(BUNDLE_KEY_USER, user)
            }
            context.startActivity(intent)
        }
    }

    @LayoutRes override val layoutResId: Int = R.layout.activity_user_details

    override fun injectDependencies() {
        //no-op
        //nothing to inject
    }
}