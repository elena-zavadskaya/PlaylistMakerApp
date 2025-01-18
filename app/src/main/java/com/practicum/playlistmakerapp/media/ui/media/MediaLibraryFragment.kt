package com.practicum.playlistmakerapp.media.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.FragmentMediaLibraryBinding

class MediaLibraryFragment : Fragment() {

    private var _binding: FragmentMediaLibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediaLibraryPagerAdapter: MediaLibraryPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)

        setupViewPager()

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return binding.root
    }

    private fun setupViewPager() {
        mediaLibraryPagerAdapter = MediaLibraryPagerAdapter(childFragmentManager, lifecycle)

        binding.viewPager.adapter = mediaLibraryPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_favorites)
                1 -> getString(R.string.tab_playlists)
                else -> ""
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}