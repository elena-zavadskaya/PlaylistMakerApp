package com.practicum.playlistmakerapp.player.usecases

interface UseCase<in Params, out Type> {
    fun execute(params: Params): Type
}