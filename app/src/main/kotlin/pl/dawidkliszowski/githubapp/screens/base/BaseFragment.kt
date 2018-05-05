package pl.dawidkliszowski.githubapp.screens.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.dawidkliszowski.githubapp.GithubApplication
import pl.dawidkliszowski.githubapp.di.component.FragmentComponent
import pl.dawidkliszowski.githubapp.di.module.ActivityModule

abstract class BaseFragment : Fragment() {

    protected val fragmentComponent: FragmentComponent by lazy {
        GithubApplication.get(activity!!)
                .applicationComponent
                .fragmentComponent(ActivityModule(activity!!))
    }

    protected abstract val layoutResId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutResId, container, false)
    }

    protected abstract fun injectDependencies()
}