package com.practicum.playlistmakerapp.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmakerapp.databinding.FragmentMediaLibraryBinding

class MediaLibraryFragment : Fragment() {

    private lateinit var binding: FragmentMediaLibraryBinding
    private lateinit var mediaLibraryPagerAdapter: MediaLibraryPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)

        setupViewPager()

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    private fun setupViewPager() {
        mediaLibraryPagerAdapter = MediaLibraryPagerAdapter(childFragmentManager, lifecycle)

        binding.viewPager.adapter = mediaLibraryPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Избранные треки"
                1 -> "Плейлисты"
                else -> ""
            }
        }.attach()
    }
}
