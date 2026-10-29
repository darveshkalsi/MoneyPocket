package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WeeklyAllocation
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.PlannerViewModel

@Composable
fun WeeklyPlannerScreen(
    viewModel: PlannerViewModel,
    onNavigateToProfile: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val weeklyAllocations by viewModel.weeklyAllocations.collectAsState()

    var showAdjustDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Pocket-Money Planner",
                subtitle = "Weekly Plan",
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
            // Header / Pacing subtitle
            item {
                Column {
                    Text(
                        text = "NOVEMBER CYCLE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = BrandPrimary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "${profile.name}'s Pacing",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextHeadlineSlate
                    )
                }
            }

            // Weekly Allocation Cards
            items(weeklyAllocations.size) { index ->
                val week = weeklyAllocations[index]
                WeeklyAllocationCard(week = week)
            }

            // Adjust Plan CTA Button
            item {
                PrimaryFinButton(
                    text = "Adjust Plan",
                    icon = Icons.Default.Tune,
                    onClick = { showAdjustDialog = true },
                    testTag = "adjust_plan_button"
                )
            }
        }
    }

    if (showAdjustDialog) {
        AlertDialog(
            onDismissRequest = { showAdjustDialog = false },
            title = {
                Text(
                    text = "Adjust Weekly Pacing",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Smart Pacing automatically balances your discretionary cash across remaining weeks. High spend weeks (e.g. Diwali) are cushioned by Exam week savings.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextBodySlate
                )
            },
            confirmButton = {
                Button(
                    onClick = { showAdjustDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimaryContainer)
                ) {
                    Text("Optimize Pacing")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdjustDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun WeeklyAllocationCard(week: WeeklyAllocation) {
    FinCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceLight
    ) {
        // Week Title & Tag & Amount
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = week.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextHeadlineSlate
                )
                StatusPill(
                    text = week.tag,
                    backgroundColor = when (week.tag) {
                        "Current Week" -> SuccessEmeraldContainer
                        "Social Peak" -> BrandSkyTint
                        else -> SurfaceContainer
                    },
                    textColor = when (week.tag) {
                        "Current Week" -> SuccessDark
                        "Social Peak" -> BrandPrimary
                        else -> TextBodySlate
                    }
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${if (week.isCurrentWeek) week.plannedBudget else week.plannedBudget}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextHeadlineSlate
                )
                Text(
                    text = if (week.isCurrentWeek) "Allocated" else "Planned",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondarySlate
                )
            }
        }

        Text(
            text = week.dateRange,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondarySlate,
            modifier = Modifier.padding(top = 2.dp)
        )

        // Special Highlight / Cushion Note if present
        if (week.cushionNote.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (week.isCurrentWeek) SurfaceContainerLow else BrandSkyTint.copy(alpha = 0.6f))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    val icon = if (week.tag == "Social Peak") Icons.Outlined.Celebration else Icons.Outlined.MenuBook
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BrandPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = week.cushionNote,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextHeadlineSlate
                    )
                }

                if (week.tag == "Social Peak") {
                    StatusPill(
                        text = "+₹350 cushion",
                        backgroundColor = BrandPrimaryContainer,
                        textColor = Color.White
                    )
                } else if (week.tag == "Low Spend Focus") {
                    StatusPill(
                        text = "High Saver",
                        backgroundColor = SuccessEmeraldContainer,
                        textColor = SuccessDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Progress & Metric row
        if (week.isCurrentWeek) {
            val remaining = (week.plannedBudget - week.spent).coerceAtLeast(0)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Spent: ₹${week.spent}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = TextHeadlineSlate
                )
                Text(
                    text = "Remaining: ₹$remaining",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = SuccessDark
                )
            }

            val progress = (week.spent.toFloat() / week.plannedBudget).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = SuccessDark,
                trackColor = SurfaceContainer
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                    contentDescription = null,
                    tint = SuccessDark,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Under daily run-rate by ₹40",
                    style = MaterialTheme.typography.labelSmall,
                    color = SuccessDark,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Budget: ₹${week.plannedBudget}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondarySlate
                )
                Text(
                    text = if (week.tag == "Social Peak") "34% of pool" else "Safe Zone",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (week.tag == "Social Peak") BrandPrimary else SuccessDark
                )
            }

            LinearProgressIndicator(
                progress = { 0.3f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = BrandPrimary,
                trackColor = SurfaceContainer
            )
        }
    }
}
