package com.practicum.playlistmakerapp.creator

import com.google.gson.Gson
import com.practicum.playlistmakerapp.player.AudioPlayerViewModel
import com.practicum.playlistmakerapp.player.data.impl.AudioPlayerImpl
import com.practicum.playlistmakerapp.player.data.impl.TrackRepositoryImpl
import com.practicum.playlistmakerapp.player.usecases.PauseTrackUseCase
import com.practicum.playlistmakerapp.player.usecases.PlayTrackUseCase
import com.practicum.playlistmakerapp.player.usecases.PrepareTrackUseCase
import com.practicum.playlistmakerapp.player.usecases.UpdateTrackPositionUseCase
import com.practicum.playlistmakerapp.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmakerapp.search.data.network.TracksRepositoryImpl
import com.practicum.playlistmakerapp.search.domain.api.TracksInteractor
import com.practicum.playlistmakerapp.search.domain.api.TracksRepository
import com.practicum.playlistmakerapp.search.domain.impl.TracksInteractorImpl

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

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}