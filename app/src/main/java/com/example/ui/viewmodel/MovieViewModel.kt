package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.Movie
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    MOVIES,
    PORTAL,
    WATCHLIST,
    SETTINGS
}

data class MovieUiState(
    val currentTab: AppTab = AppTab.MOVIES,
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val selectedMovie: Movie? = null,
    val portalUrl: String = "https://www.5movierulz.works/",
    val activeMirror: String = "https://www.5movierulz.works/",
    val isTeluguLanguage: Boolean = true,
    val isAdBlockEnabled: Boolean = true,
    val isDesktopMode: Boolean = false,
    val isWebLoading: Boolean = false,
    val webProgress: Int = 0,
    val webTitle: String = "5MovieRulz Works",
    val canGoBackWeb: Boolean = false,
    val canGoForwardWeb: Boolean = false
)

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = MovieRepository(database.movieDao())
    }

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState

    val watchlist: StateFlow<List<Movie>> = repository.watchlistMovies
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val movies: StateFlow<List<Movie>> = combine(
        _uiState,
        watchlist
    ) { state, savedList ->
        val savedIds = savedList.map { it.id }.toSet()
        val allCurated = repository.getCatalog().map { m ->
            m.copy(isBookmarked = savedIds.contains(m.id))
        }

        allCurated.filter { movie ->
            val matchesCategory = when (state.selectedCategory) {
                "All" -> true
                "Featured" -> movie.isFeatured || movie.category == "Featured"
                else -> movie.category.equals(state.selectedCategory, ignoreCase = true)
            }

            val q = state.searchQuery.trim().lowercase()
            val matchesSearch = if (q.isEmpty()) true else {
                movie.title.lowercase().contains(q) ||
                        movie.teluguTitle.lowercase().contains(q) ||
                        movie.cast.lowercase().contains(q) ||
                        movie.director.lowercase().contains(q) ||
                        movie.genre.lowercase().contains(q) ||
                        movie.year.contains(q)
            }

            matchesCategory && matchesSearch
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.getCatalog()
    )

    fun setTab(tab: AppTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun selectMovie(movie: Movie?) {
        _uiState.value = _uiState.value.copy(selectedMovie = movie)
    }

    fun toggleBookmark(movie: Movie) {
        viewModelScope.launch {
            repository.toggleBookmark(movie)
        }
    }

    fun removeBookmark(movieId: String) {
        viewModelScope.launch {
            repository.removeBookmark(movieId)
        }
    }

    fun setPortalUrl(url: String) {
        _uiState.value = _uiState.value.copy(portalUrl = url)
    }

    fun setMirror(mirrorUrl: String) {
        _uiState.value = _uiState.value.copy(
            activeMirror = mirrorUrl,
            portalUrl = mirrorUrl
        )
    }

    fun openMovieInPortal(movie: Movie) {
        val baseUrl = _uiState.value.activeMirror.removeSuffix("/")
        val targetUrl = "$baseUrl${movie.webPath}"
        _uiState.value = _uiState.value.copy(
            currentTab = AppTab.PORTAL,
            portalUrl = targetUrl,
            selectedMovie = null
        )
    }

    fun toggleLanguage() {
        _uiState.value = _uiState.value.copy(isTeluguLanguage = !_uiState.value.isTeluguLanguage)
    }

    fun toggleAdBlock() {
        _uiState.value = _uiState.value.copy(isAdBlockEnabled = !_uiState.value.isAdBlockEnabled)
    }

    fun toggleDesktopMode() {
        _uiState.value = _uiState.value.copy(isDesktopMode = !_uiState.value.isDesktopMode)
    }

    fun updateWebStatus(loading: Boolean, progress: Int, title: String? = null, canBack: Boolean = false, canFwd: Boolean = false) {
        _uiState.value = _uiState.value.copy(
            isWebLoading = loading,
            webProgress = progress,
            webTitle = title ?: _uiState.value.webTitle,
            canGoBackWeb = canBack,
            canGoForwardWeb = canFwd
        )
    }
}
