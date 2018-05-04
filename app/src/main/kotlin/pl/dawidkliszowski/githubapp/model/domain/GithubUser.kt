package pl.dawidkliszowski.githubapp.model.domain

data class GithubUser(
        val id: Long,
        val login: String,
        val avatarUrl: String?,
        val score: Double,
        val followersUrl: String
)