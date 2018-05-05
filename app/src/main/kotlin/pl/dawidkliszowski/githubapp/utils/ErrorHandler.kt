package pl.dawidkliszowski.githubapp.utils

import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.data.RemoteRepositoryLimitsReachedException
import pl.dawidkliszowski.githubapp.data.RemoteRepositoryUnavailableException
import pl.dawidkliszowski.githubapp.data.UnknownRemoteRepositoryException
import javax.inject.Inject

class ErrorHandler @Inject constructor(
        private val stringProvider: StringProvider
) {

    fun isFatalError(throwable: Throwable): Boolean = !isNonFatalError(throwable)

    fun isNonFatalError(throwable: Throwable): Boolean {
        return when (throwable) {
            RemoteRepositoryUnavailableException,
            RemoteRepositoryLimitsReachedException,
            UnknownRemoteRepositoryException -> true
            else -> false
        }
    }

    fun getMessageTextForNonFatalError(throwable: Throwable): String {
        if (isNonFatalError(throwable)) {
            val messageResId = when (throwable) {
                UnknownRemoteRepositoryException -> R.string.error_unknown_server_error_message
                RemoteRepositoryUnavailableException -> R.string.error_server_unavailable_message
                RemoteRepositoryLimitsReachedException -> R.string.error_server_message_limits_reached
                else -> R.string.error_message_default
            }
            return stringProvider.getString(messageResId)
        } else {
            throw IllegalArgumentException("Error messages are provided only for non-fatal errors")
        }
    }
}