package pl.dawidkliszowski.githubapp.screens.userdetails

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import pl.dawidkliszowski.githubapp.BaseTest
import pl.dawidkliszowski.githubapp.data.RemoteRepositoryUnavailableException
import pl.dawidkliszowski.githubapp.data.UsersRepository
import pl.dawidkliszowski.githubapp.data.model.GithubUser
import pl.dawidkliszowski.githubapp.utils.ErrorHandler
import pl.dawidkliszowski.githubapp.utils.StringProvider

@RunWith(MockitoJUnitRunner::class)
class UserDetailsPresenterTest : BaseTest() {

    @Mock lateinit var viewMock: UserDetailsView
    @Mock lateinit var navigatorMock: UserDetailsNavigator
    @Mock lateinit var usersRepositoryMock: UsersRepository
    @Mock lateinit var stringProviderMock: StringProvider
    @InjectMocks lateinit var errorHandler: ErrorHandler

    lateinit var userDetailsPresenter: UserDetailsPresenter

    private val testUser = GithubUser(id = 1, login = "User", avatarUrl = "avatar_url", score = 0.1, followersUrl = "followers_url")
    private val testFollowersCount = 3
    private val testFormattedScore = "0,10"

    @Before
    fun setUp() {
        userDetailsPresenter = UserDetailsPresenter(usersRepositoryMock, errorHandler)

        whenever(usersRepositoryMock.fetchFollowersCount(any()))
                .thenReturn(Single.just(testFollowersCount))
        whenever(stringProviderMock.getString(any()))
                .thenReturn("")

        userDetailsPresenter.attachNavigator(navigatorMock)
        userDetailsPresenter.attachView(viewMock)
    }

    @After
    fun finish() {
        userDetailsPresenter.detachView()
        userDetailsPresenter.detachNavigator()
        userDetailsPresenter.onDestroy()
    }

    @Test
    fun `shows static user data after init with user`() {
        userDetailsPresenter.initWithUser(testUser)

        verify(viewMock).showUsername(testUser.login)
        verify(viewMock).showAvatar(testUser.avatarUrl!!)
        verify(viewMock).showUserScore(testFormattedScore)
    }

    @Test
    fun `shows progress when start fetching followers count`() {
        userDetailsPresenter.initWithUser(testUser)
        verify(viewMock).showFollowersProgress()
    }

    @Test
    fun `hides progress after followers count fetched with success`() {
        userDetailsPresenter.initWithUser(testUser)
        verify(viewMock).hideFollowersProgress()
    }

    @Test
    fun `shows followers count after fetched with success`() {
        userDetailsPresenter.initWithUser(testUser)
        verify(viewMock).showFollowersCount(testFollowersCount.toString())
    }

    @Test
    fun `hides progress after non fatal error when fetching followers count`() {
        whenever(usersRepositoryMock.fetchFollowersCount(any()))
                .thenReturn(Single.error(RemoteRepositoryUnavailableException))

        userDetailsPresenter.initWithUser(testUser)
        verify(viewMock).hideFollowersProgress()
    }

    @Test
    fun `shows error message after non fatal error when fetching followers count`() {
        whenever(usersRepositoryMock.fetchFollowersCount(any()))
                .thenReturn(Single.error(RemoteRepositoryUnavailableException))

        userDetailsPresenter.initWithUser(testUser)
        verify(viewMock).showError(any())
    }
}