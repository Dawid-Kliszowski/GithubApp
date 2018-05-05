package pl.dawidkliszowski.githubapp.screens.main.search

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import pl.dawidkliszowski.githubapp.BaseTest
import pl.dawidkliszowski.githubapp.data.GithubReposRepository
import pl.dawidkliszowski.githubapp.data.RemoteRepositoryUnavailableException
import pl.dawidkliszowski.githubapp.data.UsersRepository
import pl.dawidkliszowski.githubapp.model.domain.GithubRepo
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.model.mappers.ReposUiItemsMapper
import pl.dawidkliszowski.githubapp.model.mappers.UsersUiItemsMapper
import pl.dawidkliszowski.githubapp.utils.ErrorHandler
import pl.dawidkliszowski.githubapp.utils.StringProvider
import pl.dawidkliszowski.githubapp.utils.ViewWrapper

private const val SEARCH_QUERY_DEBOUNCE_TIME_MILLIS = 1000L

@RunWith(MockitoJUnitRunner::class)
class SearchPresenterTest : BaseTest() {

    @Mock lateinit var viewMock: SearchUsersView
    @Mock lateinit var navigatorMock: SearchNavigator
    @Mock lateinit var usersRepositoryMock: UsersRepository
    @Mock lateinit var reposRepositoryMock: GithubReposRepository
    @Mock lateinit var stringProvider: StringProvider
    @Spy lateinit var usersUiItemsMapper: UsersUiItemsMapper
    @Spy lateinit var reposUiItemsMapper: ReposUiItemsMapper
    @InjectMocks lateinit var errorHandler: ErrorHandler

    private lateinit var searchPresenter: SearchPresenter

    private val testSearchUsersResult = listOf(
            GithubUser(id = 0, login = "aaa", avatarUrl = null, score = 0.1, followersUrl = ""),
            GithubUser(id = 1, login = "bbb", avatarUrl = null, score = 0.2, followersUrl = ""),
            GithubUser(id = 2, login = "ccc", avatarUrl = null, score = 0.3, followersUrl = "")
    )
    private val testSearchReposResult = listOf(
            GithubRepo(id = 0, name = "aaa", description = "aaa", ownerAvatarUrl = "", ownerName = "a"),
            GithubRepo(id = 1, name = "bbb", description = "bbb", ownerAvatarUrl = "", ownerName = "b"),
            GithubRepo(id = 2, name = "ccc", description = "ccc", ownerAvatarUrl = "", ownerName = "c")
    )
    private val testQuery = "abc"

    @Before
    fun setUp() {
        searchPresenter = SearchPresenter(
                usersRepositoryMock,
                reposRepositoryMock,
                usersUiItemsMapper,
                reposUiItemsMapper,
                errorHandler
        )
        whenever(stringProvider.getString(any()))
                .thenReturn("")
        whenever(usersRepositoryMock.query(any(), any()))
                .thenReturn(Single.just(testSearchUsersResult))
        whenever(reposRepositoryMock.query(any(), any()))
                .thenReturn(Single.just(testSearchReposResult))
        searchPresenter.attachNavigator(navigatorMock)
        searchPresenter.attachView(viewMock)
    }

    @After
    fun finish() {
        searchPresenter.detachView()
        searchPresenter.detachNavigator()
        searchPresenter.onDestroy()
    }

    @Test
    fun `shows progress after one second debounce`() {
        searchPresenter.queryTextChanged(testQuery)

        verify(viewMock, never()).showMainProgress()
        advanceRxTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).showMainProgress()
    }

    @Test
    fun `hides progress after users fetched with success`() {
        searchPresenter.queryTextChanged(testQuery)
        advanceRxTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).hideMainProgress()
    }

    @Test
    fun `hides progress after non fatal error when fetching users`() {
        whenever(usersRepositoryMock.query(any(), any()))
                .thenReturn(Single.error(RemoteRepositoryUnavailableException()))

        searchPresenter.queryTextChanged(testQuery)
        advanceRxTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).hideMainProgress()
    }

    @Test
    fun `shows error message after non fatal error when fetching users`() {
        whenever(usersRepositoryMock.query(any(), any()))
                .thenReturn(Single.error(RemoteRepositoryUnavailableException()))

        searchPresenter.queryTextChanged(testQuery)
        advanceRxTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).showError(any())
    }

    @Test
    fun `hides empty placeholder when start searching users`() {
        whenever(usersRepositoryMock.query(any(), any()))
                .thenReturn(Single.just(emptyList()))
        whenever(reposRepositoryMock.query(any(), any()))
                .thenReturn(Single.just(emptyList()))

        searchPresenter.queryTextChanged(testQuery)
        advanceRxTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).hideEmptyPlaceholder()
    }

    @Test
    fun `shows empty placeholder on empty result when fetching users`() {
        whenever(usersRepositoryMock.query(any(), any()))
                .thenReturn(Single.just(emptyList()))
        whenever(reposRepositoryMock.query(any(), any()))
                .thenReturn(Single.just(emptyList()))

        searchPresenter.queryTextChanged(testQuery)
        advanceRxTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).showEmptyPlaceholder()
    }

    @Test
    fun `not shows empty placeholder on non-empty result when fetching users`() {
        searchPresenter.queryTextChanged(testQuery)
        advanceRxTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock, never()).showEmptyPlaceholder()
    }

    @Test
    fun `navigates to proper user details when selected item`() {
        val avatarImageViewWrapperMock = mock<ViewWrapper>()
        val usernameTextViewWrapperMock = mock<ViewWrapper>()
        val scoreTextViewWrapperMock = mock<ViewWrapper>()

        searchPresenter.queryTextChanged(testQuery)
        advanceRxTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)

        searchPresenter.userSelected(
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
        searchPresenter.queryTextChanged(testQuery)
        advanceRxTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        searchPresenter.nextPageRequest()
        verify(viewMock).showPaginateProgress()
        verify(viewMock, atLeastOnce()).hidePaginateProgress()
    }
}