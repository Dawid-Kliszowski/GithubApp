package pl.dawidkliszowski.githubapp.api

import io.reactivex.Single
import pl.dawidkliszowski.githubapp.model.api.SearchUsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface UsersApiService {

    @GET("search/users")
    fun queryUsers(
            @Query("q") searchQuery: String,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int,
            @Query("sort") sortBy: String
    ): Single<Response<SearchUsersResponse>>

    @GET
    fun getFollowers(
            @Url fullUrl: String
    ): Single<Response<List<Any>>>
}