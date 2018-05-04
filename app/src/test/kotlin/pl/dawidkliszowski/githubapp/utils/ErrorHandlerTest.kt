package pl.dawidkliszowski.githubapp.utils

import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.data.RemoteRepositoryLimitsReachedException
import pl.dawidkliszowski.githubapp.data.RemoteRepositoryUnavailableException
import pl.dawidkliszowski.githubapp.data.UnknownRemoteRepositoryException

private const val UNKNOWN_SERVER_ERROR = "Unknown server error."
private const val ERROR_SERVER_UNAVAILABLE = "Something went wrong. Check your internet connection."
private const val ERROR_SERVER_LIMITS_REACHED = "Request limits reached for unauthorized users."

@RunWith(MockitoJUnitRunner::class)
class ErrorHandlerTest {

    @Mock lateinit var stringProvider: StringProvider
    @InjectMocks lateinit var errorHandler: ErrorHandler

    @Before
    fun setUp() {
        whenever(stringProvider.getString(R.string.error_unknown_server_error_message))
                .thenReturn(UNKNOWN_SERVER_ERROR)
        whenever(stringProvider.getString(R.string.error_server_unavailable_message))
                .thenReturn(ERROR_SERVER_UNAVAILABLE)
        whenever(stringProvider.getString(R.string.error_server_message_limits_reached))
                .thenReturn(ERROR_SERVER_LIMITS_REACHED)
    }

    @Test
    fun `returns proper error message for server unavailable error`() {
        val serverErrorMessage = errorHandler.getMessageTextForNonFatalError(RemoteRepositoryUnavailableException())
        assertEquals(ERROR_SERVER_UNAVAILABLE, serverErrorMessage)
    }

    @Test
    fun `returns proper error message for unknown server error`() {
        val serverErrorMessage = errorHandler.getMessageTextForNonFatalError(UnknownRemoteRepositoryException())
        assertEquals(UNKNOWN_SERVER_ERROR, serverErrorMessage)
    }

    @Test
    fun `returns proper error message for server limits reached error`() {
        val serverErrorMessage = errorHandler.getMessageTextForNonFatalError(RemoteRepositoryLimitsReachedException())
        assertEquals(ERROR_SERVER_LIMITS_REACHED, serverErrorMessage)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws exception when getting message for fatal error`() {
        errorHandler.getMessageTextForNonFatalError(RuntimeException())
    }
}