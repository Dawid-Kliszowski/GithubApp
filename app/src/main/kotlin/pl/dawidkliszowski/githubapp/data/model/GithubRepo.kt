package pl.dawidkliszowski.githubapp.data.model

data class GithubRepo(
        val id: Long,
        val name: String,
        val description: String?,
        val ownerAvatarUrl: String?,
        val ownerName: String
)