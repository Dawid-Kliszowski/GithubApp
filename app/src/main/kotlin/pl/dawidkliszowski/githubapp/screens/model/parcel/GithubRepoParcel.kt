package pl.dawidkliszowski.githubapp.screens.model.parcel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubRepoParcel(
        val id: Long,
        val name: String,
        val description: String?,
        val ownerAvatarUrl: String?,
        val ownerName: String
) : Parcelable