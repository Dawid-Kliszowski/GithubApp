package pl.dawidkliszowski.githubapp.screens.base.mvp

import android.os.Parcelable
import android.support.annotation.CallSuper

abstract class MvpPresenter<V : MvpView, N : MvpNavigator> {

    private var view: V? = null
    private var navigator: N? = null

    abstract fun saveState(): Parcelable?

    abstract fun restoreState(parcel: Parcelable?)

    @CallSuper
    open fun attachView(view: V) {
        this.view = view
    }

    @CallSuper
    open fun detachView() {
        this.view = null
    }

    @CallSuper
    open fun attachNavigator(navigator: N) {
        this.navigator = navigator
    }

    @CallSuper
    open fun detachNavigator() {
        this.navigator = null
    }

    @CallSuper
    open fun onDestroy() {
        //no-op
        //prepared to override in particular presenter implementations
    }

    fun performViewAction(viewAction: V.() -> Unit) {
        view?.let { viewAction(it) }
    }

    fun performNavigation(navigationAction: N.() -> Unit) {
        navigator?.let { navigationAction(it) }
    }
}