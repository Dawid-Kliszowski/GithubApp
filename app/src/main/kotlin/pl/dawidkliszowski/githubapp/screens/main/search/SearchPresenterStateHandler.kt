package pl.dawidkliszowski.githubapp.screens.main.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.dawidkliszowski.githubapp.data.model.GithubRepo
import pl.dawidkliszowski.githubapp.data.model.GithubUser
import pl.dawidkliszowski.githubapp.data.mappers.ReposParcelMapper
import pl.dawidkliszowski.githubapp.data.mappers.UserParcelMapper
import pl.dawidkliszowski.githubapp.screens.model.parcel.GithubRepoParcel
import pl.dawidkliszowski.githubapp.screens.model.parcel.GithubUserParcel
import javax.inject.Inject

class SearchPresenterStateHandler @Inject constructor(
       private val userParcelMapper: UserParcelMapper,
       private val repoParcelMapper: ReposParcelMapper
) {

    fun saveState(
            searchUserResults: List<GithubUser>,
            searchRepoResults: List<GithubRepo>,
            currentQuery: String?
    ): Parcelable? {
        return PresenterStateParcel(
                searchUserResults.map(userParcelMapper::toParcel),
                searchRepoResults.map(repoParcelMapper::toParcel),
                currentQuery
        )
    }

    fun restoreState(parcel: Parcelable): SearchPresenterState {
        with (parcel as PresenterStateParcel) {
            return SearchPresenterState(
                    searchUserResults.map(userParcelMapper::fromParcel),
                    searchRepoResults.map(repoParcelMapper::fromParcel),
                    currentQuery
            )
        }
    }
}

@Parcelize
class PresenterStateParcel(
        val searchUserResults: List<GithubUserParcel>,
        val searchRepoResults: List<GithubRepoParcel>,
        val currentQuery: String?
) : Parcelable

class SearchPresenterState(
        val searchUserResults: List<GithubUser>,
        val searchRepoResults: List<GithubRepo>,
        val currentQuery: String?
)