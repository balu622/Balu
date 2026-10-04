package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Movie

@Entity(tableName = "movies_watchlist")
data class MovieEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val teluguTitle: String,
    val year: String,
    val rating: Float,
    val quality: String,
    val language: String,
    val category: String,
    val posterUrl: String,
    val backdropUrl: String,
    val duration: String,
    val genre: String,
    val director: String,
    val cast: String,
    val synopsis: String,
    val teluguSynopsis: String,
    val ottPlatform: String,
    val trailerUrl: String,
    val webPath: String,
    val addedAt: Long = System.currentTimeMillis()
) {
    fun toMovie(): Movie = Movie(
        id = id,
        title = title,
        teluguTitle = teluguTitle,
        year = year,
        rating = rating,
        quality = quality,
        language = language,
        category = category,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        duration = duration,
        genre = genre,
        director = director,
        cast = cast,
        synopsis = synopsis,
        teluguSynopsis = teluguSynopsis,
        ottPlatform = ottPlatform,
        trailerUrl = trailerUrl,
        webPath = webPath,
        isBookmarked = true
    )

    companion object {
        fun fromMovie(movie: Movie): MovieEntity = MovieEntity(
            id = movie.id,
            title = movie.title,
            teluguTitle = movie.teluguTitle,
            year = movie.year,
            rating = movie.rating,
            quality = movie.quality,
            language = movie.language,
            category = movie.category,
            posterUrl = movie.posterUrl,
            backdropUrl = movie.backdropUrl,
            duration = movie.duration,
            genre = movie.genre,
            director = movie.director,
            cast = movie.cast,
            synopsis = movie.synopsis,
            teluguSynopsis = movie.teluguSynopsis,
            ottPlatform = movie.ottPlatform,
            trailerUrl = movie.trailerUrl,
            webPath = movie.webPath
        )
    }
}
