package pl.dawidkliszowski.githubapp.screens.search

import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import pl.dawidkliszowski.githubapp.api.GithubApiService
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.model.mappers.SearchUsersResponseMapper
import pl.dawidkliszowski.githubapp.mvp.MvpPresenter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val SEARCH_QUERY_DEBOUNCE_MILLIS = 1000L

class SearchUsersPresenter @Inject constructor(
        private val githubApiService: GithubApiService,
        private val searchUsersResponseMapper: SearchUsersResponseMapper
) : MvpPresenter<SearchUsersView>() {

    override val nullView = SearchUsersNullView

    private val searchQuerySubject = PublishSubject.create<String>()
    private val searchQueryDisposable = subscribeToSearchSubject()

    fun queryTextChanged(query: String) {
        searchQuerySubject.onNext(query)
    }

    override fun onDestroy() {
        searchQueryDisposable.dispose()
    }

    private fun onSearchResult(foundUsers: List<GithubUser>) {
        //todo implement
    }

    private fun subscribeToSearchSubject(): Disposable {
        return searchQuerySubject
                .debounce(SEARCH_QUERY_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle { query ->
                    githubApiService.getUsers(query)
                }
                .map(searchUsersResponseMapper::mapApiResponseToDomainUsers)
                .subscribeBy(
                        onNext = ::onSearchResult
                        //todo implement error handling
                )
    }
}