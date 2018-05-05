package pl.dawidkliszowski.githubapp.data

import io.reactivex.Single
import pl.dawidkliszowski.githubapp.data.api.ReposApiService
import pl.dawidkliszowski.githubapp.data.api.model.SearchReposApiResponse
import pl.dawidkliszowski.githubapp.data.model.GithubRepo
import pl.dawidkliszowski.githubapp.data.mappers.ReposApiResponseMapper
import retrofit2.Response
import javax.inject.Inject

class GithubReposRepository @Inject constructor(
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