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
import pl.dawidkliszowski.githubapp.model.mappers.UsersUiItemsMapper
import pl.dawidkliszowski.githubapp.mvp.MvpPresenter
import pl.dawidkliszowski.githubapp.utils.StringProvider
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val SEARCH_QUERY_DEBOUNCE_MILLIS = 1000L

class SearchUsersPresenter @Inject constructor(
        private val githubApiService: GithubApiService,
        private val searchUsersResponseMapper: SearchUsersResponseMapper,
        private val usersUiItemsMapper: UsersUiItemsMapper,
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
        val uiItems = usersUiItemsMapper.mapToUiItems(foundUsers)
        getView().showUsers(uiItems)
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
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { getView().showProgress() }
                .switchMapSingle { query ->
                    githubApiService.getUsers(query)
                            .subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach { getView().hideProgress() }
                .map(searchUsersResponseMapper::mapApiResponseToDomainUsers)
                .doOnError(::onError)
                .retry { throwable ->
                    throwable.isNetworkException //otherwise it will crash the app
                }
                .subscribeBy(
                        onNext = ::onSearchResult
                )
    }

    private val Throwable.isNetworkException
        get() = this is IOException
}