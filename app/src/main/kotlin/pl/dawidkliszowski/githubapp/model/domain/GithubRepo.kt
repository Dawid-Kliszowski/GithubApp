package pl.dawidkliszowski.githubapp.model.domain

data class GithubRepo(
        val fullName: String,
        val description: String,
        val ownerAvatarUrl: String?
)