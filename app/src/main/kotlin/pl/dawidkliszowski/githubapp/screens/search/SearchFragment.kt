package pl.dawidkliszowski.githubapp.screens.search

import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.mvp.MvpFragment

class SearchFragment : MvpFragment<SearchView, SearchPresenter>() {

    override val presenter = SearchPresenter()
    override val layoutResId = R.layout.fragment_search
}