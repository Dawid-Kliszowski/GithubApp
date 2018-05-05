package pl.dawidkliszowski.githubapp.screens.model.mappers

import pl.dawidkliszowski.githubapp.data.model.GithubUser
import pl.dawidkliszowski.githubapp.screens.model.ui.SearchUiItem
import pl.dawidkliszowski.githubapp.utils.format
import javax.inject.Inject

class UsersUiItemsMapper @Inject constructor() {

    fun mapToUiItems(domainUsers: List<GithubUser>): List<SearchUiItem.UserUiItem> {
        return domainUsers.map { githubUser ->
            with (githubUser) {
                SearchUiItem.UserUiItem(id, login, avatarUrl, score.format(digitsAfterDot = 2))
            }
        }
    }
}