package com.example.fomenu.ui.community

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fomenu.R
import com.example.fomenu.ui.home.UserName
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForumPostAdapter(private val forumPosts: List<ForumPost>, private val currentUserUserName: String) : RecyclerView.Adapter<ForumPostViewHolder>() {

    init {
        Log.d("ForumAdapter", "Adapter initialized with current user: $currentUserUserName")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumPostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.forum_list_item, parent, false)
        return ForumPostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ForumPostViewHolder, position: Int) {
        val forumPost = forumPosts[position]
        holder.bind(forumPost)
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

        if (currentUserEmail == forumPost.userEmail) {
            val color = ContextCompat.getColor(holder.itemView.context, R.color.sand)
            holder.layout.setBackgroundColor(color)
            Log.d("ForumAdapter", "Background color changed for user: ${forumPost.userEmail}")

        }
        else {
            Log.d("ForumAdapter", "Default background set for user: ${forumPost.userEmail}")

        }
    }

    override fun getItemCount(): Int {
        return forumPosts.size
    }
}
class ForumPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textViewTimestamp = itemView.findViewById<TextView>(R.id.textViewTimestamp)
    val textViewUsername = itemView.findViewById<TextView>(R.id.textViewUsername)
    val textViewContent = itemView.findViewById<TextView>(R.id.textViewContent)
    val layout: LinearLayout = itemView.findViewById(R.id.forumPostLayout)


    fun bind(forumPost: ForumPost){
        textViewUsername.text = forumPost.username
        textViewContent.text = forumPost.content
        val timestamp = forumPost.timestamp
        textViewTimestamp.text = convertTimestampToString(timestamp)
    }
    private fun convertTimestampToString(timestamp: Long): String {

        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }

}

