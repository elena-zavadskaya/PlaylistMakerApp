package com.practicum.playlistmakerapp.media.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.FragmentPlaylistBinding
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.player.ui.AudioPlayerActivity
import org.koin.android.ext.android.inject

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by inject()

    private lateinit var trackAdapter: TracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tracksRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        trackAdapter = TracksAdapter(
            emptyList(),
            onTrackClick = { track -> onTrackClick(track) },
            onTrackLongClick = { track -> showDeleteTrackDialog(track) }
        )

        binding.tracksRecyclerView.adapter = trackAdapter

        setupObservers()

        val playlistId = arguments?.getInt("playlistId") ?: 0
        viewModel.loadPlaylistById(playlistId.toLong())
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is PlaylistState.Empty -> showEmptyState()
                is PlaylistState.Loaded -> showLoadedState(state.tracks, state.totalDuration)
            }
        })

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
    }

    private fun showEmptyState() {
        binding.tracksRecyclerView.visibility = View.GONE
    }

    private fun showLoadedState(tracks: List<Track>, totalDuration: String) {
        binding.tracksRecyclerView.visibility = View.VISIBLE
        binding.timeTV.visibility = View.VISIBLE
        binding.timeTV.text = totalDuration
        trackAdapter.updateTracks(tracks)
    }

    private fun getTrackWord(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "трек"
            count % 10 in 2..4 && (count % 100 !in 12..14) -> "трека"
            else -> "треков"
        }
    }

    private fun onTrackClick(track: Track) {
        val updatedTrack = track.copy(
            trackTimeMillis = convertTimeToMillis(track.trackTimeMillis.toString()).toString()
        )
        val intent = Intent(requireContext(), AudioPlayerActivity::class.java).apply {
            putExtra("KEY", Gson().toJson(updatedTrack))
        }
        startActivity(intent)
    }

    private fun convertTimeToMillis(time: String): Long {
        val parts = time.split(":")
        return if (parts.size == 2) {
            val minutes = parts[0].toIntOrNull() ?: 0
            val seconds = parts[1].toIntOrNull() ?: 0
            (minutes * 60 + seconds) * 1000L
        } else {
            0L
        }
    }

    private fun setupAdapter() {
        trackAdapter = TracksAdapter(
            tracks = emptyList(),
            onTrackClick = { track -> onTrackClick(track) },
            onTrackLongClick = { track -> showDeleteTrackDialog(track) }
        )
        binding.tracksRecyclerView.adapter = trackAdapter
    }

    private fun showDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить трек")
            .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Удалить") { dialog, _ ->
                dialog.dismiss()
                viewModel.deleteTrackFromPlaylist(track)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}