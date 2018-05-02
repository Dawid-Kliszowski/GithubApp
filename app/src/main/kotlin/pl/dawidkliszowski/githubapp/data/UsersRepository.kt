package pl.dawidkliszowski.githubapp.data

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import pl.dawidkliszowski.githubapp.api.GithubApiService
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.model.mappers.SearchUsersResponseMapper
import java.io.IOException
import javax.inject.Inject

class UsersRepository @Inject constructor(
        private val githubApiService: GithubApiService,
        private val searchUsersResponseMapper: SearchUsersResponseMapper
) {

    fun searchUsers(query: String): Single<List<GithubUser>> {
        return if (query.isNotBlank()) {
            githubApiService.getUsers(query)
                    .map(searchUsersResponseMapper::mapApiResponseToDomainUsers)
                    .onErrorResumeNext { throwable ->
                        if (throwable.isNetworkException) {
                            throw RemoteRepositoryUnavailableException()
                        } else {
                            throw throwable
                        }
                    }
                    .subscribeOn(Schedulers.io())
        } else {
            Single.just(emptyList())
        }
    }

    private val Throwable.isNetworkException
        get() = this is IOException
}