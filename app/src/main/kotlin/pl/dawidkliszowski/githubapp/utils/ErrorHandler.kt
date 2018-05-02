package pl.dawidkliszowski.githubapp.utils

import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.data.RemoteRepositoryUnavailableException
import javax.inject.Inject

class ErrorHandler @Inject constructor(
        private val stringProvider: StringProvider
) {

    fun isFatalError(throwable: Throwable): Boolean = !isNonFatalError(throwable)

    fun isNonFatalError(throwable: Throwable): Boolean {
        return when (throwable) {
            is RemoteRepositoryUnavailableException -> true
            else -> false
        }
    }

    fun getMessageTextForNonFatalError(throwable: Throwable): String {
        if (isNonFatalError(throwable)) {
            val messageResId = when (throwable) {
                is RemoteRepositoryUnavailableException -> R.string.error_server_message_default
                else -> R.string.error_message_default
            }
            return stringProvider.getString(messageResId)
        } else {
            throw IllegalArgumentException("Error messages are provided only for non-fatal errors")
        }
    }
}