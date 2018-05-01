package pl.dawidkliszowski.githubapp.screens.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.annotation.LayoutRes
import android.view.Menu
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.mvp.MvpFragment
import android.support.v7.widget.SearchView
import android.view.MenuInflater
import android.view.View.*
import kotlinx.android.synthetic.main.fragment_search.*
import pl.dawidkliszowski.githubapp.utils.showToast

class SearchUsersFragment : MvpFragment<SearchUsersView, SearchUsersPresenter>(), SearchUsersView {

    @LayoutRes override val layoutResId = R.layout.fragment_search

    private val uiHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_users, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchMenuItem = menu.findItem(R.id.actionSearchUsersSearchView)
        val usersSearchView = searchMenuItem.actionView as SearchView
        setupSearchView(usersSearchView)
    }

    override fun injectDependencies() {
        fragmentComponent.inject(this)
    }

    private fun setupSearchView(usersSearchView: SearchView) {
        usersSearchView.setIconifiedByDefault(false)
        usersSearchView.queryHint = getString(R.string.menu_search_users_hint)
        usersSearchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean =  true

            override fun onQueryTextChange(newText: String): Boolean {
                presenter.queryTextChanged(newText)
                return true
            }
        })
    }

    private fun runOnUiThread(toRun: () -> Unit) {
        uiHandler.post { toRun() }
    }

    // SearchUsersView interface

    override fun showError(message: String) {
        runOnUiThread { context?.showToast(message) }
    }

    override fun showProgress() {
        runOnUiThread { contentLoadingProgressBar.visibility = VISIBLE }
    }

    override fun hideProgress() {
        runOnUiThread { contentLoadingProgressBar.visibility = GONE }
    }
}