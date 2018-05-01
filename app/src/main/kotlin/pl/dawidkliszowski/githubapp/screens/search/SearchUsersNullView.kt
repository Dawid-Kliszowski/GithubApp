package pl.dawidkliszowski.githubapp.screens.search

object SearchUsersNullView : SearchUsersView {

    override fun showError(message: String) { /* no-op */ }

    override fun showProgress() { /* no-op */ }

    override fun hideProgress() { /* no-op */ }
}