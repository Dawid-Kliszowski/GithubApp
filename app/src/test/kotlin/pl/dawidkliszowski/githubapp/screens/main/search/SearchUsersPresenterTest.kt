package pl.dawidkliszowski.githubapp.screens.main.search

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import pl.dawidkliszowski.githubapp.RxTestSchedulersInitializer
import pl.dawidkliszowski.githubapp.data.RemoteRepositoryUnavailableException
import pl.dawidkliszowski.githubapp.data.UsersRepository
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.model.mappers.UsersUiItemsMapper
import pl.dawidkliszowski.githubapp.utils.ErrorHandler
import pl.dawidkliszowski.githubapp.utils.StringProvider
import pl.dawidkliszowski.githubapp.utils.ViewWrapper
import java.util.concurrent.TimeUnit

private const val SEARCH_QUERY_DEBOUNCE_TIME_MILLIS = 1000L

@RunWith(MockitoJUnitRunner::class)
class SearchUsersPresenterTest {

    companion object {
        val testSchedulersInitializer = RxTestSchedulersInitializer()

        @BeforeClass
        @JvmStatic
        fun setUpClass() {
            testSchedulersInitializer.initTestSchedulers()
        }

        @AfterClass
        @JvmStatic
        fun finishClass() {
            testSchedulersInitializer.reset()
        }
    }

    @Mock lateinit var viewMock: SearchUsersView
    @Mock lateinit var navigatorMock: SearchUsersNavigator
    @Mock lateinit var usersRepositoryMock: UsersRepository
    @Mock lateinit var stringProvider: StringProvider
    @Spy lateinit var usersUiItemsMapper: UsersUiItemsMapper
    @InjectMocks lateinit var errorHandler: ErrorHandler

    lateinit var searchUsersPresenter: SearchUsersPresenter

    private val testSearchUsersResult = listOf(
            GithubUser(id = 0, login = "aaa", avatarUrl = null, score = 0.1, followersUrl = ""),
            GithubUser(id = 1, login = "bbb", avatarUrl = null, score = 0.2, followersUrl = ""),
            GithubUser(id = 2, login = "ccc", avatarUrl = null, score = 0.3, followersUrl = "")
    )
    private val testQuery = "abc"

    @Before
    fun setUp() {
        searchUsersPresenter = SearchUsersPresenter(
                usersRepositoryMock,
                usersUiItemsMapper,
                errorHandler
        )
        whenever(stringProvider.getString(any())).thenReturn("")
        searchUsersPresenter.attachNavigator(navigatorMock)
        searchUsersPresenter.attachView(viewMock)
    }

    @After
    fun finish() {
        searchUsersPresenter.detachView()
        searchUsersPresenter.detachNavigator()
        searchUsersPresenter.onDestroy()
    }

    @Test
    fun `shows progress after one second debounce`() {
        whenever(usersRepositoryMock.searchUsers(any(), any()))
                .thenReturn(Single.just(emptyList()))

        searchUsersPresenter.queryTextChanged(testQuery)

        verify(viewMock, never()).showMainProgress()
        advanceTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).showMainProgress()
    }

    @Test
    fun `hides progress after users fetched with success`() {
        whenever(usersRepositoryMock.searchUsers(any(), any()))
                .thenReturn(Single.just(emptyList()))

        searchUsersPresenter.queryTextChanged(testQuery)
        advanceTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).hideMainProgress()
    }

    @Test
    fun `hides progress after non fatal error when fetching users`() {
        whenever(usersRepositoryMock.searchUsers(any(), any()))
                .thenReturn(Single.error(RemoteRepositoryUnavailableException()))

        searchUsersPresenter.queryTextChanged(testQuery)
        advanceTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).hideMainProgress()
    }

    @Test
    fun `shows error message after non fatal error when fetching users`() {
        whenever(usersRepositoryMock.searchUsers(any(), any()))
                .thenReturn(Single.error(RemoteRepositoryUnavailableException()))

        searchUsersPresenter.queryTextChanged(testQuery)
        advanceTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).showError(any())
    }

    @Test
    fun `hides empty placeholder when start searching users`() {
        whenever(usersRepositoryMock.searchUsers(any(), any()))
                .thenReturn(Single.just(emptyList()))

        searchUsersPresenter.queryTextChanged(testQuery)
        advanceTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).hideEmptyPlaceholder()
    }

    @Test
    fun `shows empty placeholder on empty result when fetching users`() {
        whenever(usersRepositoryMock.searchUsers(any(), any()))
                .thenReturn(Single.just(emptyList()))

        searchUsersPresenter.queryTextChanged(testQuery)
        advanceTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).showEmptyPlaceholder()
    }

    @Test
    fun `not shows empty placeholder on non-empty result when fetching users`() {
        whenever(usersRepositoryMock.searchUsers(any(), any()))
                .thenReturn(Single.just(testSearchUsersResult))

        searchUsersPresenter.queryTextChanged(testQuery)
        advanceTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock, never()).showEmptyPlaceholder()
    }

    @Test
    fun `navigates to proper user details when selected item`() {
        whenever(usersRepositoryMock.searchUsers(any(), any()))
                .thenReturn(Single.just(testSearchUsersResult))
        val avatarImageViewWrapperMock = mock<ViewWrapper>()
        val usernameTextViewWrapperMock = mock<ViewWrapper>()
        val scoreTextViewWrapperMock = mock<ViewWrapper>()

        searchUsersPresenter.queryTextChanged(testQuery)
        advanceTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)

        searchUsersPresenter.userSelected(
                testSearchUsersResult[0].id,
                avatarImageViewWrapperMock,
                usernameTextViewWrapperMock,
                scoreTextViewWrapperMock
        )

        verify(navigatorMock).goToUserDetailsScreen(
                testSearchUsersResult[0],
                avatarImageViewWrapperMock,
                usernameTextViewWrapperMock,
                scoreTextViewWrapperMock
        )
    }

    @Test
    fun `shows and hides paginate progress when paginating`() {
        whenever(usersRepositoryMock.searchUsers(any(), any()))
                .thenReturn(Single.just(emptyList()))

        searchUsersPresenter.queryTextChanged(testQuery)
        advanceTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        searchUsersPresenter.nextPageRequest()
        verify(viewMock).showPaginateProgress()
        verify(viewMock, atLeastOnce()).hidePaginateProgress()
    }

    private fun advanceTime(timeMillis: Long) {
        testSchedulersInitializer.testScheduler
                .advanceTimeBy(timeMillis, TimeUnit.MILLISECONDS)
    }
}