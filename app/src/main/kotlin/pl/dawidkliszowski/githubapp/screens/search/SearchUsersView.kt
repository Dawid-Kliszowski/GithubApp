package pl.dawidkliszowski.githubapp.screens.search

import pl.dawidkliszowski.githubapp.model.ui.UserUiItem
import pl.dawidkliszowski.githubapp.mvp.MvpView

interface SearchUsersView : MvpView {

    fun showError(message: String)

    fun showProgress()

    fun hideProgress()

    fun showUsers(users: List<UserUiItem>)

    fun showEmptyPlaceholder()

    fun hideEmptyPlaceholder()
}