package com.practicum.playlistmakerapp.player.domain.usecases

interface UseCase<in Params, out Type> {
    fun execute(params: Params): Type
}