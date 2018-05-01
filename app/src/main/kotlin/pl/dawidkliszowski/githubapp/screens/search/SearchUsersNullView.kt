package pl.dawidkliszowski.githubapp.screens.search

import pl.dawidkliszowski.githubapp.model.ui.UserUiItem

object SearchUsersNullView : SearchUsersView {

    override fun showError(message: String) { /* no-op */ }

    override fun showProgress() { /* no-op */ }

    override fun hideProgress() { /* no-op */ }

    override fun showUsers(users: List<UserUiItem>) { /* no-op */ }

    override fun showEmptyPlaceholder() { /* no-op */ }

    override fun hideEmptyPlaceholder() { /* no-op */ }
}