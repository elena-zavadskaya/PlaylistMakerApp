package com.practicum.playlistmakerapp.search.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmakerapp.databinding.FragmentSearchBinding
import com.practicum.playlistmakerapp.player.domain.models.Track
import com.practicum.playlistmakerapp.player.ui.AudioPlayerActivity
import com.practicum.playlistmakerapp.search.TracksState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModel()

    private val adapter = TrackAdapter(emptyList()) { track ->
        viewModel.handleTrackClick(track) { clickedTrack ->
            val json = Gson().toJson(clickedTrack)
            activity?.let { context ->
                Intent(context, AudioPlayerActivity::class.java).apply {
                    putExtra("KEY", json)
                    context.startActivity(this)
                }
            }
        }
    }

    private val historyAdapter = TrackAdapter(emptyList()) { track ->
        viewModel.handleTrackClick(track) { clickedTrack ->
            val json = Gson().toJson(clickedTrack)
            activity?.let { context ->
                Intent(context, AudioPlayerActivity::class.java).apply {
                    putExtra("KEY", json)
                    context.startActivity(this)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerViews()
        setupListeners()

        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        viewModel.observeHistory().observe(viewLifecycleOwner) { showSearchHistory(it) }
    }

    private fun setupToolbar() {
        binding.searchBackbuttonToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerViews() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.searchHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchHistoryRecyclerView.adapter = historyAdapter
    }

    private fun setupListeners() {
        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty()) {
                viewModel.showSearchHistory()
            }
        }

        binding.inputEditText.addTextChangedListener { s ->
            viewModel.searchDebounce(s?.toString() ?: "")
            binding.clearIcon.isVisible = !s.isNullOrEmpty()
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.text.clear()
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearSearchHistory()
        }
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Content -> {
                adapter.updateTracks(state.tracks)
                showTrackList()
            }
            is TracksState.Empty -> showNotFoundPage()
            is TracksState.Error -> showInternetErrorPage()
            is TracksState.Loading -> showLoadingPage()
        }
    }

    private fun showSearchHistory(history: List<Track>) {
        if (history.isNotEmpty()) {
            binding.searchHistory.isVisible = true
            historyAdapter.updateTracks(history)
            binding.recyclerView.isVisible = false
            binding.progressBar.isVisible = false
            binding.notFound.isVisible = false
        } else {
            binding.searchHistory.isVisible = false
        }
    }

    private fun showLoadingPage() {
        binding.searchHistory.isVisible = false
        binding.recyclerView.isVisible = false
        binding.progressBar.isVisible = true
        binding.notFound.isVisible = false
        binding.internetError.isVisible = false
    }

    private fun showTrackList() {
        binding.searchHistory.isVisible = false
        binding.recyclerView.isVisible = true
        binding.progressBar.isVisible = false
        binding.notFound.isVisible = false
        binding.internetError.isVisible = false
    }

    private fun showNotFoundPage() {
        binding.searchHistory.isVisible = false
        binding.recyclerView.isVisible = false
        binding.progressBar.isVisible = false
        binding.notFound.isVisible = true
        binding.internetError.isVisible = false
    }

    private fun showInternetErrorPage() {
        binding.searchHistory.isVisible = false
        binding.recyclerView.isVisible = false
        binding.progressBar.isVisible = false
        binding.notFound.isVisible = false
        binding.internetError.isVisible = true
    }
}
