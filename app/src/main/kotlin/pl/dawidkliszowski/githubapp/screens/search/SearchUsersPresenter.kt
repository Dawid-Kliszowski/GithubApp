package pl.dawidkliszowski.githubapp.screens.search

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.api.GithubApiService
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.model.mappers.SearchUsersResponseMapper
import pl.dawidkliszowski.githubapp.mvp.MvpPresenter
import pl.dawidkliszowski.githubapp.utils.StringProvider
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val SEARCH_QUERY_DEBOUNCE_MILLIS = 1000L

class SearchUsersPresenter @Inject constructor(
        private val githubApiService: GithubApiService,
        private val searchUsersResponseMapper: SearchUsersResponseMapper,
        private val stringProvider: StringProvider
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

    private fun onError(throwable: Throwable) {
        if (throwable.isNetworkException) {
            val defaultErrorMessage = stringProvider.getString(R.string.error_message_default)
            getView().showError(defaultErrorMessage)
        }
    }

    private fun subscribeToSearchSubject(): Disposable {
        return searchQuerySubject
                .debounce(SEARCH_QUERY_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle { query ->
                    githubApiService.getUsers(query)
                            .showProgressWhileSubscribed()
                }
                .map(searchUsersResponseMapper::mapApiResponseToDomainUsers)
                .doOnError(::onError)
                .retry { throwable ->
                    throwable.isNetworkException //otherwise it will crash the app
                }
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = ::onSearchResult
                )
    }

    private fun <T> Single<T>.showProgressWhileSubscribed(): Single<T> {
        return this.doOnSubscribe { getView().showProgress() }
                .doFinally { getView().hideProgress() }
    }

    private val Throwable.isNetworkException
        get() = this is IOException
}