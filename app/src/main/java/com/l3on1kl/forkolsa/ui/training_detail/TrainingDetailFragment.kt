package com.l3on1kl.forkolsa.ui.training_detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.l3on1kl.forkolsa.R
import com.l3on1kl.forkolsa.databinding.FragmentTrainingDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrainingDetailFragment : Fragment() {

    private var _binding: FragmentTrainingDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrainingDetailViewModel by viewModels()
    private var player: ExoPlayer? = null

    private val args: TrainingDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.title.text = args.title
        binding.description.text = args.description
        binding.duration.text = resources.getQuantityString(
            R.plurals.minutes_count,
            args.duration,
            args.duration
        )
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is TrainingDetailViewModel.DetailUiState.Loading -> {
                    }

                    is TrainingDetailViewModel.DetailUiState.Success -> {
                        initPlayer(state.video.link)
                    }

                    is TrainingDetailViewModel.DetailUiState.Error -> {
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }


    }

    @OptIn(UnstableApi::class)
    private fun initPlayer(url: String) {
        val videoUrl = if (url.startsWith("http", ignoreCase = true)) {
            url
        } else {
            "https://ref.test.kolsa.ru$url"
        }

        player?.release()
        player = ExoPlayer.Builder(requireContext()).build().also { exo ->
            binding.playerView.player = exo
            val mediaItem = MediaItem.Builder()
                .setUri(videoUrl)
                .build()
            exo.setMediaItem(mediaItem)
            exo.prepare()
            exo.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {

                }

                override fun onPlayerError(error: PlaybackException) {

                    Snackbar.make(
                        binding.root,
                        "${error.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                override fun onVideoSizeChanged(videoSize: VideoSize) {
                    val width = videoSize.width
                    val height = videoSize.height
                    if (width > 0 && height > 0) {
                        val aspectRatio = height.toFloat() / width
                        binding.playerView.post {
                            val viewWidth = binding.playerView.width
                            val layoutParams = binding.playerView.layoutParams
                            layoutParams.height = (viewWidth * aspectRatio).toInt()
                            binding.playerView.layoutParams = layoutParams
                        }
                    }
                }
            })
        }

        binding.playerView.setFullscreenButtonClickListener {
            toggleFullscreen()
        }

        with(binding) {
            playerView.controllerShowTimeoutMs = 3000
            playerView.controllerAutoShow = true
        }

    }

    private var isFullscreen = false

    private fun toggleFullscreen() {
        val window = requireActivity().window
        val decorView = window.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ (API 30+)
            val controller = window.insetsController ?: return
            if (!isFullscreen) {
                controller.hide(WindowInsets.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                controller.show(WindowInsets.Type.systemBars())
            }
        } else {
            // Android 9–10 (API 28–29)
            if (!isFullscreen) {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        )
            } else {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }

        val activity = requireActivity() as AppCompatActivity
        if (!isFullscreen) {
            activity.supportActionBar?.hide()
        } else {
            activity.supportActionBar?.show()
        }

        isFullscreen = !isFullscreen
    }


    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroyView() {
        player?.release()
        player = null
        _binding = null
        super.onDestroyView()
    }
}
