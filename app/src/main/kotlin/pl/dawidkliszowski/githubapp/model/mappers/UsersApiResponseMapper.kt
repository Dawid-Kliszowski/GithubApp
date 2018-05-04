package pl.dawidkliszowski.githubapp.model.mappers

import pl.dawidkliszowski.githubapp.model.api.SearchUsersResponse
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.utils.getNonBlankOrNull
import javax.inject.Inject

class UsersApiResponseMapper @Inject constructor() {

    fun mapApiResponseToDomainUsers(searchUsersResponse: SearchUsersResponse): List<GithubUser> {
        return searchUsersResponse.users
                .map { searchUserItem ->
                    with (searchUserItem) {
                        GithubUser(id, login, avatarUrl.getNonBlankOrNull(), score, followersUrl)
                    }
                }
    }
}