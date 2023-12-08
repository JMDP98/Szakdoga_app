package com.example.fomenu.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.fomenu.R
import com.example.fomenu.StepCountAdapter
import com.example.fomenu.database.StepCountEntity
import com.example.fomenu.database.StepsDatabase
import com.example.fomenu.databinding.FragmentGalleryBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log


class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private lateinit var submitButton: Button
    private lateinit var feedbackEditText: EditText
    private lateinit var stepsDatabase: StepsDatabase

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StepCountAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view =  inflater.inflate(R.layout.fragment_gallery, container, false)
        //val webView = view.findViewById<WebView>(R.id.webView)
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        submitButton = view.findViewById(R.id.submitButton)
        feedbackEditText = view.findViewById(R.id.feedbackEditText)


        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitButton.setOnClickListener { sendFeedback() }
    }

    private fun sendFeedback() {
        val feedbackText = feedbackEditText.text.toString()

        // Check if the feedback text is not empty
        if (feedbackText.isNotEmpty()) {
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("calistenicsapp1998@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "User Feedback")
                putExtra(Intent.EXTRA_TEXT, feedbackText)
            }
            startActivity(Intent.createChooser(emailIntent, "Send Feedback"))

            // Clear the EditText after sending
            feedbackEditText.text.clear()

            // Show a Toast message
            Toast.makeText(context, "Feedback sent", Toast.LENGTH_SHORT).show()
        } else {
            // Show a Toast message if the EditText is empty
            Toast.makeText(context, "Please enter your feedback", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}