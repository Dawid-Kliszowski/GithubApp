package pl.dawidkliszowski.githubapp.mvp

abstract class MvpPresenter<V : MvpView, N : MvpNavigator> {

    protected abstract val nullView: V
    private var view: V? = null
    private var navigator: N? = null

    fun attachView(view: V) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    fun attachNavigator(navigator: N) {
        this.navigator = navigator
    }

    fun detachNavigator() {
        this.navigator = null
    }

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