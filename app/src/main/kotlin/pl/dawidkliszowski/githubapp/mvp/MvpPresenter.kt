package pl.dawidkliszowski.githubapp.mvp

import android.support.annotation.CallSuper

abstract class MvpPresenter<V : MvpView> {

    protected abstract val nullView: V
    private var view: V? = null

    @CallSuper
    fun attachView(view: V) {
        this.view = view
    }

    @CallSuper
    fun detachView() {
        this.view = null
    }

    open fun onDestroy() {
        //no-op
        //prepared to override in particular presenter implementations
    }

    fun getView(): V {
        return view?.let { it } ?: nullView
    }
}