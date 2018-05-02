package pl.dawidkliszowski.githubapp.model.mappers

import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.model.parcel.GithubUserParcel
import javax.inject.Inject

class UserParcelMapper @Inject constructor() {

    fun mapToParcel(user: GithubUser): GithubUserParcel {
        return with (user) {
            GithubUserParcel(id, login, avatarUrl, score)
        }
    }
}