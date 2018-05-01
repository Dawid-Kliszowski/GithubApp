package pl.dawidkliszowski.githubapp.screens.search

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class UsersAdapter : RecyclerView.Adapter<UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(parent)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)