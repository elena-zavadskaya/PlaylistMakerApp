package com.practicum.playlistmakerapp.create.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import androidx.core.net.toUri
import com.google.gson.Gson
import com.practicum.playlistmakerapp.create.data.db.PlaylistDao
import com.practicum.playlistmakerapp.create.data.db.PlaylistEntity
import com.practicum.playlistmakerapp.create.data.db.PlaylistTrackDao
import com.practicum.playlistmakerapp.create.data.db.PlaylistTrackEntity
import com.practicum.playlistmakerapp.create.domain.repository.CreatePlaylistRepository
import com.practicum.playlistmakerapp.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistTrackDao: PlaylistTrackDao,
    private val context: Context
) : CreatePlaylistRepository {

    // Методы для плейлистов
    override suspend fun savePlaylist(playlist: PlaylistEntity) {
        playlistDao.insertPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: PlaylistEntity) {
        playlistDao.updatePlaylist(playlist)
    }

    override suspend fun getPlaylistById(id: Long): PlaylistEntity? {
        return playlistDao.getPlaylistById(id)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistEntity>> {
        return playlistDao.getAllPlaylists()
    }

    override suspend fun addTrackToPlaylist(track: PlaylistTrackEntity) {
        playlistTrackDao.insertTrackToPlaylist(track)
    }

    // Методы для треков
    override suspend fun getTrackById(id: String): PlaylistTrackEntity? {
        return playlistTrackDao.getTrackById(id)
    }

    override suspend fun getAllTracks(): List<PlaylistTrackEntity> {
        return playlistTrackDao.getAllTracks()
    }

    override suspend fun getTracksByIds(ids: List<String>): List<Track> {
        val playlistTrackEntities = playlistTrackDao.getTracksByIds(ids)
        return playlistTrackEntities.map { it.toTrack() }
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String) {
        val playlists = playlistDao.getAllPlaylists().first()
        playlists.forEach { playlist ->
            val trackIds = Gson().fromJson(playlist.trackIds, Array<String>::class.java).toMutableList()
            if (trackIds.contains(trackId)) {
                trackIds.remove(trackId)
                playlistDao.updatePlaylist(
                    playlist.copy(trackIds = Gson().toJson(trackIds), trackCount = trackIds.size)
                )
            }
        }

        val isTrackInUse = playlists.any { playlist ->
            val trackIds = Gson().fromJson(playlist.trackIds, Array<String>::class.java)
            trackIds.contains(trackId)
        }

        if (!isTrackInUse) {
            playlistTrackDao.deleteTrack(trackId)
        }
    }

    override suspend fun saveImageToStorage(uri: Uri, fileName: String): Uri {
        val storageDir = File(context.getExternalFilesDir(null), "playlist_covers")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val file = File(storageDir, fileName)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                val bitmap = BitmapFactory.decodeStream(input)
                val roundedBitmap = createRoundedBitmap(bitmap)
                roundedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, output)
            }
        }

        return file.toUri()
    }

    private fun createRoundedBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawRoundRect(rectF, 16f, 16f, paint)

        paint.xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }
}

fun PlaylistTrackEntity.toTrack(): Track {
    return Track(
        trackId = id,
        trackName = title,
        artistName = artist,
        trackTimeMillis = duration,
        artworkUrl100 = coverUrl,
        collectionName = album.orEmpty(),
        releaseDate = releaseYear.toString(),
        primaryGenreName = genre,
        country = country,
        previewUrl = fileUrl
    )
}
