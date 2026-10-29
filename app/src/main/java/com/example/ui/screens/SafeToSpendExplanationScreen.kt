package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
fun SafeToSpendExplanationScreen(
    viewModel: PlannerViewModel,
    onNavigateBack: () -> Unit
) {
    val allowance by viewModel.allowance.collectAsState()
    val dailySafeToSpend = viewModel.dailySafeToSpend
    val availableCash = viewModel.availableCash
    val reservedPool = viewModel.reservedPool
    val discretionaryPool = viewModel.discretionaryPool
    val daysRemaining = allowance.daysRemaining

    var isHowItWorksExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DetailTopBar(
                title = "Safe-to-Spend Explanation",
                onBackClick = onNavigateBack,
                trailingAction = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("close_explanation")
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
            // Hero Card (Royal Blue)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(BrandPrimaryContainer)
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StatusPill(
                            text = "DAILY SAFE VELOCITY",
                            backgroundColor = Color.White.copy(alpha = 0.2f),
                            textColor = Color.White,
                            icon = Icons.Outlined.Speed
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₹$dailySafeToSpend",
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = " / day",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "$daysRemaining days remaining until ${allowance.nextAllowanceDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Formula Breakdown Card
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
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BrandSkyTint),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Calculate,
                                    contentDescription = null,
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "Formula Breakdown",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextHeadlineSlate
                            )
                        }

                        StatusPill(
                            text = "Transparent",
                            backgroundColor = BrandSkyTint,
                            textColor = BrandPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Step 1: Gross Cash
                    FormulaRow(
                        label = "Gross Cash",
                        value = "₹$availableCash",
                        valueColor = TextHeadlineSlate
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Step 2: Less Reserved Bills
                    FormulaRow(
                        label = "Less Reserved Bills",
                        value = "-₹$reservedPool",
                        valueColor = ErrorRed
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Step 3: Discretionary Pool
                    FormulaRow(
                        label = "Discretionary Pool",
                        value = "₹$discretionaryPool",
                        valueColor = BrandPrimary,
                        isBold = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Step 4: Divided by Days
                    FormulaRow(
                        label = "Divided by $daysRemaining Days",
                        value = "$daysRemaining Days",
                        valueColor = TextHeadlineSlate
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Final Daily Limit Highlight Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SuccessEmeraldContainer)
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Daily Safe Spend Limit",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SuccessDark
                        )
                        Text(
                            text = "₹$dailySafeToSpend/day",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = SuccessDark
                        )
                    }
                }
            }

            // How it works accordion
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isHowItWorksExpanded = !isHowItWorksExpanded },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BrandSkyTint),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.HelpOutline,
                                    contentDescription = null,
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "How it works",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextHeadlineSlate
                            )
                        }

                        Icon(
                            imageVector = if (isHowItWorksExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand",
                            tint = TextSecondarySlate
                        )
                    }

                    AnimatedVisibility(visible = isHowItWorksExpanded) {
                        Column(modifier = Modifier.padding(top = 12.dp)) {
                            Text(
                                text = "Safe-to-Spend ring-fences your fixed dues (like rent, metro pass, and campus Wi-Fi) first. The remainder is divided equally over your remaining days. If you spend less today, tomorrow's limit expands without guilt!",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextBodySlate,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            // Educational Guide Card
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceContainerLow)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = TextSecondarySlate,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Educational Pacing Guide: Safe-to-Spend is a dynamic pacing guide based on your allowance cycle and recorded expenses. It is for student planning purposes.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondarySlate
                    )
                }
            }

            // Back to Dashboard CTA
            item {
                PrimaryFinButton(
                    text = "Back to Dashboard",
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = onNavigateBack,
                    testTag = "back_to_dashboard_button"
                )
            }
        }
    }
}

@Composable
fun FormulaRow(
    label: String,
    value: String,
    valueColor: Color,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceContainerLow)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
            color = TextHeadlineSlate
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Bold,
            color = valueColor
        )
    }
}
