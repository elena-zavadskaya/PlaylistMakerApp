package com.practicum.playlistmakerapp.creator

import com.google.gson.Gson
import com.practicum.playlistmakerapp.player.AudioPlayerViewModel
import com.practicum.playlistmakerapp.player.data.impl.AudioPlayerImpl
import com.practicum.playlistmakerapp.player.data.impl.TrackRepositoryImpl
import com.practicum.playlistmakerapp.player.usecases.PauseTrackUseCase
import com.practicum.playlistmakerapp.player.usecases.PlayTrackUseCase
import com.practicum.playlistmakerapp.player.usecases.PrepareTrackUseCase
import com.practicum.playlistmakerapp.player.usecases.UpdateTrackPositionUseCase

object Creator {
    private val gson = Gson()
    private val audioPlayer = AudioPlayerImpl()
    private val trackRepository = TrackRepositoryImpl(gson, audioPlayer)

    fun provideAudioPlayerViewModel(): AudioPlayerViewModel {
        return AudioPlayerViewModel(
            prepareTrackUseCase = PrepareTrackUseCase(trackRepository),
            playTrackUseCase = PlayTrackUseCase(trackRepository),
            pauseTrackUseCase = PauseTrackUseCase(trackRepository),
            updateTrackPositionUseCase = UpdateTrackPositionUseCase(trackRepository)
        )
    }
}