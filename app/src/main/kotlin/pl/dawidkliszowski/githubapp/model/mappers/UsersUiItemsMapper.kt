package pl.dawidkliszowski.githubapp.model.mappers

import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.model.ui.UserUiItem
import pl.dawidkliszowski.githubapp.utils.format
import javax.inject.Inject

class UsersUiItemsMapper @Inject constructor() {

    fun mapToUiItems(domainUsers: List<GithubUser>): List<UserUiItem> {
        return domainUsers.map { githubUser ->
            with (githubUser) {
                UserUiItem(id, login, avatarUrl, score.format(digitsAfterDot = 2))
            }
        }
    }
}