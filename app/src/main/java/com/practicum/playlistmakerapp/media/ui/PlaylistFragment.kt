package com.practicum.playlistmakerapp.media.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.FragmentPlaylistBinding
import com.practicum.playlistmakerapp.media.ui.media.MediaActivity
import org.koin.android.ext.android.inject

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backbuttonToolbar.setNavigationOnClickListener {
            navigateBack()
        }

        val playlistId = arguments?.getInt("playlistId") ?: 0
        viewModel.loadPlaylistById(playlistId.toLong())

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                binding.titleTV.text = it.name
                binding.descriptionTV.text = it.description
                binding.trackCountTV.text = "${it.trackCount} ${getTrackWord(it.trackCount)}"

                if (it.coverImagePath.isNotEmpty()) {
                    val imageUri = Uri.parse(it.coverImagePath)
                    binding.playlistImage.setImageURI(imageUri)
                } else {
                    binding.playlistImage.setImageResource(R.drawable.placeholder)
                }
            }
        }

        viewModel.totalDuration.observe(viewLifecycleOwner) { totalDuration ->
            binding.timeTV.text = totalDuration
        }
    }

    private fun getTrackWord(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "трек"
            count % 10 in 2..4 && (count % 100 !in 12..14) -> "трека"
            else -> "треков"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MediaActivity).binding.bottomNavigation.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as MediaActivity).binding.bottomNavigation.visibility = View.VISIBLE
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }
}