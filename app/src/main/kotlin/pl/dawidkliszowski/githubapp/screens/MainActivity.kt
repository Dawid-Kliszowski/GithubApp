package pl.dawidkliszowski.githubapp.screens

import android.support.annotation.LayoutRes
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.base.BaseActivity

class MainActivity : BaseActivity() {

    @LayoutRes override val layoutResId = R.layout.activity_main

    override fun injectDependencies() {
        //no-op
        //nothing to inject
    }
}
