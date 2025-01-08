package com.practicum.playlistmakerapp.create.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmakerapp.databinding.ActivityCreatePlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePlaylistBinding
    private var selectedImageUri: Uri? = null
    private val viewModel: CreatePlaylistViewModel by viewModel { parametersOf(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.createButton.setOnClickListener {
            val name = binding.playlistName.text.toString()
            val description = binding.playlistDescription.text.toString()
            val coverImagePath = saveImageToInternalStorage()

            viewModel.createPlaylist(name, description, coverImagePath)
        }
    }

    private fun observeViewModel() {
        viewModel.stateLiveData.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToInternalStorage(imageUri: Uri?): String? {
        imageUri ?: return null

        val fileName = "playlist_cover_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)

        contentResolver.openInputStream(imageUri).use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
        }

        return file.absolutePath
    }
}
