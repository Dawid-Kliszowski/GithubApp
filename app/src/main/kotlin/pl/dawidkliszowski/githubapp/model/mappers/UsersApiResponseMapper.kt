package pl.dawidkliszowski.githubapp.model.mappers

import pl.dawidkliszowski.githubapp.model.api.SearchUsersResponse
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import javax.inject.Inject

class UsersApiResponseMapper @Inject constructor() {

    fun mapApiResponseToDomainUsers(searchUsersResponse: SearchUsersResponse): List<GithubUser> {
        return searchUsersResponse.users
                .map { searchUserItem ->
                    with (searchUserItem) {
                        val nonBlankOrNullAvatarUrl = getNonBlankOrNull(avatarUrl)
                        GithubUser(id, login, nonBlankOrNullAvatarUrl, score, followersUrl)
                    }
                }
    }

    private fun getNonBlankOrNull(text: String) = if (text.isNotBlank()) text else null
}