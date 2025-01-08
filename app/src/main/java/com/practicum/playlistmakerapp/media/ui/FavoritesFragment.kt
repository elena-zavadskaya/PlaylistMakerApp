package com.practicum.playlistmakerapp.media.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmakerapp.databinding.FragmentFavoritesBinding
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.player.ui.AudioPlayerActivity
import com.practicum.playlistmakerapp.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModel()

    private lateinit var trackAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        trackAdapter = TrackAdapter(emptyList()) { track -> onTrackClick(track) }
        binding.recyclerView.adapter = trackAdapter

        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is FavoritesState.Empty -> showEmptyState()
                is FavoritesState.Loaded -> showLoadedState(state.tracks)
            }
        })
    }

    private fun showEmptyState() {
        binding.recyclerView.visibility = View.GONE
        binding.notFoundImage.visibility = View.VISIBLE
        binding.notFoundText.visibility = View.VISIBLE
    }

    private fun showLoadedState(tracks: List<Track>) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.notFoundImage.visibility = View.GONE
        binding.notFoundText.visibility = View.GONE
        trackAdapter.updateTracks(tracks)
    }

    private fun onTrackClick(track: Track) {
        val json = Gson().toJson(track)
        activity?.let { context ->
            Intent(context, AudioPlayerActivity::class.java).apply {
                putExtra("KEY", json)
                context.startActivity(this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}