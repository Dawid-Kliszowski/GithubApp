package pl.dawidkliszowski.githubapp.mvp

import android.os.Bundle
import pl.dawidkliszowski.githubapp.base.BaseActivity
import javax.inject.Inject

abstract class MvpActivity<V : MvpView, N : MvpNavigator, P : MvpPresenter<V, N>> : BaseActivity(), MvpView {

    @Inject lateinit var presenter: P
    @Inject lateinit var navigator: N

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachNavigator(navigator)
        presenter.attachView(this as V)
    }

    override fun onDestroy() {
        presenter.detachView()
        presenter.detachNavigator()
        presenter.onDestroy()
        super.onDestroy()
    }
}