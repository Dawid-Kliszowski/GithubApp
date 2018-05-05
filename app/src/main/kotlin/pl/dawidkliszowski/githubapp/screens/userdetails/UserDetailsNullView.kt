package pl.dawidkliszowski.githubapp.screens.userdetails

object UserDetailsNullView : UserDetailsView {

    override fun showAvatar(url: String) { /* no-op */ }

    override fun showUsername(username: String) { /* no-op */ }

    override fun showUserScore(score: String) { /* no-op */ }

    override fun showFollowersCount(count: String) { /* no-op */ }

    override fun showFollowersProgress() { /* no-op */ }

    override fun hideFollowersProgress() { /* no-op */ }

    override fun showError(message: String) { /* no-op */ }
}