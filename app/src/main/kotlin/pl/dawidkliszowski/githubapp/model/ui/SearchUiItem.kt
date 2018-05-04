package pl.dawidkliszowski.githubapp.model.ui

sealed class SearchUiItem {

    abstract val id: Long

    data class UserUiItem(
            override val id: Long,
            val login: String,
            val avatarUrl: String?,
            val score: String
    ) : SearchUiItem()

    data class RepoUiItem(
            override val id: Long,
            val name: String,
            val description: String?,
            val ownerAvatarUrl: String?,
            val ownerName: String
    ) : SearchUiItem()
}