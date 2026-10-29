package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.PlannerViewModel

@Composable
fun SpendingInsightsScreen(
    viewModel: PlannerViewModel,
    onNavigateToProfile: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val allowance by viewModel.allowance.collectAsState()
    val totalSpent = viewModel.totalExpenses

    Scaffold(
        topBar = {
            AppHeader(
                title = "Pocket-Money Planner",
                subtitle = "Spend Insights",
                avatarUrl = profile.avatarUrl,
                onAvatarClick = onNavigateToProfile
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
            // Month Selector & Cycle Day Pill
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerLow)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "October 2024 (${profile.term})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextHeadlineSlate
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = TextSecondarySlate,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    StatusPill(
                        text = "Day 12 / 31",
                        backgroundColor = SuccessEmeraldContainer,
                        textColor = SuccessDark,
                        icon = Icons.Outlined.CalendarMonth
                    )
                }
            }

            // Monthly Budget Card (Gradient Blue)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(BrandPrimaryContainer)
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MONTHLY BUDGET",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f),
                                letterSpacing = 0.5.sp
                            )
                            StatusPill(
                                text = "Healthy Pacing",
                                backgroundColor = Color.White.copy(alpha = 0.2f),
                                textColor = Color.White,
                                icon = Icons.Default.Check
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₹$totalSpent",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = " / ₹4,000",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        LinearProgressIndicator(
                            progress = { 0.45f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = 0.3f)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "45% used",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = "${allowance.daysRemaining} days remaining",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Smart Observation Card
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight,
                    borderColor = BrandSkyBorder
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(BrandSkyTint),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Lightbulb,
                                contentDescription = null,
                                tint = BrandPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "SMART OBSERVATION",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandPrimary,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "${profile.term} Routine",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondarySlate
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "68% of spending happens between 1 PM and 5 PM on campus.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextHeadlineSlate
                            )
                        }
                    }
                }
            }

            // Category Distribution Donut & Breakdown
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Category Distribution",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextHeadlineSlate
                            )
                            Text(
                                text = "Where allowance flowed this month",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondarySlate
                            )
                        }
                        StatusPill(
                            text = "4 buckets",
                            backgroundColor = SurfaceContainer,
                            textColor = TextBodySlate
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Donut Chart + Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Donut Canvas
                        Box(
                            modifier = Modifier.size(130.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(130.dp)) {
                                val strokeWidth = 22.dp.toPx()
                                val arcSize = size.width - strokeWidth
                                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                                // Segment 1: Food 53%
                                drawArc(
                                    color = BrandPrimaryContainer,
                                    startAngle = -90f,
                                    sweepAngle = 360f * 0.53f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = Size(arcSize, arcSize),
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                )
                                // Segment 2: Transit 23%
                                drawArc(
                                    color = SuccessEmerald,
                                    startAngle = -90f + (360f * 0.53f),
                                    sweepAngle = 360f * 0.23f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = Size(arcSize, arcSize),
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                )
                                // Segment 3: Study 16%
                                drawArc(
                                    color = BrandAccentSky,
                                    startAngle = -90f + (360f * 0.76f),
                                    sweepAngle = 360f * 0.16f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = Size(arcSize, arcSize),
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                )
                                // Segment 4: Hangouts 8%
                                drawArc(
                                    color = Color(0xFF93C5FD),
                                    startAngle = -90f + (360f * 0.92f),
                                    sweepAngle = 360f * 0.08f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = Size(arcSize, arcSize),
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Total",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondarySlate
                                )
                                Text(
                                    text = "₹1.8k",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextHeadlineSlate
                                )
                            }
                        }

                        // Legend
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(start = 12.dp)
                        ) {
                            CategoryLegendItem("Food & Canteen", "53%", BrandPrimaryContainer)
                            CategoryLegendItem("Metro & Transit", "23%", SuccessEmerald)
                            CategoryLegendItem("Study & Xerox", "16%", BrandAccentSky)
                            CategoryLegendItem("Hangouts", "8%", Color(0xFF93C5FD))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Horizontal multi-colored progress bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(0.53f)
                                .fillMaxHeight()
                                .background(BrandPrimaryContainer)
                        )
                        Box(
                            modifier = Modifier
                                .weight(0.23f)
                                .fillMaxHeight()
                                .background(SuccessEmerald)
                        )
                        Box(
                            modifier = Modifier
                                .weight(0.16f)
                                .fillMaxHeight()
                                .background(BrandAccentSky)
                        )
                        Box(
                            modifier = Modifier
                                .weight(0.08f)
                                .fillMaxHeight()
                                .background(Color(0xFF93C5FD))
                        )
                    }
                }
            }

            // Category Deep-Dive Card
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Category Deep-Dive",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextHeadlineSlate
                        )
                        Text(
                            text = "October spend",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondarySlate
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    DeepDiveItem(
                        icon = Icons.Outlined.Restaurant,
                        name = "Food & Canteen",
                        percentage = "53%",
                        amount = "₹950",
                        iconBg = BrandSkyTint,
                        iconTint = BrandPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DeepDiveItem(
                        icon = Icons.Outlined.DirectionsSubway,
                        name = "Metro & Transit",
                        percentage = "23%",
                        amount = "₹420",
                        iconBg = SuccessEmeraldContainer,
                        iconTint = SuccessDark
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DeepDiveItem(
                        icon = Icons.Outlined.MenuBook,
                        name = "Study & Xerox Prints",
                        percentage = "16%",
                        amount = "₹280",
                        iconBg = BrandSkyTint,
                        iconTint = BrandAccentSky
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DeepDiveItem(
                        icon = Icons.Outlined.SportsEsports,
                        name = "Hangouts & Personal",
                        percentage = "8%",
                        amount = "₹150",
                        iconBg = SurfaceContainerLow,
                        iconTint = BrandPrimary
                    )
                }
            }

            // Weekly Spend Rhythms Bar Chart
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Weekly Spend Rhythms",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextHeadlineSlate
                            )
                            Text(
                                text = "Weekday vs Weekend distribution",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondarySlate
                            )
                        }

                        StatusPill(
                            text = "Peak: Thursday",
                            backgroundColor = BrandSkyTint,
                            textColor = BrandPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Day of Week Bars
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val daysData = listOf(
                            Pair("M", 180),
                            Pair("T", 210),
                            Pair("W", 240),
                            Pair("Th", 460),
                            Pair("F", 310),
                            Pair("Sa", 220),
                            Pair("Su", 180)
                        )

                        val maxVal = 460f

                        daysData.forEach { (day, amount) ->
                            val isPeak = day == "Th"
                            val barHeightRatio = (amount / maxVal).coerceIn(0.1f, 1f)

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "₹$amount",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isPeak) FontWeight.ExtraBold else FontWeight.SemiBold,
                                    color = if (isPeak) BrandPrimary else TextSecondarySlate,
                                    fontSize = 10.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(26.dp)
                                        .height((100 * barHeightRatio).dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isPeak) BrandPrimaryContainer else BrandSkyBorder)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isPeak) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isPeak) BrandPrimary else TextBodySlate
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceContainerLow)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = BrandPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Peak spending occurs on Thursdays.",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = TextHeadlineSlate
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryLegendItem(name: String, percentage: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.width(170.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                color = TextBodySlate
            )
        }
        Text(
            text = percentage,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = TextHeadlineSlate
        )
    }
}

@Composable
fun DeepDiveItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    name: String,
    percentage: String,
    amount: String,
    iconBg: Color,
    iconTint: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextHeadlineSlate
                )
                Text(
                    text = percentage,
                    style = MaterialTheme.typography.labelSmall,
                    color = BrandPrimary
                )
            }
        }

        Text(
            text = amount,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = TextHeadlineSlate
        )
    }
}
