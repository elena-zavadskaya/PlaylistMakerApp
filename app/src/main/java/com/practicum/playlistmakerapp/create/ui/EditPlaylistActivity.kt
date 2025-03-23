package com.practicum.playlistmakerapp.create.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import org.koin.android.ext.android.inject

class EditPlaylistActivity : CreatePlaylistActivity() {

    override val viewModel: EditPlaylistViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playlistId = intent.getLongExtra("playlistId", -1)
        if (playlistId == -1L) {
            Toast.makeText(this, "Ошибка: ID плейлиста отсутствует", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.setPlaylistId(playlistId)

        binding.createButton.text = "Сохранить"
        binding.backbuttonToolbar.title= "Редактировать плейлист"

        loadPlaylistData(playlistId)
    }

    private fun loadPlaylistData(playlistId: Long) {
        viewModel.getPlaylistById(playlistId).observe(this) { playlist ->
            if (playlist != null) {
                binding.playlistName.setText(playlist.name)
                binding.playlistDescription.setText(playlist.description)

                if (playlist.coverImagePath.isNotEmpty()) {
                    binding.storageImage.apply {
                        setImageURI(playlist.coverImagePath.toUri())
                        visibility = View.VISIBLE
                    }
                    binding.playlistImage.visibility = View.GONE
                } else {
                    binding.storageImage.visibility = View.GONE
                    binding.playlistImage.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(this, "Ошибка: плейлист не найден", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun showExitConfirmationDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Завершить редактирование плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setPositiveButton("Завершить") { _, _ -> finish() }
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.show()
    }
}