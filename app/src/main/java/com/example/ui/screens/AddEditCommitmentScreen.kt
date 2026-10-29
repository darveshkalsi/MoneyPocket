package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.*
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
import com.example.model.CommitmentCategory
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.PlannerViewModel

@Composable
fun AddEditCommitmentScreen(
    viewModel: PlannerViewModel,
    existingCommitmentId: String? = null,
    onNavigateBack: () -> Unit
) {
    val commitments by viewModel.commitments.collectAsState()
    val allowance by viewModel.allowance.collectAsState()
    val existing = remember(existingCommitmentId) {
        commitments.find { it.id == existingCommitmentId }
    }

    var amountText by remember { mutableStateOf(existing?.amount?.toString() ?: "1500") }
    var nameText by remember { mutableStateOf(existing?.name ?: "Hostel Rent Share") }
    var selectedCategory by remember { mutableStateOf(existing?.category ?: CommitmentCategory.HOUSING) }
    var dueDateText by remember { mutableStateOf(existing?.dueDate ?: "28 October 2024") }
    var isReserved by remember { mutableStateOf(existing?.isReserved ?: true) }

    val amountInt = amountText.toIntOrNull() ?: 0
    val days = allowance.daysRemaining.coerceAtLeast(1)
    val deltaDaily = if (isReserved) amountInt / days else 0
    val currentDaily = viewModel.dailySafeToSpend
    val adjustedDaily = (currentDaily - (if (existing == null) deltaDaily else 0)).coerceAtLeast(0)

    Scaffold(
        topBar = {
            DetailTopBar(
                title = if (existing != null) "Edit Commitment" else "Add Commitment",
                onBackClick = onNavigateBack,
                trailingAction = {
                    IconButton(
                        onClick = {},
                        modifier = Modifier.testTag("more_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
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
            // Pledged Amount Input Card
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight
                ) {
                    Text(
                        text = "Pledged Amount",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondarySlate,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "₹",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextHeadlineSlate,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        OutlinedTextField(
                            value = amountText,
                            onValueChange = { amountText = it.filter { ch -> ch.isDigit() } },
                            textStyle = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BrandPrimary
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .widthIn(min = 120.dp, max = 220.dp)
                                .testTag("pledged_amount_input")
                        )
                    }
                }
            }

            // Commitment Name Card
            item {
                Column {
                    Text(
                        text = "Commitment Name",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextHeadlineSlate
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nameText,
                        onValueChange = { nameText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("commitment_name_input"),
                        shape = RoundedCornerShape(14.dp),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                tint = TextSecondarySlate
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceLight,
                            unfocusedContainerColor = SurfaceLight,
                            focusedBorderColor = BrandPrimary,
                            unfocusedBorderColor = BorderHairline
                        )
                    )
                }
            }

            // Category Grid
            item {
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextHeadlineSlate
                )
                Spacer(modifier = Modifier.height(8.dp))

                val categories = CommitmentCategory.entries.toList()
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (i in categories.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val cat1 = categories[i]
                            CategoryOptionTile(
                                category = cat1,
                                isSelected = selectedCategory == cat1,
                                onClick = { selectedCategory = cat1 },
                                modifier = Modifier.weight(1f)
                            )
                            if (i + 1 < categories.size) {
                                val cat2 = categories[i + 1]
                                CategoryOptionTile(
                                    category = cat2,
                                    isSelected = selectedCategory == cat2,
                                    onClick = { selectedCategory = cat2 },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // Target Due Date
            item {
                Text(
                    text = "Target Due Date",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextHeadlineSlate
                )
                Spacer(modifier = Modifier.height(8.dp))

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
                                Icon(
                                    imageVector = Icons.Outlined.CalendarMonth,
                                    contentDescription = null,
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = dueDateText,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextHeadlineSlate
                                )
                                Text(
                                    text = "Due in 4 days (Before Month-end)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SuccessDark,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                dueDateText = if (dueDateText.contains("28")) "31 October 2024" else "28 October 2024"
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerLow)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Change Date",
                                tint = TextBodySlate
                            )
                        }
                    }
                }
            }

            // Reserve Money Switch
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
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BrandSkyTint),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Reserve this money",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextHeadlineSlate
                                )
                                Text(
                                    text = "Deducts from daily Safe-to-Spend",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondarySlate
                                )
                            }
                        }

                        Switch(
                            checked = isReserved,
                            onCheckedChange = { isReserved = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = BrandPrimaryContainer
                            ),
                            modifier = Modifier.testTag("reserve_switch")
                        )
                    }
                }
            }

            // Live Impact Notice Pill
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandSkyTint)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(BrandPrimary)
                        )
                        Text(
                            text = "Safe-to-Spend adjusts to ₹$adjustedDaily/day (-₹$deltaDaily)",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandPrimary
                        )
                    }
                }
            }

            // Bottom Action Buttons
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PrimaryFinButton(
                        text = "Save Commitment",
                        icon = Icons.Default.Bookmark,
                        onClick = {
                            viewModel.addCommitment(
                                name = nameText,
                                amount = amountInt,
                                category = selectedCategory,
                                dueDate = dueDateText,
                                isReserved = isReserved
                            )
                            onNavigateBack()
                        },
                        testTag = "save_commitment_button"
                    )

                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondarySlate,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onNavigateBack)
                            .padding(8.dp)
                            .testTag("cancel_commitment_button")
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryOptionTile(
    category: CommitmentCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (isSelected) BrandPrimaryContainer else SurfaceLight
    val contentColor = if (isSelected) Color.White else TextHeadlineSlate
    val borderColor = if (isSelected) BrandPrimaryContainer else BorderHairline

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val icon = when (category) {
            CommitmentCategory.HOUSING -> Icons.Outlined.Home
            CommitmentCategory.TRANSIT -> Icons.Outlined.DirectionsSubway
            CommitmentCategory.FOOD -> Icons.Outlined.Restaurant
            CommitmentCategory.SUPPLIES -> Icons.Outlined.MenuBook
            CommitmentCategory.UTILITIES -> Icons.Outlined.Wifi
            CommitmentCategory.OTHER -> Icons.Outlined.Category
        }
        Icon(
            imageVector = icon,
            contentDescription = category.displayName,
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}
