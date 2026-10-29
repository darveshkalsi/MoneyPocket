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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditCalendar
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
import com.example.model.SipPastMonth
import com.example.model.SipStatus
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.PlannerViewModel

@Composable
fun SipTrackerScreen(
    viewModel: PlannerViewModel,
    onNavigateBack: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val sipPlan by viewModel.sipPlan.collectAsState()

    var showEditScheduleDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DetailTopBar(
                title = "SIP Contribution Tracker",
                onBackClick = onNavigateBack,
                trailingAction = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("close_sip_tracker")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextHeadlineSlate
                        )
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
            // Header Student Portfolio Card
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
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(BrandSkyTint),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Savings,
                                    contentDescription = null,
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = profile.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextHeadlineSlate
                                )
                                Text(
                                    text = "Student Portfolio",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondarySlate
                                )
                            }
                        }

                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandPrimaryContainer,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "New SIP Goal",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Active Target Plan Card (Royal Blue)
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(SuccessEmerald)
                                )
                                Text(
                                    text = "ACTIVE TARGET PLAN",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.9f),
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = sipPlan.planName,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "₹${sipPlan.monthlyTarget} / month (${sipPlan.targetPercentage}%)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Schedule,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Next: ${sipPlan.nextExecutionDate}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }
                }
            }

            // November Status / Manual Declaration
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NOVEMBER STATUS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondarySlate,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Manual Declaration",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondarySlate
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerLow)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val current = sipPlan.currentMonthStatus

                        SipStatusSegment(
                            title = "Planned",
                            icon = Icons.Outlined.Circle,
                            isSelected = current == SipStatus.PLANNED,
                            onClick = { viewModel.setSipCurrentStatus(SipStatus.PLANNED) },
                            modifier = Modifier.weight(1f)
                        )

                        SipStatusSegment(
                            title = "Completed",
                            icon = Icons.Default.Check,
                            isSelected = current == SipStatus.COMPLETED,
                            onClick = { viewModel.setSipCurrentStatus(SipStatus.COMPLETED) },
                            modifier = Modifier.weight(1f)
                        )

                        SipStatusSegment(
                            title = "Skip Month",
                            icon = Icons.Outlined.PauseCircle,
                            isSelected = current == SipStatus.SKIP_MONTH,
                            onClick = { viewModel.setSipCurrentStatus(SipStatus.SKIP_MONTH) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Discipline Metrics Card
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Discipline Metrics",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextHeadlineSlate
                        )
                        StatusPill(
                            text = "75% On-Track",
                            backgroundColor = SuccessEmeraldContainer,
                            textColor = SuccessDark
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricBlock(
                            label = "Months Done",
                            value = "${sipPlan.monthsDone} of ${sipPlan.totalMonths}",
                            modifier = Modifier.weight(1f)
                        )
                        MetricBlock(
                            label = "Invested",
                            value = "₹${sipPlan.totalInvested}",
                            modifier = Modifier.weight(1f)
                        )
                        MetricBlock(
                            label = "Overdrafts",
                            value = "${sipPlan.overdraftsCount}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Past Months List
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Past Months",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextHeadlineSlate
                    )
                    Text(
                        text = "ODD SEM 2024",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondarySlate
                    )
                }
            }

            items(sipPlan.pastMonths.size) { index ->
                val month = sipPlan.pastMonths[index]
                PastMonthRow(item = month)
            }

            // Action Buttons
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PrimaryFinButton(
                        text = "Edit Plan Schedule",
                        icon = Icons.Default.EditCalendar,
                        onClick = { showEditScheduleDialog = true },
                        testTag = "edit_plan_schedule_button"
                    )

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PauseCircle,
                            contentDescription = null,
                            tint = TextSecondarySlate,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Pause Tracking",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextHeadlineSlate
                        )
                    }
                }
            }
        }
    }

    if (showEditScheduleDialog) {
        AlertDialog(
            onDismissRequest = { showEditScheduleDialog = false },
            title = {
                Text(
                    text = "SIP Execution Schedule",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Your next scheduled auto-deposit is set for 5 Nov. You can reschedule this date to coordinate with your allowance payout day.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextBodySlate
                )
            },
            confirmButton = {
                Button(
                    onClick = { showEditScheduleDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimaryContainer)
                ) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
fun SipStatusSegment(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (isSelected) SurfaceLight else Color.Transparent
    val textColor = if (isSelected) BrandPrimary else TextBodySlate

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) BrandPrimary else TextMuted,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun MetricBlock(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    FinCard(
        modifier = modifier,
        backgroundColor = SurfaceLight
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = if (value.startsWith("₹")) BrandPrimary else TextHeadlineSlate
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondarySlate
            )
        }
    }
}

@Composable
fun PastMonthRow(item: SipPastMonth) {
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
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (item.status == SipStatus.COMPLETED) SuccessEmeraldContainer else SurfaceContainerLow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (item.status == SipStatus.COMPLETED) Icons.Default.Check else Icons.Outlined.PauseCircle,
                        contentDescription = null,
                        tint = if (item.status == SipStatus.COMPLETED) SuccessDark else TextSecondarySlate,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = item.monthName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextHeadlineSlate
                    )
                    Text(
                        text = if (item.status == SipStatus.COMPLETED) "Completed" else "Skipped",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (item.status == SipStatus.COMPLETED) SuccessDark else TextSecondarySlate
                    )
                }
            }

            Text(
                text = "₹${item.amount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (item.amount > 0) TextHeadlineSlate else TextMuted
            )
        }
    }
}
