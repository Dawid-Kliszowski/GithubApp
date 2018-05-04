package pl.dawidkliszowski.githubapp.screens.main.search

import pl.dawidkliszowski.githubapp.model.ui.SearchUiItem
import pl.dawidkliszowski.githubapp.mvp.MvpView

interface SearchUsersView : MvpView {

    fun showError(message: String)

    fun showMainProgress()

    fun hideMainProgress()

    fun showPaginateProgress()

    fun hidePaginateProgress()

    fun showSearchResults(results: List<SearchUiItem>)

    fun showEmptyPlaceholder()

    fun hideEmptyPlaceholder()
}