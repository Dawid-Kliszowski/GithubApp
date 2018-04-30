package pl.dawidkliszowski.githubapp.screens.search

import pl.dawidkliszowski.githubapp.mvp.MvpPresenter

class SearchPresenter : MvpPresenter<SearchView>() {

    override val nullView = SearchNullView
}