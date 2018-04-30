package pl.dawidkliszowski.githubapp.model.mappers

import pl.dawidkliszowski.githubapp.model.api.SearchUsersResponse
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import javax.inject.Inject

class SearchUsersResponseMapper @Inject constructor() {

    fun mapApiResponseToDomainUsers(searchUsersResponse: SearchUsersResponse): List<GithubUser> {
        return searchUsersResponse.users
                .map { searchUserItem ->
                    with (searchUserItem) {
                        GithubUser(login, avatarUrl, score)
                    }
                }
    }
}