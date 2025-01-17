package com.practicum.playlistmakerapp.create.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.practicum.playlistmakerapp.R
import com.practicum.playlistmakerapp.databinding.ActivityCreatePlaylistBinding
import org.koin.android.ext.android.inject

open class CreatePlaylistActivity : AppCompatActivity() {

    protected open lateinit var binding: ActivityCreatePlaylistBinding
    private var selectedImageUri: Uri? = null
    protected open val viewModel: CreatePlaylistViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playlistImage.visibility = View.VISIBLE
        binding.storageImage.visibility = View.GONE

        binding.backbuttonToolbar.setNavigationOnClickListener {
            handleBackPress()
        }

        binding.playlistName.setOnFocusChangeListener { _, hasFocus ->
            binding.clueName.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }

        binding.playlistDescription.setOnFocusChangeListener { _, hasFocus ->
            binding.clueDescription.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                binding.storageImage.setImageURI(selectedImageUri)
                binding.playlistImage.visibility = View.GONE
                binding.storageImage.visibility = View.VISIBLE
            }
        }

        binding.playlistImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.playlistName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateEditTextBorderColor(binding.playlistName, !s.isNullOrEmpty())
            }
            override fun afterTextChanged(s: Editable?) {
                val isNameFilled = !s.isNullOrBlank()
                binding.createButton.isEnabled = isNameFilled
                val buttonColor = if (isNameFilled) R.color.blue else R.color.dark_gray
                binding.createButton.backgroundTintList = ContextCompat.getColorStateList(this@CreatePlaylistActivity, buttonColor)
            }
        })

        binding.playlistDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateEditTextBorderColor(binding.playlistDescription, !s.isNullOrEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        viewModel.isCreateButtonEnabled.observe(this, Observer { isEnabled ->
            binding.createButton.isEnabled = isEnabled
            val colorRes = if (isEnabled) R.color.blue else R.color.another_text
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
            val coverImagePath = selectedImageUri

            if (name.isBlank()) {
                Toast.makeText(this, "Название плейлиста не может быть пустым", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createPlaylist(name, description, coverImagePath)
        }

    }

    override fun onBackPressed() {
        handleBackPress()
    }

    protected open fun handleBackPress() {
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

    private fun updateEditTextBorderColor(editText: EditText, hasText: Boolean) {
        val colorResId = if (hasText) R.drawable.edit_text_border_filled else R.drawable.edit_text_border_empty
        editText.setBackgroundResource(colorResId)
    }
}