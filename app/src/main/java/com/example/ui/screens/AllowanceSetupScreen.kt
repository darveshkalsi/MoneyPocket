package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.model.PayoutCadence
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.PlannerViewModel

@Composable
fun AllowanceSetupScreen(
    viewModel: PlannerViewModel,
    onNavigateBack: () -> Unit
) {
    val currentAllowance by viewModel.allowance.collectAsState()

    var allowanceText by remember { mutableStateOf(currentAllowance.monthlyAmount.toString()) }
    var nextDate by remember { mutableStateOf(currentAllowance.nextAllowanceDate) }
    var selectedCadence by remember { mutableStateOf(currentAllowance.payoutCadence) }

    val allowanceInt = allowanceText.toIntOrNull() ?: 0
    val dailyBaseline = if (allowanceInt > 0) allowanceInt / 30 else 0

    Scaffold(
        topBar = {
            DetailTopBar(
                title = "Allowance Setup",
                onBackClick = onNavigateBack,
                trailingAction = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("close_button")
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
            // Monthly Allowance Input Card
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight
                ) {
                    Text(
                        text = "Monthly Allowance",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextHeadlineSlate
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = allowanceText,
                        onValueChange = { allowanceText = it.filter { char -> char.isDigit() } },
                        leadingIcon = {
                            Text(
                                text = "₹",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextHeadlineSlate
                            )
                        },
                        textStyle = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextHeadlineSlate
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("allowance_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceContainerLow,
                            unfocusedContainerColor = SurfaceContainerLow,
                            focusedBorderColor = BrandPrimary,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Preset Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val presets = listOf(5000, 8000, 10000, 15000)
                        presets.forEach { preset ->
                            val isSelected = allowanceText == preset.toString()
                            val chipBg = if (isSelected) BrandPrimaryContainer else SurfaceContainer
                            val chipText = if (isSelected) Color.White else TextHeadlineSlate

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(chipBg)
                                    .clickable { allowanceText = preset.toString() }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "₹${preset / 1000}k",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = chipText
                                )
                            }
                        }
                    }
                }
            }

            // Next Allowance Date Card
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight
                ) {
                    Text(
                        text = "Next Allowance Date",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextHeadlineSlate
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceContainerLow)
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BrandSkyTint),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CalendarMonth,
                                    contentDescription = null,
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = nextDate,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = TextHeadlineSlate
                            )
                        }

                        IconButton(
                            onClick = {
                                nextDate = if (nextDate.contains("Dec")) "15 Dec 2024" else "1 Dec 2024"
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = "Pick Date",
                                tint = BrandPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Payout Cadence Card
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight
                ) {
                    Text(
                        text = "Payout Cadence",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextHeadlineSlate
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PayoutCadence.entries.forEach { cadence ->
                        val isSelected = selectedCadence == cadence
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) SurfaceContainerLow else Color.Transparent)
                                .clickable { selectedCadence = cadence }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedCadence = cadence },
                                    colors = RadioButtonDefaults.colors(selectedColor = BrandPrimary)
                                )
                                Text(
                                    text = cadence.label,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextHeadlineSlate
                                )
                            }

                            Text(
                                text = cadence.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isSelected) BrandPrimary else TextSecondarySlate
                            )
                        }
                    }
                }
            }

            // Daily Safety Net Preview Card
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight,
                    borderColor = BrandSkyBorder
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "DAILY SAFETY NET",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondarySlate,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "₹$dailyBaseline",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BrandPrimary
                                )
                                Text(
                                    text = "/day",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondarySlate,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }

                        StatusPill(
                            text = "Safe baseline",
                            backgroundColor = SuccessEmeraldContainer,
                            textColor = SuccessDark,
                            icon = Icons.Outlined.Security
                        )
                    }
                }
            }

            // Save Allowance CTA & Skip
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PrimaryFinButton(
                        text = "Save Allowance",
                        onClick = {
                            viewModel.updateAllowance(
                                amount = allowanceInt,
                                nextDate = nextDate,
                                cadence = selectedCadence
                            )
                            onNavigateBack()
                        },
                        testTag = "save_allowance_button"
                    )

                    Text(
                        text = "Skip for Now",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondarySlate,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onNavigateBack)
                            .padding(8.dp)
                            .testTag("skip_allowance_button")
                    )
                }
            }
        }
    }
}
