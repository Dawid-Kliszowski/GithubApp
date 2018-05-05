package pl.dawidkliszowski.githubapp.data.mappers

import pl.dawidkliszowski.githubapp.data.api.model.SearchReposApiResponse
import pl.dawidkliszowski.githubapp.data.model.GithubRepo
import pl.dawidkliszowski.githubapp.utils.getNonBlankOrNull
import javax.inject.Inject

class ReposApiResponseMapper @Inject constructor() {

    fun mapApiResponseToDomainRepos(searchRepoResponse: SearchReposApiResponse): List<GithubRepo> {
        return searchRepoResponse.items.map { searchRepoItem ->
            with (searchRepoItem) {
                GithubRepo(id, name, description, owner.avatarUrl.getNonBlankOrNull(), owner.login)
            }
        }
    }
}