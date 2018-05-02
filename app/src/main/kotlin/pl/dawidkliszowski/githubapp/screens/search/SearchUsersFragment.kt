package pl.dawidkliszowski.githubapp.screens.search

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.mvp.MvpFragment
import android.support.v7.widget.SearchView
import android.view.MenuInflater
import android.view.View
import android.view.View.*
import kotlinx.android.synthetic.main.fragment_search.*
import pl.dawidkliszowski.githubapp.model.ui.UserUiItem
import pl.dawidkliszowski.githubapp.utils.showToast
import javax.inject.Inject

class SearchUsersFragment : MvpFragment<SearchUsersView, SearchUsersPresenter>(), SearchUsersView {

    @LayoutRes override val layoutResId = R.layout.fragment_search

    @Inject lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setupAdapter()
    }

    override fun onDestroy() {
        usersAdapter.onUserItemClickListener = null
        super.onDestroy()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun injectDependencies() {
        fragmentComponent.inject(this)
    }

    private fun setupSearchView(usersSearchView: SearchView) {
        usersSearchView.apply {
            setIconifiedByDefault(false)
            queryHint = getString(R.string.menu_search_users_hint)
            setOnQueryTextListener( object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean =  true

                override fun onQueryTextChange(newText: String): Boolean {
                    presenter.queryTextChanged(newText)
                    return true
                }
            })
        }
    }

    private fun setupRecyclerView() {
        usersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
        }
    }

    private fun setupAdapter() {
        usersAdapter.onUserItemClickListener = presenter::userSelected
    }

    // SearchUsersView interface

    override fun showError(message: String) {
        context?.showToast(message)
    }

    override fun showProgress() {
        contentLoadingProgressBar.visibility = VISIBLE
    }

    override fun hideProgress() {
        contentLoadingProgressBar.visibility = GONE
    }

    override fun showUsers(users: List<UserUiItem>) {
        usersAdapter.userItems = users
    }

    override fun showEmptyPlaceholder() {
        emptyPlaceholderTextView.visibility = VISIBLE
    }

    override fun hideEmptyPlaceholder() {
        emptyPlaceholderTextView.visibility = INVISIBLE
    }
}