package pl.dawidkliszowski.githubapp.api

import io.reactivex.Single
import pl.dawidkliszowski.githubapp.model.api.SearchUsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApiService {

    @GET("search/users")
    fun queryUsers(
            @Query("q") searchQuery: String,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
    ): Single<Response<SearchUsersResponse>>
}