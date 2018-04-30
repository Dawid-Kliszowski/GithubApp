package pl.dawidkliszowski.githubapp.mvp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class MvpActivity<V : MvpView, out P : MvpPresenter<V>> : AppCompatActivity(), MvpView {

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