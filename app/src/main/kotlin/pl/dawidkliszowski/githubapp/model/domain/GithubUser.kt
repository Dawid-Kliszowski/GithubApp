package pl.dawidkliszowski.githubapp.model.domain

data class GithubUser(
     val login: String,
     val avatarUrl: String,
     val score: Double
)