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
import pl.dawidkliszowski.githubapp.data.RemoteRepositoryUnavailableException

private const val SERVER_ERROR_MESSAGE = "Something went wrong. Check your internet connection."

@RunWith(MockitoJUnitRunner::class)
class ErrorHandlerTest {

    @Mock lateinit var stringProvider: StringProvider
    @InjectMocks lateinit var errorHandler: ErrorHandler

    @Before
    fun setUp() {
        whenever(stringProvider.getString(R.string.error_server_message_default))
                .thenReturn(SERVER_ERROR_MESSAGE)
    }

    @Test
    fun `returns proper error message for server error`() {
        val serverErrorMessage = errorHandler.getMessageTextForNonFatalError(RemoteRepositoryUnavailableException())
        assertEquals(SERVER_ERROR_MESSAGE, serverErrorMessage)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws exception when getting message for fatal error`() {
        errorHandler.getMessageTextForNonFatalError(RuntimeException())
    }
}