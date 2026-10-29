package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.PlannerViewModel

@Composable
fun SmartSipPlannerScreen(
    viewModel: PlannerViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToTracker: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val allowance by viewModel.allowance.collectAsState()
    val sipPlan by viewModel.sipPlan.collectAsState()

    var selectedPercentage by remember { mutableIntStateOf(sipPlan.targetPercentage) }

    val monthlyTarget = (allowance.monthlyAmount * selectedPercentage) / 100
    val cushion = allowance.monthlyAmount - (viewModel.reservedPool + monthlyTarget + 4000).coerceAtLeast(0)

    Scaffold(
        topBar = {
            DetailTopBar(
                title = "Smart Sip Planner",
                onBackClick = onNavigateBack,
                trailingAction = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Info",
                                tint = TextHeadlineSlate
                            )
                        }
                    }
                }
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Student Card
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(BrandSkyTint),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "DK",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandPrimary
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = profile.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextHeadlineSlate
                                )
                                StatusPill(
                                    text = "Hostel Plan",
                                    backgroundColor = BrandSkyTint,
                                    textColor = BrandPrimary
                                )
                            }
                            Text(
                                text = "${profile.college.split("•").firstOrNull()?.trim() ?: "IIT Delhi"} • Monthly Allowance Sandbox",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondarySlate
                            )
                        }
                    }
                }
            }

            // Target SIP Percentage Selection Cards
            item {
                Text(
                    text = "TARGET SIP PERCENTAGE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondarySlate,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                val options = listOf(
                    Pair(15, "₹1,500/mo (Conservative)"),
                    Pair(20, "₹2,000/mo (Recommended)"),
                    Pair(25, "₹2,500/mo (Aggressive)")
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    options.forEach { (pct, label) ->
                        val isSelected = selectedPercentage == pct
                        val bg = if (isSelected) BrandSkyTint else SurfaceLight
                        val border = if (isSelected) BrandPrimaryContainer else BorderHairline

                        FinCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = bg,
                            borderColor = border,
                            onClick = { selectedPercentage = pct }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedPercentage = pct },
                                        colors = RadioButtonDefaults.colors(selectedColor = BrandPrimary)
                                    )
                                    Column {
                                        Text(
                                            text = "$pct% Allowance",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = TextHeadlineSlate
                                        )
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (isSelected) BrandPrimary else TextSecondarySlate
                                        )
                                    }
                                }

                                Text(
                                    text = "₹${(allowance.monthlyAmount * pct) / 100}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) BrandPrimary else TextHeadlineSlate
                                )
                            }
                        }
                    }
                }
            }

            // Explore 15% SIP Suggestion Card
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Lightbulb,
                                contentDescription = null,
                                tint = BrandPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Explore 15% SIP (₹1,500/mo) to expand cushion to ₹2,300",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = TextHeadlineSlate
                            )
                        }

                        Button(
                            onClick = { selectedPercentage = 15 },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandSkyTint,
                                contentColor = BrandPrimary
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = "Try 15%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Buffer Cushion Status Card
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SuccessEmeraldContainer)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(SuccessDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "Positive buffer (+18%): ₹1,800 cushion",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SuccessDark
                    )
                }
            }

            // Save SIP Plan CTA & View SIP Tracker
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    PrimaryFinButton(
                        text = "Save SIP Plan",
                        icon = Icons.Default.Savings,
                        onClick = {
                            viewModel.updateSipPlan(selectedPercentage)
                            onNavigateBack()
                        },
                        testTag = "save_sip_plan_button"
                    )

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onNavigateToTracker)
                            .padding(8.dp)
                            .testTag("view_sip_tracker_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = TextBodySlate,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "View SIP Tracker",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextHeadlineSlate
                        )
                    }
                }
            }
        }
    }
}
