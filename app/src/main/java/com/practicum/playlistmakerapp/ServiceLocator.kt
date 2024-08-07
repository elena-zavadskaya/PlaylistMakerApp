package com.practicum.playlistmakerapp

import com.google.gson.Gson
import com.practicum.playlistmakerapp.data.impl.AudioPlayerImpl
import com.practicum.playlistmakerapp.data.impl.TrackRepositoryImpl
import com.practicum.playlistmakerapp.domain.usecases.GetTrackUseCase
import com.practicum.playlistmakerapp.domain.usecases.PauseTrackUseCase
import com.practicum.playlistmakerapp.domain.usecases.PlayTrackUseCase
import com.practicum.playlistmakerapp.domain.usecases.PrepareTrackUseCase
import com.practicum.playlistmakerapp.domain.usecases.UpdateTrackPositionUseCase
import com.practicum.playlistmakerapp.presentation.presenters.AudioPlayerPresenter

object ServiceLocator {

    private val gson: Gson by lazy { Gson() }
    private val audioPlayer: AudioPlayerImpl by lazy { AudioPlayerImpl() }
    private val trackRepository: TrackRepositoryImpl by lazy { TrackRepositoryImpl(gson, audioPlayer) }

    fun getPresenter(view: AudioPlayerPresenter.View): AudioPlayerPresenter {
        return AudioPlayerPresenter(
            GetTrackUseCase(trackRepository),
            PrepareTrackUseCase(trackRepository),
            PlayTrackUseCase(trackRepository),
            PauseTrackUseCase(trackRepository),
            UpdateTrackPositionUseCase(trackRepository),
            view
        )
    }
}