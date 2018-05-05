package pl.dawidkliszowski.githubapp.data

object UnknownRemoteRepositoryException : Exception("Unknown server error")

object RemoteRepositoryUnavailableException : Exception("Server connection error occurred.")

object RemoteRepositoryLimitsReachedException : Exception("Limit for unauthorized calls reached")