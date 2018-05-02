package pl.dawidkliszowski.githubapp.mvp

import android.os.Bundle
import android.view.View
import pl.dawidkliszowski.githubapp.base.BaseFragment
import javax.inject.Inject

abstract class MvpFragment<V : MvpView, N : MvpNavigator, P : MvpPresenter<V, N>> : BaseFragment(), MvpView {

    @Inject lateinit var presenter: P
    @Inject lateinit var navigator: N

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachNavigator(navigator)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this as V)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter.detachNavigator()
        presenter.onDestroy()
        super.onDestroy()
    }
}