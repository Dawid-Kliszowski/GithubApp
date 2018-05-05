package pl.dawidkliszowski.githubapp.data.model

data class GithubUser(
        val id: Long,
        val login: String,
        val avatarUrl: String?,
        val score: Double,
        val followersUrl: String
)