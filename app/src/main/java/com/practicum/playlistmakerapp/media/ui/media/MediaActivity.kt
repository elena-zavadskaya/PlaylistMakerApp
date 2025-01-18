package com.practicum.playlistmakerapp.media.ui.media

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {

    lateinit var binding: ActivityMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        navHostFragment?.let {
            val navController = it.navController
            binding.bottomNavigation.setupWithNavController(navController)
        }
    }
}
