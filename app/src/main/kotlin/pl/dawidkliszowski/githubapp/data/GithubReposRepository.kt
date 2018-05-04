package pl.dawidkliszowski.githubapp.data

import io.reactivex.Single
import pl.dawidkliszowski.githubapp.api.ReposApiService
import pl.dawidkliszowski.githubapp.model.api.SearchReposApiResponse
import pl.dawidkliszowski.githubapp.model.domain.GithubRepo
import pl.dawidkliszowski.githubapp.model.mappers.ReposApiResponseMapper
import retrofit2.Response

class GithubReposRepository(
        private val reposApiService: ReposApiService,
        private val mapper: ReposApiResponseMapper
) : BaseRemoteRepository<GithubRepo, SearchReposApiResponse>() {

    override fun queryItems(queryText: String, page: Int, perPage: Int): Single<Response<SearchReposApiResponse>> {
        return reposApiService.queryRepos(queryText, page, perPage)
    }

    override fun mapToDomainModel(apiResponse: SearchReposApiResponse): List<GithubRepo> {
        return mapper.mapApiResponseToDomainRepos(apiResponse)
    }
}