package pl.dawidkliszowski.githubapp.data

class UnknownRemoteRepositoryException : Exception("Unknown server error")

class RemoteRepositoryUnavailableException : Exception("Server connection error occurred.")

class RemoteRepositoryLimitsReachedException : Exception("Limit for unauthorized calls reached")