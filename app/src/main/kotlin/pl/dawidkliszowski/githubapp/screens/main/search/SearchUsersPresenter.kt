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
import pl.dawidkliszowski.githubapp.utils.ViewWrapper
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
        disposables += subscribeToSubjects()
    }

    fun queryTextChanged(query: String) {
        searchQuerySubject.onNext(query)
    }

    fun nextPageRequest() {
        nextPageSubject.onNext(Unit)
    }

    fun userSelected(
            id: Long,
            avatarImageView: ViewWrapper,
            usernameTextView: ViewWrapper,
            scoreTextView: ViewWrapper
    ) {
        val selectedUser = searchResults.find { it.id == id }
        performNavigation {
            goToUserDetailsScreen(
                    selectedUser!!, //Should never be null
                    avatarImageView,
                    usernameTextView,
                    scoreTextView
            )
        }
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

    private fun subscribeToSubjects(): Disposable {
        val queryObservables = Observable.merge( //combined into one observable to prevent making multiple repository requests at the same time
                getNewQueryObservable(),
                getLoadNextPageObservable()
        )

        return queryObservables
                .switchMapSingle { query ->
                    usersRepository.searchUsers(query, searchResults.size)
                }
                .hideProgressViews()
                .handleErrors()
                .subscribeBy(
                        onNext = ::onNextPageSearchResult
                )
    }

    private fun getNewQueryObservable(): Observable<String> {
        return searchQuerySubject
                .debounce(SEARCH_QUERY_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .showProgressAndClearList()
                .doOnNext { currentQuery = it }
    }

    private fun getLoadNextPageObservable(): Observable<String> {
        return nextPageSubject
                .filter { currentQuery != null }
                .map { currentQuery!! }
                .doOnNext { getView().showPaginateProgress() }
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

    private fun <T> Observable<T>.hideProgressViews(): Observable<T> {
        return this.observeOn(AndroidSchedulers.mainThread())
                .doOnEach {
                    getView().hideMainProgress()
                    getView().hidePaginateProgress()
                }
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