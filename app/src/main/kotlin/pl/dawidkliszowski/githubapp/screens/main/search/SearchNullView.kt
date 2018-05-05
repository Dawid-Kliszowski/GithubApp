package pl.dawidkliszowski.githubapp.screens.main.search

import pl.dawidkliszowski.githubapp.screens.model.ui.SearchUiItem

object SearchNullView : SearchUsersView {

    override fun showError(message: String) { /* no-op */ }

    override fun showMainProgress() { /* no-op */ }

    override fun hideMainProgress() { /* no-op */ }

    override fun showSearchResults(results: List<SearchUiItem>) { /* no-op */ }

    override fun showEmptyPlaceholder() { /* no-op */ }

    override fun hideEmptyPlaceholder() { /* no-op */ }

    override fun showPaginateProgress() { /* no-op */ }

    override fun hidePaginateProgress() { /* no-op */ }

    override fun showSearchQuery(query: String) { /* no-op */ }
}