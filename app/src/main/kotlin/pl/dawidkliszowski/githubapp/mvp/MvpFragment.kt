package pl.dawidkliszowski.githubapp.mvp

import android.os.Bundle
import android.view.View
import pl.dawidkliszowski.githubapp.base.BaseFragment
import javax.inject.Inject

abstract class MvpFragment<V : MvpView, P : MvpPresenter<V>> : BaseFragment(), MvpView {

    @Inject lateinit var presenter: P

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this as V)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}