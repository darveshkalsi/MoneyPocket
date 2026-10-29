package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
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
import com.example.model.Commitment
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.PlannerViewModel

@Composable
fun CommitmentsOverviewScreen(
    viewModel: PlannerViewModel,
    onNavigateToAddCommitment: () -> Unit,
    onNavigateToEditCommitment: (String) -> Unit = {}
) {
    val commitments by viewModel.commitments.collectAsState()
    val allowance by viewModel.allowance.collectAsState()
    val profile by viewModel.profile.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Upcoming, 1: Settled

    val reservedPool = viewModel.reservedPool
    val totalAllowance = allowance.monthlyAmount
    val safeLiquid = totalAllowance - reservedPool
    val lockedPercent = if (totalAllowance > 0) ((reservedPool.toFloat() / totalAllowance) * 100).toInt() else 0

    val upcomingList = commitments.filter { !it.isSettled }
    val settledList = commitments.filter { it.isSettled }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Commitments",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextHeadlineSlate
                        )
                        Text(
                            text = "Fixed monthly dues",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondarySlate
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = TextBodySlate
                            )
                        }

                        Button(
                            onClick = onNavigateToAddCommitment,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandPrimaryContainer,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("add_commitment_header_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Add",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
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
            // Reserved Pool Progress Card
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BrandSkyTint),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "RESERVED POOL",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextHeadlineSlate
                            )
                        }

                        StatusPill(
                            text = "$lockedPercent% Locked",
                            backgroundColor = BrandSkyTint,
                            textColor = BrandPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₹$reservedPool",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = BrandPrimary
                            )
                            Text(
                                text = " Reserved",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondarySlate,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }

                        Text(
                            text = "₹$safeLiquid Safe Liquid",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = TextHeadlineSlate
                        )
                    }

                    // Progress track
                    LinearProgressIndicator(
                        progress = { (lockedPercent / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = BrandPrimary,
                        trackColor = SurfaceContainer
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total ₹$totalAllowance",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondarySlate
                        )
                        Text(
                            text = "${upcomingList.size} dues",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandPrimary
                        )
                    }
                }
            }

            // Segmented Tab Control: Upcoming vs Settled
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedTab == 0) BrandPrimaryContainer else Color.Transparent)
                            .clickable { selectedTab = 0 }
                            .padding(vertical = 10.dp)
                            .testTag("tab_upcoming"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Upcoming (${upcomingList.size})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 0) Color.White else TextBodySlate
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedTab == 1) BrandPrimaryContainer else Color.Transparent)
                            .clickable { selectedTab = 1 }
                            .padding(vertical = 10.dp)
                            .testTag("tab_settled"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Settled (${settledList.size})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 1) Color.White else TextBodySlate
                        )
                    }
                }
            }

            // Commitments List
            val currentList = if (selectedTab == 0) upcomingList else settledList

            if (currentList.isEmpty()) {
                item {
                    FinCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = SurfaceLight
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (selectedTab == 0) Icons.Outlined.CheckCircle else Icons.Outlined.PendingActions,
                                contentDescription = null,
                                tint = SuccessDark,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = if (selectedTab == 0) "All dues are cleared!" else "No settled dues yet",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextHeadlineSlate
                            )
                            Text(
                                text = if (selectedTab == 0) "You have zero pending commitments for this cycle." else "Completed dues will appear here.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondarySlate
                            )
                        }
                    }
                }
            } else {
                items(currentList.size) { index ->
                    val item = currentList[index]
                    CommitmentItemCard(
                        commitment = item,
                        onToggleSettled = { viewModel.toggleCommitmentSettled(item.id) },
                        onClick = { onNavigateToEditCommitment(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CommitmentItemCard(
    commitment: Commitment,
    onToggleSettled: () -> Unit,
    onClick: () -> Unit
) {
    FinCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceLight,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandSkyTint),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when (commitment.category.name) {
                        "HOUSING" -> Icons.Outlined.Apartment
                        "TRANSIT" -> Icons.Outlined.DirectionsSubway
                        "UTILITIES" -> Icons.Outlined.Wifi
                        "FOOD" -> Icons.Outlined.Restaurant
                        "SUPPLIES" -> Icons.Outlined.MenuBook
                        else -> Icons.Outlined.ReceiptLong
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = commitment.category.displayName,
                        tint = BrandPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Text(
                        text = commitment.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextHeadlineSlate
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        StatusPill(
                            text = if (commitment.isSettled) "Settled" else "Due ${commitment.dueDate}",
                            backgroundColor = if (commitment.isSettled) SuccessEmeraldContainer else ErrorRedContainer,
                            textColor = if (commitment.isSettled) SuccessDark else ErrorRed
                        )
                        if (commitment.isReserved) {
                            StatusPill(
                                text = "Reserved",
                                backgroundColor = SurfaceContainerLow,
                                textColor = BrandPrimary,
                                icon = Icons.Outlined.Lock
                            )
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${commitment.amount}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextHeadlineSlate
                    )
                    Text(
                        text = if (commitment.isSettled) "Paid" else "Pending",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (commitment.isSettled) SuccessDark else TextSecondarySlate
                    )
                }

                IconButton(
                    onClick = onToggleSettled,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (commitment.isSettled) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
                        contentDescription = "Toggle Settled",
                        tint = if (commitment.isSettled) SuccessEmerald else TextMuted
                    )
                }
            }
        }
    }
}
