package pl.dawidkliszowski.githubapp.screens.main.search

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import android.view.Menu
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.screens.base.mvp.MvpFragment
import android.support.v7.widget.SearchView
import android.view.MenuInflater
import android.view.View
import android.view.View.*
import kotlinx.android.synthetic.main.fragment_search.*
import pl.dawidkliszowski.githubapp.screens.model.ui.SearchUiItem
import pl.dawidkliszowski.githubapp.screens.main.search.adapter.UsersAdapter
import pl.dawidkliszowski.githubapp.utils.ViewWrapper
import pl.dawidkliszowski.githubapp.utils.showToast
import javax.inject.Inject

class SearchFragment : MvpFragment<SearchUsersView, SearchNavigator, SearchPresenter>(), SearchUsersView {

    @LayoutRes override val layoutResId = R.layout.fragment_search

    @Inject lateinit var usersAdapter: UsersAdapter
    private var toolbarSearchView: SearchView? = null
        set(value) {
            field = value
            field?.let { postToolbarSearchViewAction?.invoke() }
        }
    private var postToolbarSearchViewAction: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setupAdapter()
    }

    override fun onDestroy() {
        usersAdapter.onUserItemClickListener = null
        usersAdapter.onScrollToLastNonProgressItem = null
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_users, menu)
        super.onCreateOptionsMenu(menu, inflater)
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

    override fun onDestroyView() {
        toolbarSearchView = null
        super.onDestroyView()
    }

    override fun injectDependencies() {
        fragmentComponent.inject(this)
    }

    private fun setupSearchView(toolbarSearchView: SearchView) {
        this.toolbarSearchView = toolbarSearchView
        toolbarSearchView.apply {
            setIconifiedByDefault(false)
            queryHint = getString(R.string.menu_search_users_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean = true

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
        usersAdapter.onUserItemClickListener = { userId, avatarImageView, usernameTextView, scoreTextView ->
            presenter.userSelected(
                    userId,
                    ViewWrapper(avatarImageView),
                    ViewWrapper(usernameTextView),
                    ViewWrapper(scoreTextView)
            )
        }
        usersAdapter.onScrollToLastNonProgressItem = presenter::nextPageRequest
    }

    // SearchUsersView interface

    override fun showError(message: String) {
        context?.showToast(message)
    }

    override fun showMainProgress() {
        contentLoadingProgressBar.visibility = VISIBLE
    }

    override fun hideMainProgress() {
        contentLoadingProgressBar.visibility = GONE
    }

    override fun showEmptyPlaceholder() {
        emptyPlaceholderTextView.visibility = VISIBLE
    }

    override fun hideEmptyPlaceholder() {
        emptyPlaceholderTextView.visibility = INVISIBLE
    }

    override fun showSearchResults(results: List<SearchUiItem>) {
        usersAdapter.items = results
        notifyAdapter()
    }

    override fun showPaginateProgress() {
        usersAdapter.isNextPageProgressVisible = true
        notifyAdapter()
    }

    override fun hidePaginateProgress() {
        usersAdapter.isNextPageProgressVisible = false
        notifyAdapter()
    }

    private fun notifyAdapter() {
        if (usersRecyclerView.isComputingLayout) {
            usersRecyclerView.post {
                notifyAdapter()
            }
        } else {
            usersAdapter.notifyDataSetChanged()
        }
    }

    override fun showSearchQuery(query: String) {
        if (toolbarSearchView != null) {
            toolbarSearchView!!.setQuery(query, false)
        } else {
            postToolbarSearchViewAction = { toolbarSearchView?.setQuery(query, false) }
        }
    }
}