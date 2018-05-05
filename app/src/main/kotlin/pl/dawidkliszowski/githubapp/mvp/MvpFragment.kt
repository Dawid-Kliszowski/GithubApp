package pl.dawidkliszowski.githubapp.mvp

import android.os.Bundle
import android.view.View
import pl.dawidkliszowski.githubapp.BuildConfig
import pl.dawidkliszowski.githubapp.base.BaseFragment
import javax.inject.Inject

private const val BUNDLE_KEY_PRESENTER_STATE = BuildConfig.APPLICATION_ID + ".fragment_presenter_state"

abstract class MvpFragment<V : MvpView, N : MvpNavigator, P : MvpPresenter<V, N>> : BaseFragment(), MvpView {

    @Inject lateinit var presenter: P
    @Inject lateinit var navigator: N

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachNavigator(navigator)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restorePresenterState(savedInstanceState)
        presenter.attachView(this as V)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onDestroy() {
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
}