package pl.dawidkliszowski.githubapp.model.domain

data class GithubRepo(
        val id: Long,
        val name: String,
        val description: String?,
        val ownerAvatarUrl: String?,
        val ownerName: String
)