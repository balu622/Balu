package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CinemaBackground
import com.example.ui.theme.CinemaBorder
import com.example.ui.theme.CinemaSurface
import com.example.ui.theme.CinemaSurfaceVariant
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MovieUiState

@Composable
fun SettingsScreen(
    uiState: MovieUiState,
    onToggleLanguage: () -> Unit,
    onMirrorSelect: (String) -> Unit,
    onToggleAdBlock: () -> Unit,
    onToggleDesktop: () -> Unit
) {
    val isTelugu = uiState.isTeluguLanguage

    val mirrors = listOf(
        "https://www.5movierulz.works/" to "5movierulz.works (సిఫార్సు చేయబడింది / Recommended)",
        "https://5movierulz.tc/" to "5movierulz.tc (మిర్రర్ 2)",
        "https://7movierulz.pw/" to "7movierulz.pw (మిర్రర్ 3)",
        "https://movierulz.lat/" to "movierulz.lat (మిర్రర్ 4)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CinemaBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Title
        Text(
            text = if (isTelugu) "సెట్టింగ్స్ & కాన్ఫిగరేషన్" else "Settings & Configuration",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = if (isTelugu) "యాప్ మరియు పోర్టల్ సెట్టింగులను మార్చండి" else "Customize app and portal experience",
            color = TextMuted,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Language Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CinemaSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CinemaBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = CyanPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isTelugu) "భాష (Language)" else "App Language",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isTelugu) "ప్రస్తుతం: తెలుగు" else "Currently: English",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Switch(
                        checked = isTelugu,
                        onCheckedChange = { onToggleLanguage() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = CyanPrimary,
                            uncheckedTrackColor = CinemaSurfaceVariant
                        ),
                        modifier = Modifier.testTag("lang_switch")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // AdShield / Clean Mode
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CinemaSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CinemaBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = AmberGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isTelugu) "యాడ్-బ్లాక్ & క్లీన్ మోడ్" else "Ad-Blocker & Clean Mode",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isTelugu) "వెబ్‌సైట్ పాప్-అప్‌లను మరియు అడ్వర్టైజ్‌మెంట్లను నిరోధిస్తుంది" else "Blocks invasive popup ads and trackers",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Switch(
                        checked = uiState.isAdBlockEnabled,
                        onCheckedChange = { onToggleAdBlock() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = CyanPrimary,
                            uncheckedTrackColor = CinemaSurfaceVariant
                        ),
                        modifier = Modifier.testTag("adblock_switch")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // MovieRulz Mirror Selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CinemaSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CinemaBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = CyanPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isTelugu) "డిఫాల్ట్ వెబ్‌సైట్ మిర్రర్" else "Default MovieRulz Mirror",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = CinemaBorder, thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(6.dp))

                mirrors.forEach { (url, label) ->
                    val isSelected = uiState.activeMirror == url
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMirrorSelect(url) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onMirrorSelect(url) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = CyanPrimary,
                                unselectedColor = TextMuted
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = label,
                            color = if (isSelected) CyanPrimary else TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // About & Disclaimer
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CinemaSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CinemaBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isTelugu) "యాప్ సమాచారం & మార్గదర్శకాలు" else "About MovieRulz App",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isTelugu)
                        "ఈ యాప్ https://www.5movierulz.works/ వెబ్‌సైట్‌ను అనుకూలమైన మొబైల్ అనుభవంలో చూడటానికి మరియు సరికొత్త తెలుగు సినిమా సమాచారాన్ని తెలుసుకోవడానికి రూపొందించబడింది. ఇది అధికారిక ఓటీటీ ప్లాట్‌ఫారమ్‌లు (Aha, Netflix, Prime Video మొదలైనవి) మరియు ట్రైలర్‌ల వివరాలను కూడా అందిస్తుంది."
                    else
                        "This app is crafted to bring https://www.5movierulz.works/ into an optimized mobile companion with integrated in-app web portal, native Telugu cinema catalog, trailers, and official OTT streaming guide.",
                    color = TextMuted,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "App Version", color = TextMuted, fontSize = 12.sp)
                    Text(text = "v2.5 (Cinema Hub)", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
