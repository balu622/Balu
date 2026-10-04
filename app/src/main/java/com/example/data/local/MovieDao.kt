package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies_watchlist ORDER BY addedAt DESC")
    fun getAllWatchlist(): Flow<List<MovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM movies_watchlist WHERE id = :movieId)")
    fun isMovieBookmarked(movieId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("DELETE FROM movies_watchlist WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: String)

    @Query("DELETE FROM movies_watchlist")
    suspend fun clearWatchlist()
}
