package pl.dawidkliszowski.githubapp.screens.search

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import pl.dawidkliszowski.githubapp.RxTestSchedulersInitializer
import pl.dawidkliszowski.githubapp.data.UsersRepository
import pl.dawidkliszowski.githubapp.model.mappers.UsersUiItemsMapper
import pl.dawidkliszowski.githubapp.utils.ErrorHandler
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

    @Mock lateinit var viewMock: SearchUsersView
    @Mock lateinit var usersRepositoryMock: UsersRepository
    @Mock lateinit var stringProvider: StringProvider
    @Spy lateinit var usersUiItemsMapper: UsersUiItemsMapper
    @InjectMocks lateinit var errorHandler: ErrorHandler

    lateinit var searchUsersPresenter: SearchUsersPresenter

    @Before
    fun setUp() {
        searchUsersPresenter = SearchUsersPresenter(
                usersRepositoryMock,
                usersUiItemsMapper,
                errorHandler
        )
        searchUsersPresenter.attachView(viewMock)
    }

    @After
    fun finish() {
        searchUsersPresenter.detachView()
        searchUsersPresenter.onDestroy()
    }

    @Test
    fun `shows progress after one second debounce`() {
        whenever(usersRepositoryMock.searchUsers(any()))
                .thenReturn(Single.just(emptyList()))

        searchUsersPresenter.queryTextChanged("abc")

        advanceTime(SEARCH_QUERY_DEBOUNCE_TIME_MILLIS)
        verify(viewMock).showProgress()
    }

    private fun advanceTime(timeMillis: Long) {
        testSchedulersInitializer.testScheduler
                .advanceTimeBy(timeMillis, TimeUnit.MILLISECONDS)
    }
}