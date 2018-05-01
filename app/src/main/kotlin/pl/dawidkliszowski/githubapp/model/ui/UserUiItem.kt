package pl.dawidkliszowski.githubapp.model.ui

data class UserUiItem(
        val id: Long,
        val login: String,
        val avatarUrl: String?,
        val score: String
)