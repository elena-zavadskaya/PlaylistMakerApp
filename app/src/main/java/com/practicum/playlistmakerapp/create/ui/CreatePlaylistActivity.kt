package com.practicum.playlistmakerapp.create.ui

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.ActivityCreatePlaylistBinding
import org.koin.android.ext.android.inject

class CreatePlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePlaylistBinding
    private var selectedImageUri: Uri? = null
    private val pickImageRequestCode = 1001
    private val viewModel: CreatePlaylistViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbuttonToolbar.setNavigationOnClickListener {
            handleBackPress()
        }

        binding.playlistImage.setOnClickListener {
            openGallery()
        }

        binding.playlistName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.onPlaylistNameChanged(s.toString())
            }
        })

        viewModel.isCreateButtonEnabled.observe(this, Observer { isEnabled ->
            binding.createButton.isEnabled = isEnabled
            val colorRes = if (isEnabled) R.color.blue else R.color.dark_gray
            binding.createButton.setBackgroundTintList(ContextCompat.getColorStateList(this, colorRes))
        })

        viewModel.toastMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            if (message.startsWith("Плейлист")) {
                finish()
            }
        })

        binding.createButton.setOnClickListener {
            val name = binding.playlistName.text.toString()
            val description = binding.playlistDescription.text.toString()
            viewModel.createPlaylist(name, description, selectedImageUri) { uri ->
                contentResolver.openInputStream(uri)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, pickImageRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequestCode && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let { uri ->
                binding.playlistImage.background = uriToDrawable(uri)
            }
        }
    }

    private fun uriToDrawable(uri: Uri) = Drawable.createFromStream(contentResolver.openInputStream(uri), uri.toString())

    private fun handleBackPress() {
        if (hasUnsavedChanges()) {
            showExitConfirmationDialog()
        } else {
            finish()
        }
    }

    private fun showExitConfirmationDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setPositiveButton("Завершить") { _, _ -> finish() }
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.show()
    }

    private fun hasUnsavedChanges(): Boolean {
        val isImageSet = selectedImageUri != null
        val isNameFilled = !binding.playlistName.text.isNullOrBlank()
        val isDescriptionFilled = !binding.playlistDescription.text.isNullOrBlank()
        return isImageSet || isNameFilled || isDescriptionFilled
    }
}
