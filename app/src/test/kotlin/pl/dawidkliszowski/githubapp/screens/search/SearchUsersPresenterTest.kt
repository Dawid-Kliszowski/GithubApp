package pl.dawidkliszowski.githubapp.screens.search

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import pl.dawidkliszowski.githubapp.RxTestSchedulersInitializer
import pl.dawidkliszowski.githubapp.api.GithubApiService
import pl.dawidkliszowski.githubapp.model.api.SearchUsersResponse
import pl.dawidkliszowski.githubapp.model.mappers.SearchUsersResponseMapper
import pl.dawidkliszowski.githubapp.model.mappers.UsersUiItemsMapper
import pl.dawidkliszowski.githubapp.utils.StringProvider
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

    @Spy lateinit var searchUsersResponseMapper: SearchUsersResponseMapper
    @Spy lateinit var usersUiItemsMapper: UsersUiItemsMapper
    @Mock lateinit var viewMock: SearchUsersView
    @Mock lateinit var githubApiServiceMock: GithubApiService
    @Mock lateinit var stringProviderMock: StringProvider

    @InjectMocks lateinit var searchUsersPresenter: SearchUsersPresenter

    @Before
    fun setUp() {
        searchUsersPresenter.attachView(viewMock)
    }

    @After
    fun finish() {
        searchUsersPresenter.detachView()
        searchUsersPresenter.onDestroy()
    }

    @Test
    fun `shows progress after one second debounce`() {
        val emptyApiUsersResponse = SearchUsersResponse(0, true, emptyList())
        whenever(githubApiServiceMock.getUsers(any()))
                .thenReturn(Single.just(emptyApiUsersResponse))

        searchUsersPresenter.queryTextChanged("abc")

        advanceTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).showProgress()
    }

    private fun advanceTime(timeMillis: Long) {
        testSchedulersInitializer.testScheduler
                .advanceTimeBy(timeMillis, TimeUnit.MILLISECONDS)
    }
}