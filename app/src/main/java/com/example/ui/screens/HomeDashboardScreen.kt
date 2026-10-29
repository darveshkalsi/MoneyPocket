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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Commitment
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.PlannerViewModel

@Composable
fun HomeDashboardScreen(
    viewModel: PlannerViewModel,
    onNavigateToAddExpense: () -> Unit,
    onNavigateToExplanation: () -> Unit,
    onNavigateToCommitments: () -> Unit,
    onNavigateToAllowanceSetup: () -> Unit,
    onNavigateToSipPlanner: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToHistory: () -> Unit = {}
) {
    val profile by viewModel.profile.collectAsState()
    val allowance by viewModel.allowance.collectAsState()
    val commitments by viewModel.commitments.collectAsState()
    val sipPlan by viewModel.sipPlan.collectAsState()

    val totalExpenses = viewModel.totalExpenses
    val availableCash = viewModel.availableCash
    val reservedPool = viewModel.reservedPool
    val dailySafeToSpend = viewModel.dailySafeToSpend
    val monthlyPool = viewModel.monthlyLiquidPool
    val spendPercentage = viewModel.spendPercentage

    Scaffold(
        topBar = {
            AppHeader(
                title = "Pocket-Money Planner",
                subtitle = "Home",
                avatarUrl = profile.avatarUrl,
                onAvatarClick = onNavigateToProfile,
                onNotificationClick = onNavigateToHistory
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
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.size(52.dp)) {
                                AsyncImage(
                                    model = profile.avatarUrl,
                                    contentDescription = "Profile",
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, BrandPrimary.copy(alpha = 0.2f), CircleShape)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(SuccessEmerald)
                                        .align(Alignment.BottomEnd),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Verified",
                                        tint = Color.White,
                                        modifier = Modifier.size(11.dp)
                                    )
                                }
                            }
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Hey ${profile.name.split(" ").firstOrNull() ?: "Darvesh"}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextHeadlineSlate
                                    )
                                    Text(text = "👋", fontSize = 16.sp)
                                }
                                Text(
                                    text = profile.college,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondarySlate
                                )
                            }
                        }

                        StatusPill(
                            text = profile.term,
                            backgroundColor = BrandSkyTint,
                            textColor = BrandPrimary,
                            icon = Icons.Default.Circle
                        )
                    }
                }
            }

            // Daily Safe Budget Hero Card
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight,
                    borderColor = BrandSkyBorder
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusPill(
                            text = "DAILY SAFE BUDGET",
                            backgroundColor = BrandSkyTint,
                            textColor = BrandPrimary
                        )

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(onClick = onNavigateToExplanation)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .testTag("explanation_link"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "How is this calculated?",
                                style = MaterialTheme.typography.labelSmall,
                                color = BrandPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null,
                                tint = BrandPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "₹$dailySafeToSpend",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextHeadlineSlate
                        )
                        Text(
                            text = "/ today",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondarySlate,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = SuccessDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${allowance.daysRemaining} days left until allowance",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondarySlate
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(onClick = onNavigateToHistory)
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "₹$totalExpenses of ₹$monthlyPool",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = TextHeadlineSlate
                        )
                        Text(
                            text = "$spendPercentage% spent ›",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary
                        )
                    }

                    LinearProgressIndicator(
                        progress = { (spendPercentage / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = BrandPrimary,
                        trackColor = SurfaceContainer
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = SuccessDark,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "On healthy track",
                                style = MaterialTheme.typography.labelSmall,
                                color = SuccessDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "₹$monthlyPool pool",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondarySlate
                        )
                    }
                }
            }

            // Quick Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FinCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = SurfaceLight
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AVAILABLE CASH",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondarySlate,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Outlined.AccountBalanceWallet,
                                contentDescription = null,
                                tint = BrandPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "₹$availableCash",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextHeadlineSlate
                        )
                    }

                    FinCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = SurfaceLight,
                        onClick = onNavigateToCommitments
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "RESERVED",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondarySlate,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = ErrorRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "₹$reservedPool",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextHeadlineSlate
                        )
                    }
                }
            }

            // Monthly Allowance Row
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceContainerLow,
                    borderColor = Color.Transparent,
                    onClick = onNavigateToAllowanceSetup
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
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(BrandPrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Savings,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Monthly Allowance",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondarySlate
                                )
                                Text(
                                    text = "₹${allowance.monthlyAmount}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextHeadlineSlate
                                )
                            }
                        }

                        Text(
                            text = "Next: ${allowance.nextAllowanceDate}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary
                        )
                    }
                }
            }

            // Primary Add Expense CTA Button
            item {
                PrimaryFinButton(
                    text = "Add Expense",
                    icon = Icons.Default.Add,
                    onClick = onNavigateToAddExpense,
                    testTag = "add_expense_button"
                )
            }

            // Planned SIP Banner
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight,
                    borderColor = SuccessEmerald.copy(alpha = 0.2f),
                    onClick = onNavigateToSipPlanner
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
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SuccessEmeraldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = SuccessDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Planned SIP: ₹${sipPlan.monthlyTarget}/mo (${sipPlan.targetPercentage}%)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextHeadlineSlate
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Next: ${sipPlan.nextExecutionDate}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondarySlate
                                    )
                                    StatusPill(
                                        text = "+18% buffer",
                                        backgroundColor = SuccessEmeraldContainer,
                                        textColor = SuccessDark
                                    )
                                }
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "View SIP",
                            tint = TextSecondarySlate
                        )
                    }
                }
            }

            // Upcoming Expenses Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Upcoming Expenses",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextHeadlineSlate
                        )
                        StatusPill(
                            text = "Next 7 Days",
                            backgroundColor = SurfaceContainer,
                            textColor = TextBodySlate
                        )
                    }

                    Text(
                        text = "See all (${commitments.count { !it.isSettled }})",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = BrandPrimary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable(onClick = onNavigateToCommitments)
                            .padding(4.dp)
                            .testTag("see_all_commitments")
                    )
                }
            }

            // Active Upcoming Commitments List
            val upcoming = commitments.filter { !it.isSettled }
            items(upcoming.size) { index ->
                val commitment = upcoming[index]
                UpcomingExpenseRow(commitment = commitment)
            }
        }
    }
}

@Composable
fun UpcomingExpenseRow(commitment: Commitment) {
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
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandSkyTint),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when (commitment.category.name) {
                        "HOUSING" -> Icons.Outlined.Apartment
                        "TRANSIT" -> Icons.Outlined.DirectionsSubway
                        "UTILITIES" -> Icons.Outlined.Wifi
                        "FOOD" -> Icons.Outlined.Restaurant
                        else -> Icons.Outlined.ReceiptLong
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = commitment.category.displayName,
                        tint = BrandPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = commitment.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextHeadlineSlate
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = commitment.dueDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondarySlate
                        )
                        Text(text = "•", color = TextMuted)
                        StatusPill(
                            text = commitment.category.displayName,
                            backgroundColor = BrandSkyTint,
                            textColor = BrandPrimary
                        )
                        if (commitment.isReserved) {
                            StatusPill(
                                text = "Reserved",
                                backgroundColor = SurfaceContainerLow,
                                textColor = SuccessDark,
                                icon = Icons.Outlined.Lock
                            )
                        }
                    }
                }
            }

            Text(
                text = "₹${commitment.amount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextHeadlineSlate
            )
        }
    }
}
