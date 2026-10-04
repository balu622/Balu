package com.example.data.model

data class Movie(
    val id: String,
    val title: String,
    val teluguTitle: String,
    val year: String,
    val rating: Float,
    val quality: String, // "4K UHD", "1080p FHD", "720p HD", "HDRip"
    val language: String, // "Telugu", "Tamil", "Hindi", "Malayalam", "English"
    val category: String, // "Telugu Latest", "Featured", "Dubbed", "OTT Premieres", "Classics"
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
    val webPath: String, // Path on 5movierulz website
    val isTrending: Boolean = false,
    val isFeatured: Boolean = false,
    val isBookmarked: Boolean = false
)
