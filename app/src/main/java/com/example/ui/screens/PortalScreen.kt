package com.example.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CinemaBackground
import com.example.ui.theme.CinemaBorder
import com.example.ui.theme.CinemaSurface
import com.example.ui.theme.CinemaSurfaceVariant
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MovieUiState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PortalScreen(
    uiState: MovieUiState,
    onUrlChange: (String) -> Unit,
    onMirrorSelect: (String) -> Unit,
    onToggleAdBlock: () -> Unit,
    onToggleDesktop: () -> Unit,
    onWebStatusChange: (Boolean, Int, String?, Boolean, Boolean) -> Unit
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var currentUrlInput by remember(uiState.portalUrl) { mutableStateOf(uiState.portalUrl) }
    var hasError by remember { mutableStateOf(false) }
    var showMirrorMenu by remember { mutableStateOf(false) }

    val mirrors = listOf(
        "https://www.5movierulz.works/" to "Primary (5movierulz.works)",
        "https://5movierulz.tc/" to "Mirror 2 (5movierulz.tc)",
        "https://7movierulz.pw/" to "Mirror 3 (7movierulz.pw)",
        "https://movierulz.lat/" to "Mirror 4 (movierulz.lat)"
    )

    val quickShortcuts = listOf(
        "హోమ్" to uiState.activeMirror,
        "తెలుగు సినిమాలు" to "${uiState.activeMirror.removeSuffix("/")}/category/telugu-featured/",
        "డబ్బింగ్" to "${uiState.activeMirror.removeSuffix("/")}/category/telugu-dubbed-movies/",
        "తమిళం" to "${uiState.activeMirror.removeSuffix("/")}/category/tamil-featured/",
        "బాలీవుడ్" to "${uiState.activeMirror.removeSuffix("/")}/category/bollywood-featured/",
        "హాలీవుడ్" to "${uiState.activeMirror.removeSuffix("/")}/category/hollywood-featured/"
    )

    // Handle back button for in-webview navigation
    BackHandler(enabled = webViewRef?.canGoBack() == true) {
        webViewRef?.goBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CinemaBackground)
    ) {
        // Top Navigation Bar
        Surface(
            color = CinemaSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CinemaBorder)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back
                    IconButton(
                        onClick = { webViewRef?.goBack() },
                        enabled = uiState.canGoBackWeb,
                        modifier = Modifier.size(36.dp).testTag("web_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (uiState.canGoBackWeb) Color.White else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Forward
                    IconButton(
                        onClick = { webViewRef?.goForward() },
                        enabled = uiState.canGoForwardWeb,
                        modifier = Modifier.size(36.dp).testTag("web_forward_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Forward",
                            tint = if (uiState.canGoForwardWeb) Color.White else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Refresh
                    IconButton(
                        onClick = {
                            hasError = false
                            webViewRef?.reload()
                        },
                        modifier = Modifier.size(36.dp).testTag("web_refresh_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reload",
                            tint = CyanPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // URL address input box
                    OutlinedTextField(
                        value = currentUrlInput,
                        onValueChange = { currentUrlInput = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .padding(horizontal = 4.dp)
                            .testTag("url_input_field"),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                        keyboardActions = KeyboardActions(
                            onGo = {
                                val url = if (!currentUrlInput.startsWith("http")) "https://$currentUrlInput" else currentUrlInput
                                onUrlChange(url)
                                webViewRef?.loadUrl(url)
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CinemaSurfaceVariant,
                            unfocusedContainerColor = CinemaSurfaceVariant,
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = CinemaBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(20.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                    )

                    // Mirror Selector Button
                    Box {
                        IconButton(
                            onClick = { showMirrorMenu = true },
                            modifier = Modifier.size(36.dp).testTag("mirror_menu_btn")
                        ) {
                            Text(
                                text = "🌐",
                                fontSize = 16.sp
                            )
                        }

                        DropdownMenu(
                            expanded = showMirrorMenu,
                            onDismissRequest = { showMirrorMenu = false },
                            modifier = Modifier.background(CinemaSurfaceVariant)
                        ) {
                            mirrors.forEach { (url, label) ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = label,
                                            color = if (uiState.activeMirror == url) CyanPrimary else TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = if (uiState.activeMirror == url) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        showMirrorMenu = false
                                        hasError = false
                                        onMirrorSelect(url)
                                        currentUrlInput = url
                                        webViewRef?.loadUrl(url)
                                    }
                                )
                            }
                        }
                    }

                    // Open in External Browser
                    IconButton(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uiState.portalUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Cannot open external browser", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.size(36.dp).testTag("open_external_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = "External Browser",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Quick Tools Row: Ad Shield, Desktop Mode, Quick Categories
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ad Shield indicator
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (uiState.isAdBlockEnabled) EmeraldGreen.copy(alpha = 0.2f) else CinemaSurfaceVariant)
                            .border(
                                1.dp,
                                if (uiState.isAdBlockEnabled) EmeraldGreen else CinemaBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable(onClick = onToggleAdBlock)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = if (uiState.isAdBlockEnabled) EmeraldGreen else TextMuted,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (uiState.isAdBlockEnabled) "యాడ్-బ్లాక్ ఆన్" else "యాడ్-బ్లాక్ ఆఫ్",
                                color = if (uiState.isAdBlockEnabled) EmeraldGreen else TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Desktop mode toggle
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (uiState.isDesktopMode) CyanPrimary.copy(alpha = 0.2f) else CinemaSurfaceVariant)
                            .border(
                                1.dp,
                                if (uiState.isDesktopMode) CyanPrimary else CinemaBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                onToggleDesktop()
                                webViewRef?.settings?.userAgentString = if (!uiState.isDesktopMode) {
                                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                                } else {
                                    null
                                }
                                webViewRef?.reload()
                            }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DesktopWindows,
                                contentDescription = null,
                                tint = if (uiState.isDesktopMode) CyanPrimary else TextMuted,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (uiState.isDesktopMode) "డెస్క్‌టాప్" else "మొబైల్",
                                color = if (uiState.isDesktopMode) CyanPrimary else TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Shortcut chips
                    quickShortcuts.forEach { (name, target) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(CinemaSurfaceVariant)
                                .border(1.dp, CinemaBorder, RoundedCornerShape(12.dp))
                                .clickable {
                                    hasError = false
                                    onUrlChange(target)
                                    currentUrlInput = target
                                    webViewRef?.loadUrl(target)
                                }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = name,
                                color = TextPrimary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                // Progress Bar
                if (uiState.isWebLoading && uiState.webProgress < 100) {
                    LinearProgressIndicator(
                        progress = { uiState.webProgress / 100f },
                        modifier = Modifier.fillMaxWidth().height(2.dp),
                        color = CyanPrimary,
                        trackColor = Color.Transparent
                    )
                }
            }
        }

        // Web Error Banner if unreachable or blocked
        AnimatedVisibility(visible = hasError) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2D1619))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = AmberGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "సైట్ అందుబాటులో లేదా? (Website unreachable?)",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "మీ నెట్‌వర్క్ లేదా ISP బ్లాక్ చేసి ఉండవచ్చు. దయచేసి వేరే మిర్రర్ లేదా యాప్ సినిమా క్యాటలాగ్ వాడండి.",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    Button(
                        onClick = {
                            hasError = false
                            val nextMirror = mirrors.firstOrNull { it.first != uiState.activeMirror }?.first ?: mirrors.first().first
                            onMirrorSelect(nextMirror)
                            currentUrlInput = nextMirror
                            webViewRef?.loadUrl(nextMirror)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("వేరే లింక్", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Embedded WebView
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .testTag("portal_webview"),
            factory = { ctx ->
                WebView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        builtInZoomControls = true
                        displayZoomControls = false
                        cacheMode = WebSettings.LOAD_DEFAULT
                        databaseEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            hasError = false
                            url?.let {
                                currentUrlInput = it
                                onUrlChange(it)
                            }
                            onWebStatusChange(true, 10, view?.title, canGoBack(), canGoForward())
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            url?.let {
                                currentUrlInput = it
                                onUrlChange(it)
                            }
                            onWebStatusChange(false, 100, view?.title, canGoBack(), canGoForward())

                            // Injected Ad-filtering script if ad-block enabled
                            if (uiState.isAdBlockEnabled) {
                                val script = """
                                    javascript:(function() {
                                        var css = 'iframe[src*="ad"], div[class*="popup"], div[id*="popup"], div[class*="banner"], a[href*="bet"], a[href*="affiliate"], a[target="_blank"][rel*="nofollow"] { display: none !important; }';
                                        var style = document.createElement('style');
                                        style.type = 'text/css';
                                        style.appendChild(document.createTextNode(css));
                                        document.head.appendChild(style);
                                    })();
                                """.trimIndent()
                                view?.evaluateJavascript(script, null)
                            }
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            if (request?.isForMainFrame == true) {
                                hasError = true
                                onWebStatusChange(false, 100, null, canGoBack(), canGoForward())
                            }
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            val url = request?.url?.toString() ?: return false

                            // Prevent crash for non-http schemas (e.g. magnet:, tel:, mailto:, intent:)
                            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Supported app not installed for this link", Toast.LENGTH_SHORT).show()
                                }
                                return true
                            }

                            // If AdBlock is enabled, filter typical popup domains
                            if (uiState.isAdBlockEnabled) {
                                val lower = url.lowercase()
                                if (lower.contains("popads") || lower.contains("bet365") || lower.contains("adsterra") || lower.contains("clickadu")) {
                                    return true // block popup url
                                }
                            }

                            return false
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            onWebStatusChange(newProgress < 100, newProgress, view?.title, canGoBack(), canGoForward())
                        }

                        override fun onReceivedTitle(view: WebView?, title: String?) {
                            super.onReceivedTitle(view, title)
                            onWebStatusChange(uiState.isWebLoading, uiState.webProgress, title, canGoBack(), canGoForward())
                        }
                    }

                    loadUrl(uiState.portalUrl)
                    webViewRef = this
                }
            },
            update = { webView ->
                webViewRef = webView
                // Only load if the url has changed externally
                if (webView.url != uiState.portalUrl && !uiState.portalUrl.isEmpty()) {
                    webView.loadUrl(uiState.portalUrl)
                }
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            webViewRef?.destroy()
            webViewRef = null
        }
    }
}
