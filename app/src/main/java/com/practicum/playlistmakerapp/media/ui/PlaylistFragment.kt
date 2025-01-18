package com.practicum.playlistmakerapp.media.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.create.ui.EditPlaylistActivity
import com.practicum.playlistmakerapp.databinding.FragmentPlaylistBinding
import com.practicum.playlistmakerapp.media.ui.media.MediaActivity
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.player.ui.AudioPlayerActivity
import org.koin.android.ext.android.inject

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by inject()

    private lateinit var trackAdapter: TracksAdapter

    private lateinit var bottomSheetBehaviorForMenu: BottomSheetBehavior<View>
    private lateinit var overlay: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MediaActivity)?.binding?.bottomNavigation?.visibility = View.GONE

        binding.backbuttonToolbar.setNavigationOnClickListener {
            navigateBack()
        }

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

        binding.shareButton.setOnClickListener { viewModel.onShareButtonClicked() }

        binding.menuButton.setOnClickListener {
            binding.bottomSheetInfo.visibility = View.VISIBLE
            bottomSheetBehaviorForMenu.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.overlay.visibility = View.VISIBLE
        }

        val bottomSheetForInfo: View = binding.root.findViewById(R.id.bottom_sheet_info)
        if (bottomSheetForInfo == null) {
            Log.e("PlaylistFragment", "Bottom sheet info view not found")
        } else {
            bottomSheetBehaviorForMenu = BottomSheetBehavior.from(bottomSheetForInfo)
        }

        overlay = binding.root.findViewById(R.id.overlay)

        bottomSheetBehaviorForMenu.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.visibility = if (newState == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.share.setOnClickListener { viewModel.onShareButtonClicked() }

        binding.delete.setOnClickListener { onDeletePlaylistClicked() }

        binding.edit.setOnClickListener {
            bottomSheetBehaviorForMenu.state = BottomSheetBehavior.STATE_HIDDEN
            binding.overlay.visibility = View.GONE
            val playlist = viewModel.playlist.value
            playlist?.let {
                val intent = Intent(requireContext(), EditPlaylistActivity::class.java).apply {
                    putExtra("playlistId", it.id)
                }
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val playlistId = arguments?.getInt("playlistId") ?: 0
        viewModel.loadPlaylistById(playlistId.toLong())
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is PlaylistState.Empty -> {
                    showEmptyState()
                    showToast("В этом плейлисте нет треков")
                }
                is PlaylistState.Loaded -> showLoadedState(state.tracks, state.totalDuration)
            }
        })

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                binding.titleTV.text = it.name
                binding.title.text = it.name
                binding.descriptionTV.text = it.description
                binding.trackCountTV.text = "${it.trackCount} ${getTrackWord(it.trackCount)}"
                binding.trackCount.text = "${it.trackCount} ${getTrackWord(it.trackCount)}"

                if (it.coverImagePath.isNotEmpty()) {
                    val imageUri = Uri.parse(it.coverImagePath)
                    binding.playlistImage.setImageURI(imageUri)
                } else {
                    binding.playlistImage.setImageResource(R.drawable.placeholder)
                }
            }
        }

        viewModel.shareEvent.observe(viewLifecycleOwner) { shareText ->
            sharePlaylist(shareText)
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            showToast(message)
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

    private fun showDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить трек")
            .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Да") { dialog, _ ->
                dialog.dismiss()
                viewModel.deleteTrackFromPlaylist(track)
            }
            .show()
    }

    private fun onDeletePlaylistClicked() {
        bottomSheetBehaviorForMenu.state = BottomSheetBehavior.STATE_HIDDEN
        binding.overlay.visibility = View.GONE

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить плейлист")
            .setMessage("Хотите удалить плейлист?")
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Да") { dialog, _ ->
                dialog.dismiss()
                val playlistId = arguments?.getInt("playlistId")?.toLong() ?: return@setPositiveButton
                viewModel.deletePlaylist(playlistId) {
                    navigateBack()
                }
            }
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun sharePlaylist(shareText: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(shareIntent, "Поделиться плейлистом через"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MediaActivity)?.binding?.bottomNavigation?.visibility = View.VISIBLE
        _binding = null
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }
}