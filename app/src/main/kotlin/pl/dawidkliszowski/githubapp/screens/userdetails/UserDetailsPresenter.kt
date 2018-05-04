package pl.dawidkliszowski.githubapp.screens.userdetails

import pl.dawidkliszowski.githubapp.mvp.MvpPresenter
import javax.inject.Inject

class UserDetailsPresenter @Inject constructor() : MvpPresenter<UserDetailsView, UserDetailsNavigator>() {

    override val nullView = UserDetailsNullView
}