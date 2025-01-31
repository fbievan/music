package com.zionhuang.music.ui.fragments

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.zionhuang.music.databinding.FragmentMiniPlayerBinding
import com.zionhuang.music.ui.activities.MainActivity
import com.zionhuang.music.viewmodels.PlaybackViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MiniPlayerFragment : Fragment() {
    private lateinit var binding: FragmentMiniPlayerBinding
    private val viewModel by activityViewModels<PlaybackViewModel>()
    private val mainActivity: MainActivity get() = requireActivity() as MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMiniPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        // Marquee
        binding.songTitle.isSelected = true
        binding.songArtist.isSelected = true

        lifecycleScope.launch {
            viewModel.playbackState.collectLatest { playbackState ->
                if (playbackState.state != STATE_NONE && playbackState.state != STATE_STOPPED) {
                    if (mainActivity.bottomSheetBehavior.state == STATE_HIDDEN) {
                        mainActivity.bottomSheetBehavior.state = STATE_COLLAPSED
                    }
                }
            }
        }
        binding.root.setOnClickListener {
            mainActivity.bottomSheetBehavior.state = STATE_EXPANDED
        }
        lifecycleScope.launch {
            viewModel.playbackState.collectLatest {
                binding.progressBar.isIndeterminate = it.state == STATE_BUFFERING
            }
        }
        lifecycleScope.launch {
            viewModel.position.collect { position ->
                binding.progressBar.progress = position.toInt()
            }
        }
        lifecycleScope.launch {
            viewModel.duration.collect { duration ->
                binding.progressBar.max = duration.toInt()
            }
        }
    }
}