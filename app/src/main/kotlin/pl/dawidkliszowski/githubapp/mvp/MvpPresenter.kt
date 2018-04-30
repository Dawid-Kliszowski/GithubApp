package pl.dawidkliszowski.githubapp.mvp

import android.support.annotation.CallSuper

abstract class MvpPresenter<V : MvpView> {

    protected abstract val nullView: V
    private var view: V? = null

    @CallSuper
    fun attachView(view: V) {
        //todo implement
    }

    @CallSuper
    fun detachView() {
        //todo implement
    }
}