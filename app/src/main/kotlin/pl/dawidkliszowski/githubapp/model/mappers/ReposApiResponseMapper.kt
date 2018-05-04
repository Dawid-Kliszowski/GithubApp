package pl.dawidkliszowski.githubapp.model.mappers

import pl.dawidkliszowski.githubapp.model.api.SearchReposApiResponse
import pl.dawidkliszowski.githubapp.model.domain.GithubRepo
import javax.inject.Inject

class ReposApiResponseMapper @Inject constructor() {

    fun mapApiResponseToDomainRepos(searchRepoResponse: SearchReposApiResponse): List<GithubRepo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}