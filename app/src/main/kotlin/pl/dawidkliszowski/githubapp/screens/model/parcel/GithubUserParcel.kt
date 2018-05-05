package pl.dawidkliszowski.githubapp.screens.model.parcel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubUserParcel(
        val id: Long,
        val login: String,
        val avatarUrl: String?,
        val score: Double,
        val followersUrl: String
) : Parcelable