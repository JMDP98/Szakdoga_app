package com.example.fomenu.ui.community

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fomenu.R
import com.example.fomenu.databinding.FragmentCommunityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class ForumPost(
    val username: String,
    val content: String,
    val timestamp: Long,
    val userEmail: String?

)

class Community : Fragment() {

    companion object {
        fun newInstance() = Community()
    }

    private var _binding: FragmentCommunityBinding? = null
    private lateinit var viewModel: CommunityViewModel
    private val db = FirebaseFirestore.getInstance()
    private lateinit var buttonSubmitPost: Button
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var editTextPostContent: EditText
    private lateinit var forumPostAdapter: ForumPostAdapter
    private val forumPosts = mutableListOf<ForumPost>()
    private lateinit var recyclerView: RecyclerView




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_community, container, false)

        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        buttonSubmitPost = view.findViewById(R.id.buttonSubmitPost)
        editTextPostContent = view.findViewById(R.id.editTextPostContent)

        buttonSubmitPost.setOnClickListener {

            val postContent = editTextPostContent.text.toString().trim()

            if (postContent.isNotEmpty()) {
                // Retrieve the current user's username from Firestore
                val currentUser = FirebaseAuth.getInstance().currentUser

                currentUser?.let { user ->
                    val userId = user.uid

                    db.collection("usernames").document(userId)
                        .get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val username = documentSnapshot.getString("username")

                                // Create a new forum post with the retrieved username
                                val forumPost = ForumPost(
                                    username = username ?: "",
                                    content = postContent,
                                    timestamp = System.currentTimeMillis(),
                                    userEmail = FirebaseAuth.getInstance().currentUser?.email)

                                // Add the post to Firestore
                                db.collection("forum_posts")
                                    .add(forumPost)
                                    .addOnSuccessListener {
                                        // Post added successfully
                                        // Post added successfully
                                        editTextPostContent.text.clear()
                                        forumPosts.add(0, forumPost) // Adding at the beginning of the list
                                        forumPostAdapter.notifyItemInserted(0)
                                        recyclerView.scrollToPosition(0) // Scroll to the top to show the newest post
                                    }
                                    .addOnFailureListener {
                                        // Handle the error
                                    }
                            }
                        }
                        .addOnFailureListener {
                            // Handle the error
                        }
                }
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CommunityViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create a list to hold the forum posts


        // Initialize RecyclerView and its adapter
        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val currentUserUser = FirebaseAuth.getInstance().currentUser?.email ?: ""

        forumPostAdapter = ForumPostAdapter(forumPosts,currentUserUser)

        recyclerView.adapter = forumPostAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            fetchForumPosts()
        }

    }
    private suspend fun fetchForumPosts() = withContext(Dispatchers.IO) {
        try {
            val result = db.collection("forum_posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await()
            val newForumPosts = mutableListOf<ForumPost>()

            for (document in result) {
                val username = document.getString("username")
                val content = document.getString("content")
                val timestamp = document.getLong("timestamp")
                val userEmail = document.getString("userEmail")


                if (username != null && content != null && timestamp != null) {
                    newForumPosts.add(ForumPost(username, content, timestamp, userEmail))
                }
            }
            withContext(Dispatchers.Main) {
                forumPosts.clear()
                forumPosts.addAll(newForumPosts)
                forumPostAdapter.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            Log.w("Forum", "Error fetching forum posts", e)
        }
    }

}

