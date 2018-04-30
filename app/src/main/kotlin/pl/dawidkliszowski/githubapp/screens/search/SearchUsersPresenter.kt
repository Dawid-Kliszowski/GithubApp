package pl.dawidkliszowski.githubapp.screens.search

import pl.dawidkliszowski.githubapp.mvp.MvpPresenter

class SearchUsersPresenter : MvpPresenter<SearchUsersView>() {

    override val nullView = SearchUsersNullView
}