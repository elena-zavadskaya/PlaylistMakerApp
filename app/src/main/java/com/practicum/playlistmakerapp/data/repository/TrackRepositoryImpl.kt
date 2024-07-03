package com.practicum.playlistmakerapp.data.repository

import com.google.gson.Gson
import com.practicum.playlistmakerapp.data.api.ITunesApi
import com.practicum.playlistmakerapp.domain.repository.TrackRepository
import com.practicum.playlistmakerapp.domain.models.Track
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Реализацию репозитория для работы с треками. Отвечает за получение данных из внешнего API.
class TrackRepositoryImpl(private val gson: Gson, private val audioPlayer: AudioPlayerImpl) : TrackRepository {

    private val api: ITunesApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(ITunesApi::class.java)
    }

    override fun getTrack(trackId: String): Track {
        return gson.fromJson(trackId, Track::class.java)
    }

    override fun prepareTrack(url: String, onPrepared: () -> Unit, onError: () -> Unit) {
        audioPlayer.prepare(url, onPrepared, onError)
    }

    override fun playTrack() {
        audioPlayer.play()
    }

    override fun pauseTrack() {
        audioPlayer.pause()
    }

    override fun getTrackPosition(): Int {
        return audioPlayer.getCurrentPosition()
    }
}