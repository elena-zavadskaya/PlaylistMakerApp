package com.practicum.playlistmakerapp.media.ui.media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmakerapp.media.ui.favorites.FavoritesFragment
import com.practicum.playlistmakerapp.media.ui.playlists.PlaylistsFragment

class MediaLibraryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoritesFragment.newInstance()
            1 -> PlaylistsFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
