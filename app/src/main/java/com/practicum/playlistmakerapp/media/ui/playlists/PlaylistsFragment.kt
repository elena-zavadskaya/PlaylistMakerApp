package com.practicum.playlistmakerapp.media.ui.playlists

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.create.ui.CreatePlaylistActivity
import com.practicum.playlistmakerapp.databinding.FragmentPlaylistsBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlaylistsAdapter(emptyList()) { playlistId ->
            val bundle = Bundle().apply {
                putInt("playlistId", playlistId)
            }
            findNavController().navigate(R.id.playlistFragment, bundle)
        }
        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.newPlaylistButton.setOnClickListener {
            val intent = Intent(requireContext(), CreatePlaylistActivity::class.java)
            startActivity(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playlists.collect { playlists ->
                if (playlists.isEmpty()) {
                    showEmptyState()
                } else {
                    showPlaylists()
                    adapter.playlists = playlists
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showEmptyState() = with(binding) {
        recyclerView.visibility = View.GONE
        emptyStateLayout.visibility = View.VISIBLE
    }

    private fun showPlaylists() = with(binding) {
        recyclerView.visibility = View.VISIBLE
        emptyStateLayout.visibility = View.GONE
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}