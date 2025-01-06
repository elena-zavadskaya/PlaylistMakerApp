package com.practicum.playlistmakerapp.search.di

import android.content.Context
import com.practicum.playlistmakerapp.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmakerapp.search.data.network.ITunesApi
import com.practicum.playlistmakerapp.search.data.network.NetworkClient
import com.practicum.playlistmakerapp.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmakerapp.search.data.network.SearchRepositoryImpl
import com.practicum.playlistmakerapp.search.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmakerapp.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmakerapp.search.domain.api.SearchInteractor
import com.practicum.playlistmakerapp.search.domain.api.SearchRepository
import com.practicum.playlistmakerapp.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmakerapp.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmakerapp.search.ui.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(ITunesApi::class.java)
    }

    single<NetworkClient> { RetrofitNetworkClient(get()) }

    single<SearchRepository> { SearchRepositoryImpl(get()) }

    single<SearchInteractor> { SearchInteractorImpl(get()) }

    single { androidContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }

    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }

    single<SearchHistoryInteractor> { SearchHistoryInteractorImpl(get()) }

    viewModel { SearchViewModel(get(), get(), get()) }
}
