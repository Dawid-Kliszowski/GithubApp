package pl.dawidkliszowski.githubapp.api

import io.reactivex.Single
import pl.dawidkliszowski.githubapp.model.api.SearchUsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {

    @GET("search/users")
    fun getUsers(
            @Query("q") searchQuery: String,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
    ): Single<SearchUsersResponse>
}