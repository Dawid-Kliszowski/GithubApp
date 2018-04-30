package pl.dawidkliszowski.githubapp.mvp

import android.os.Bundle
import pl.dawidkliszowski.githubapp.base.BaseActivity

abstract class MvpActivity<V : MvpView, out P : MvpPresenter<V>> : BaseActivity(), MvpView {

    abstract val presenter: P

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this as V)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}