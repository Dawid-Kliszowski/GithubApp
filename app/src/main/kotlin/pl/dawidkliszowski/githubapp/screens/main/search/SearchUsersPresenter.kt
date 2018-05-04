package pl.dawidkliszowski.githubapp.screens.main.search

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import pl.dawidkliszowski.githubapp.data.UsersRepository
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.model.mappers.UsersUiItemsMapper
import pl.dawidkliszowski.githubapp.mvp.MvpPresenter
import pl.dawidkliszowski.githubapp.utils.ErrorHandler
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val SEARCH_QUERY_DEBOUNCE_MILLIS = 1000L

class SearchUsersPresenter @Inject constructor(
        private val usersRepository: UsersRepository,
        private val usersUiItemsMapper: UsersUiItemsMapper,
        private val errorHandler: ErrorHandler
) : MvpPresenter<SearchUsersView, SearchUsersNavigator>() {

    override val nullView = SearchUsersNullView

    private val searchQuerySubject = PublishSubject.create<String>()
    private val nextPageSubject = PublishSubject.create<Unit>()
    private val disposables = CompositeDisposable()
    private var searchResults = mutableListOf<GithubUser>()
    private var currentQuery: String? = null

    init {
        disposables += subscribeToQuerySubject()
        disposables += subscribeToNextPageSubject()
    }

    fun queryTextChanged(query: String) {
        searchQuerySubject.onNext(query)
    }

    fun nextPageRequest() {
        nextPageSubject.onNext(Unit)
    }

    fun userSelected(id: Long) {
        val selectedUser = searchResults.find { it.id == id }
        performNavigation { goToUserDetailsScreen(selectedUser!!) } //Should never be null
    }

    override fun onDestroy() {
        disposables.clear()
    }

    private fun onNextPageSearchResult(foundUsers: List<GithubUser>) {
        searchResults.addAll(foundUsers)
        showSearchResults()
    }

    private fun showSearchResults() {
        val uiItems = usersUiItemsMapper.mapToUiItems(searchResults)
        getView().showUsers(uiItems)

        if (uiItems.isEmpty()) {
            getView().showEmptyPlaceholder()
        } else {
            getView().hideEmptyPlaceholder()
        }
    }

    private fun showErrorMessage(message: String) {
        getView().showError(message)
    }

    private fun subscribeToQuerySubject(): Disposable {
        return searchQuerySubject
                .debounce(SEARCH_QUERY_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .showProgressAndClearList()
                .doOnNext { currentQuery = it }
                .switchMapSingle { query ->
                    usersRepository.searchUsers(query, searchResults.size)
                }
                .hideMainProgress()
                .handleErrors()
                .subscribeBy(
                        onNext = ::onNextPageSearchResult
                )
    }

    private fun subscribeToNextPageSubject(): Disposable {
        return nextPageSubject
                .filter { currentQuery != null }
                .map { currentQuery!! }
                .doOnNext { getView().showPaginateProgress() }
                .switchMapSingle { query ->
                    usersRepository.searchUsers(query, searchResults.size)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { getView().hidePaginateProgress() }
                .subscribeBy(
                        onNext = ::onNextPageSearchResult
                )
    }

    private fun <T> Observable<T>.showProgressAndClearList(): Observable<T> {
        return this.observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    searchResults.clear()
                    getView().run {
                        hideEmptyPlaceholder()
                        showMainProgress()
                        showUsers(emptyList())
                    }
                }
    }

    private fun <T> Observable<T>.hideMainProgress(): Observable<T> {
        return this.observeOn(AndroidSchedulers.mainThread())
                .doOnEach { getView().hideMainProgress() }
    }

    private fun <T> Observable<T>.handleErrors(): Observable<T> {
        return this
                .doOnError { throwable ->
                    if (errorHandler.isNonFatalError(throwable)) {
                        showErrorMessage(errorHandler.getMessageTextForNonFatalError(throwable))
                    }
                }
                .retry { throwable -> errorHandler.isNonFatalError(throwable) }
    }
}