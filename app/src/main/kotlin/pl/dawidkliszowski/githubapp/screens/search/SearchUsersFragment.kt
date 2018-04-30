package pl.dawidkliszowski.githubapp.screens.search

import android.os.Bundle
import android.view.Menu
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.mvp.MvpFragment
import android.support.v7.widget.SearchView
import android.view.MenuInflater

class SearchUsersFragment : MvpFragment<SearchUsersView, SearchUsersPresenter>() {

    override val presenter = SearchUsersPresenter()
    override val layoutResId = R.layout.fragment_search

    private lateinit var usersSearchView: SearchView

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
        usersSearchView = searchMenuItem.actionView as SearchView
        usersSearchView.setIconifiedByDefault(false)
        usersSearchView.queryHint = getString(R.string.menu_search_users_hint)
    }
}