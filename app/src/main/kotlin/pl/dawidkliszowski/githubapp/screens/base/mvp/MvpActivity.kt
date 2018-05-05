package pl.dawidkliszowski.githubapp.screens.base.mvp

import android.os.Bundle
import pl.dawidkliszowski.githubapp.BuildConfig
import pl.dawidkliszowski.githubapp.screens.base.BaseActivity
import javax.inject.Inject

private const val BUNDLE_KEY_PRESENTER_STATE = BuildConfig.APPLICATION_ID + ".activity_presenter_state"

abstract class MvpActivity<V : MvpView, N : MvpNavigator, P : MvpPresenter<V, N>> : BaseActivity(), MvpView {

    @Inject lateinit var presenter: P
    @Inject lateinit var navigator: N

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInitPresenter()
        restorePresenterState(savedInstanceState)
        presenter.attachNavigator(navigator)
        presenter.attachView(this as V)
    }

    override fun onDestroy() {
        presenter.detachView()
        presenter.detachNavigator()
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val presenterStateParcel = presenter.saveState()
        presenterStateParcel?.let {
            outState.putParcelable(BUNDLE_KEY_PRESENTER_STATE, presenterStateParcel)
        }
    }

    private fun restorePresenterState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY_PRESENTER_STATE)) {
            presenter.restoreState(savedInstanceState.getParcelable(BUNDLE_KEY_PRESENTER_STATE))
        }
    }

    open fun onInitPresenter() {
        //no-op
        //prepared to override in particular activity implementations
    }
}