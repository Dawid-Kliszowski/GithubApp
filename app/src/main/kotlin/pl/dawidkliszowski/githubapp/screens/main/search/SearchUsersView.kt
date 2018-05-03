package pl.dawidkliszowski.githubapp.screens.main.search

import pl.dawidkliszowski.githubapp.model.ui.UserUiItem
import pl.dawidkliszowski.githubapp.mvp.MvpView

interface SearchUsersView : MvpView {

    fun showError(message: String)

    fun showMainProgress()

    fun hideMainProgress()

    fun showPaginateProgress()

    fun hidePaginateProgress()

    fun showUsers(users: List<UserUiItem>)

    fun showEmptyPlaceholder()

    fun hideEmptyPlaceholder()
}