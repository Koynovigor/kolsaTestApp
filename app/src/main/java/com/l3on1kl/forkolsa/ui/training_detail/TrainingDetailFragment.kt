package com.l3on1kl.forkolsa.ui.training_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.core.view.isVisible
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
                    binding.videoProgress.isVisible = when (state) {
                        Player.STATE_BUFFERING -> true
                        Player.STATE_READY, Player.STATE_ENDED, Player.STATE_IDLE -> false
                        else -> false
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    binding.videoProgress.isVisible = false
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
        with(binding) {
            playerView.controllerShowTimeoutMs = 3000  // 3 сек
            playerView.controllerAutoShow = true
        }
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
