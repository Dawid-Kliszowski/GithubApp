package pl.dawidkliszowski.githubapp.screens.main.search

import pl.dawidkliszowski.githubapp.model.ui.UserUiItem

object SearchUsersNullView : SearchUsersView {

    override fun showError(message: String) { /* no-op */ }

    override fun showMainProgress() { /* no-op */ }

    override fun hideMainProgress() { /* no-op */ }

    override fun showUsers(users: List<UserUiItem>) { /* no-op */ }

    override fun showEmptyPlaceholder() { /* no-op */ }

    override fun hideEmptyPlaceholder() { /* no-op */ }

    override fun showPaginateProgress() { /* no-op */ }

    override fun hidePaginateProgress() { /* no-op */ }
}