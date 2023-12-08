package com.example.fomenu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.storage.FirebaseStorage
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


class VideoBottomSheetFragment : BottomSheetDialogFragment() {

    private var y1: Float = 0f
    private var y2: Float = 0f

    private lateinit var viewModel: VideoBottomSheetViewModel
    private lateinit var exercise: Exercise
    private val scope = CoroutineScope(Dispatchers.IO)

    //val videoCache = VideoCache(requireContext())


    fun setExercise(exercise: Exercise) {
        this.exercise = exercise
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("VideoBottomSheetFragment", "onViewCreated called")

        /*val videoView = view.findViewById<VideoView>(R.id.exerciseVideoView)
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        scope.launch {
            try {
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference
                val videoRef = storageRef.child(exercise.videoPath)

                // Use suspend functions to perform async tasks
                val uri = videoRef.downloadUrl.await()
                val videoUrl = uri.toString()

                // Load video in background thread
                withContext(Dispatchers.Main) {
                    videoView.setVideoPath(videoUrl)
                    videoView.start()

                videoView.setOnCompletionListener { mediaPlayer ->
                    mediaPlayer.start() // Restart the video
                }
            }
            } catch (exception: Exception) {
                // Handle any errors here
            }
        }
         */

        //val aspectRatio = 16f / 9f

        val youTubePlayerView: YouTubePlayerView = view.findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

        val videoId = exercise.videoPath

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.setVolume(0)
                youTubePlayer.loadVideo(videoId, 0f)
                youTubePlayer.play()
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                super.onStateChange(youTubePlayer, state)
                if (state == PlayerConstants.PlayerState.ENDED) {
                    // Video ended, replay
                    youTubePlayer.seekTo(0f)
                    youTubePlayer.play()
                }
            }
        })

        view.setOnTouchListener { _, event ->
            Log.d("VideoBottomSheetFragment", "Touch event: ${event.action}")
            when (event.action) {
                MotionEvent.ACTION_DOWN -> y1 = event.y
                MotionEvent.ACTION_UP -> {
                    y2 = event.y
                    val deltaY = y2 - y1
                    if (deltaY > MIN_SWIPE_DISTANCE) {
                        // Downward swipe detected, close the bottom sheet
                        dismiss()
                    }
                }
            }
            true
        }
    }

    companion object {
        private const val MIN_SWIPE_DISTANCE = 150

        fun newInstance(exercise: Exercise): VideoBottomSheetFragment {
            val fragment = VideoBottomSheetFragment()
            val args = Bundle()
            args.putParcelable("exercise", exercise)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VideoBottomSheetViewModel::class.java)
        // TODO: Use the ViewModel
    }

}