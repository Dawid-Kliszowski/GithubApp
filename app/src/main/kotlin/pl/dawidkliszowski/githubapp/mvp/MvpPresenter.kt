package pl.dawidkliszowski.githubapp.mvp

import android.support.annotation.CallSuper

abstract class MvpPresenter<V : MvpView, N : MvpNavigator> {

    protected abstract val nullView: V
    private var view: V? = null
    private var navigator: N? = null

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

    fun getView(): V {
        return view?.let { it } ?: nullView
    }

    fun performNavigation(navigationAction: N.() -> Unit) {
        navigator?.let { navigationAction(it) }
    }
}