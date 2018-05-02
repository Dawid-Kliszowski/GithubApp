package pl.dawidkliszowski.githubapp.screens.main.search

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
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
    private val searchQueryDisposable = subscribeToSearchSubject()
    private var searchResults = listOf<GithubUser>()

    fun queryTextChanged(query: String) {
        searchQuerySubject.onNext(query)
    }

    fun userSelected(id: Long) {
        val selectedUser = searchResults.find { it.id == id }
        performNavigation { goToUserDetailsScreen(selectedUser!!) } //Should never be null
    }

    override fun onDestroy() {
        searchQueryDisposable.dispose()
    }

    private fun onSearchResult(foundUsers: List<GithubUser>) {
        searchResults = foundUsers
        showSearchResults()
    }

    private fun showSearchResults() {
        val uiItems = usersUiItemsMapper.mapToUiItems(searchResults)
        getView().showUsers(uiItems)

        if (uiItems.isEmpty()) {
            getView().showEmptyPlaceholder()
        }
    }

    private fun showErrorMessage(message: String) {
        getView().showError(message)
    }

    private fun subscribeToSearchSubject(): Disposable {
        return searchQuerySubject
                .debounce(SEARCH_QUERY_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .showProgressAndHideEmptyPlaceholder()
                .switchMapSingle { query ->
                    usersRepository.searchUsers(query)
                }
                .hideProgress()
                .handleErrors()
                .subscribeBy(
                        onNext = ::onSearchResult
                )
    }

    private fun <T> Observable<T>.showProgressAndHideEmptyPlaceholder(): Observable<T> {
        return this.observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    getView().run {
                        hideEmptyPlaceholder()
                        showProgress()
                    }
                }
    }

    private fun <T> Observable<T>.hideProgress(): Observable<T> {
        return this.observeOn(AndroidSchedulers.mainThread())
                .doOnEach { getView().hideProgress() }
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