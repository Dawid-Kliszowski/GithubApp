package pl.dawidkliszowski.githubapp.base

import android.support.v4.app.Fragment
import pl.dawidkliszowski.githubapp.GithubApplication
import pl.dawidkliszowski.githubapp.di.component.FragmentComponent
import pl.dawidkliszowski.githubapp.di.module.ActivityModule

open class BaseFragment : Fragment() {

    protected val fragmentComponent: FragmentComponent by lazy {
        GithubApplication.get(activity!!)
                .applicationComponent
                .fragmentComponent(ActivityModule(activity!!))
    }
}