package pl.dawidkliszowski.githubapp.mvp

import android.os.Bundle
import pl.dawidkliszowski.githubapp.base.BaseActivity
import javax.inject.Inject

abstract class MvpActivity<V : MvpView, P : MvpPresenter<V>> : BaseActivity(), MvpView {

    @Inject lateinit var presenter: P

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this as V)
    }

    override fun onDestroy() {
        presenter.detachView()
        presenter.onDestroy()
        super.onDestroy()
    }
}