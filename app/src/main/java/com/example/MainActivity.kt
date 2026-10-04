package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.MovieDetailsDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PortalScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CinemaBackground
import com.example.ui.theme.CinemaBorder
import com.example.ui.theme.CinemaSurface
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MovieRulzApp()
            }
        }
    }
}

@Composable
fun MovieRulzApp(viewModel: MovieViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movies by viewModel.movies.collectAsStateWithLifecycle()
    val watchlist by viewModel.watchlist.collectAsStateWithLifecycle()

    val isTelugu = uiState.isTeluguLanguage

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CinemaBackground,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .border(androidx.compose.foundation.BorderStroke(0.8.dp, CinemaBorder))
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar"),
                containerColor = CinemaSurface,
                tonalElevation = 8.dp
            ) {
                // Movies Tab
                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.MOVIES,
                    onClick = { viewModel.setTab(AppTab.MOVIES) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentTab == AppTab.MOVIES) Icons.Filled.Movie else Icons.Outlined.Movie,
                            contentDescription = "Movies",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = if (isTelugu) "సినిమాలు" else "Movies",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.currentTab == AppTab.MOVIES) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = CyanPrimary,
                        indicatorColor = CyanPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_movies_tab")
                )

                // Portal Web Tab
                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.PORTAL,
                    onClick = { viewModel.setTab(AppTab.PORTAL) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentTab == AppTab.PORTAL) Icons.Filled.Language else Icons.Outlined.Language,
                            contentDescription = "Portal",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = if (isTelugu) "వెబ్‌సైట్" else "Portal",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.currentTab == AppTab.PORTAL) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = CyanPrimary,
                        indicatorColor = CyanPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_portal_tab")
                )

                // Watchlist Tab
                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.WATCHLIST,
                    onClick = { viewModel.setTab(AppTab.WATCHLIST) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (watchlist.isNotEmpty()) {
                                    Badge(
                                        containerColor = AmberGold,
                                        contentColor = Color.Black
                                    ) {
                                        Text(
                                            text = "${watchlist.size}",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (uiState.currentTab == AppTab.WATCHLIST) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Watchlist",
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = if (isTelugu) "వాచ్‌లిస్ట్" else "Watchlist",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.currentTab == AppTab.WATCHLIST) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = CyanPrimary,
                        indicatorColor = CyanPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_watchlist_tab")
                )

                // Settings Tab
                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.SETTINGS,
                    onClick = { viewModel.setTab(AppTab.SETTINGS) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentTab == AppTab.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = if (isTelugu) "సెట్టింగ్స్" else "Settings",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.currentTab == AppTab.SETTINGS) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = CyanPrimary,
                        indicatorColor = CyanPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_settings_tab")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CinemaBackground)
        ) {
            when (uiState.currentTab) {
                AppTab.MOVIES -> HomeScreen(
                    uiState = uiState,
                    movies = movies,
                    onSelectMovie = { viewModel.selectMovie(it) },
                    onToggleBookmark = { viewModel.toggleBookmark(it) },
                    onCategorySelected = { viewModel.setCategory(it) },
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    onOpenPortal = { viewModel.openMovieInPortal(it) },
                    onToggleLanguage = { viewModel.toggleLanguage() }
                )

                AppTab.PORTAL -> PortalScreen(
                    uiState = uiState,
                    onUrlChange = { viewModel.setPortalUrl(it) },
                    onMirrorSelect = { viewModel.setMirror(it) },
                    onToggleAdBlock = { viewModel.toggleAdBlock() },
                    onToggleDesktop = { viewModel.toggleDesktopMode() },
                    onWebStatusChange = { loading, progress, title, canBack, canFwd ->
                        viewModel.updateWebStatus(loading, progress, title, canBack, canFwd)
                    }
                )

                AppTab.WATCHLIST -> WatchlistScreen(
                    watchlist = watchlist,
                    isTelugu = isTelugu,
                    onSelectMovie = { viewModel.selectMovie(it) },
                    onRemoveBookmark = { viewModel.removeBookmark(it) },
                    onExploreMovies = { viewModel.setTab(AppTab.MOVIES) }
                )

                AppTab.SETTINGS -> SettingsScreen(
                    uiState = uiState,
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    onMirrorSelect = { viewModel.setMirror(it) },
                    onToggleAdBlock = { viewModel.toggleAdBlock() },
                    onToggleDesktop = { viewModel.toggleDesktopMode() }
                )
            }

            // Movie Details Modal Dialog
            uiState.selectedMovie?.let { movie ->
                MovieDetailsDialog(
                    movie = movie,
                    isTelugu = isTelugu,
                    onDismiss = { viewModel.selectMovie(null) },
                    onOpenPortal = { viewModel.openMovieInPortal(it) },
                    onToggleBookmark = { viewModel.toggleBookmark(it) }
                )
            }
        }
    }
}
