package com.l3on1kl.forkolsa.ui.video

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SharedPlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var _player: ExoPlayer? = null
    val player: ExoPlayer
        get() = _player ?: ExoPlayer.Builder(context).build().also { _player = it }

    var videoUrl: String? = null

    override fun onCleared() {
        _player?.release()
        _player = null
        videoUrl = null
        super.onCleared()
    }
}
