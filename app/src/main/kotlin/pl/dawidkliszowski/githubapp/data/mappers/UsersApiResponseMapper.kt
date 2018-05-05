package pl.dawidkliszowski.githubapp.data.mappers

import pl.dawidkliszowski.githubapp.data.api.model.SearchUsersResponse
import pl.dawidkliszowski.githubapp.data.model.GithubUser
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