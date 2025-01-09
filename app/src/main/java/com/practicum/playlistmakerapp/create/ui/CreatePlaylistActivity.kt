package com.practicum.playlistmakerapp.create.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.ActivityCreatePlaylistBinding
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePlaylistBinding
    private var selectedImageUri: Uri? = null
    private val viewModel: CreatePlaylistViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playlistImage.visibility = View.VISIBLE
        binding.storageImage.visibility = View.GONE

        binding.backbuttonToolbar.setNavigationOnClickListener {
            handleBackPress()
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Выбранный URI: $uri")
                saveImageToPrivateStorage(uri)
                binding.playlistImage.visibility = View.GONE
                binding.storageImage.visibility = View.VISIBLE
            } else {
                Log.d("PhotoPicker", "Ничего не выбрано")
            }
        }

        binding.playlistImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
            val name = binding.playlistName.text.toString().trim()
            val description = binding.playlistDescription.text.toString().trim()
            val coverImagePath = selectedImageUri?.path

            if (name.isBlank()) {
                Toast.makeText(this, "Название плейлиста не может быть пустым", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createPlaylist(name, description, coverImagePath)
        }

    }

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

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_covers")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, "cover_${System.currentTimeMillis()}.jpg")
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                BitmapFactory.decodeStream(input).compress(Bitmap.CompressFormat.JPEG, 80, output)
            }
        }

        binding.storageImage.setImageURI(file.toUri())
        Log.d("PhotoPicker", "Изображение сохранено в: ${file.absolutePath}")
    }
}