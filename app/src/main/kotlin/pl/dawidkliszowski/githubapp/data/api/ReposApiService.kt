package pl.dawidkliszowski.githubapp.data.api

import io.reactivex.Single
import pl.dawidkliszowski.githubapp.data.api.model.SearchReposApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReposApiService {

    @GET("search/repositories")
    fun queryRepos(
            @Query("q") query: String,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
    ): Single<Response<SearchReposApiResponse>>
}