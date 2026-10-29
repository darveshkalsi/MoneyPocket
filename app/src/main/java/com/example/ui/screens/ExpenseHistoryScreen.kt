package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.model.Expense
import com.example.model.ExpenseCategory
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.PlannerViewModel

@Composable
fun ExpenseHistoryScreen(
    viewModel: PlannerViewModel,
    onNavigateBack: () -> Unit
) {
    val expenses by viewModel.expenses.collectAsState()
    val allowance by viewModel.allowance.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val totalSpent = viewModel.totalExpenses
    val totalAllowance = allowance.monthlyAmount
    val remainingBudget = (totalAllowance - totalSpent).coerceAtLeast(0)

    val filterOptions = listOf("All", "Food", "Travel", "Study", "Other")

    val filteredExpenses = remember(expenses, searchQuery, selectedFilter) {
        expenses.filter { expense ->
            val matchesSearch = searchQuery.isBlank() ||
                    expense.title.contains(searchQuery, ignoreCase = true) ||
                    expense.note.contains(searchQuery, ignoreCase = true) ||
                    expense.category.displayName.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "All" -> true
                "Food" -> expense.category == ExpenseCategory.FOOD_MESS
                "Travel" -> expense.category == ExpenseCategory.TRANSIT
                "Study" -> expense.category == ExpenseCategory.STUDY_PRINT
                "Other" -> expense.category in listOf(ExpenseCategory.HANGOUTS, ExpenseCategory.GROOMING, ExpenseCategory.OTHER)
                else -> true
            }

            matchesSearch && matchesFilter
        }
    }

    // Group filtered expenses by date
    val grouped = remember(filteredExpenses) {
        filteredExpenses.groupBy { it.dateGroup }
    }

    Scaffold(
        topBar = {
            DetailTopBar(
                title = "Expense History",
                onBackClick = onNavigateBack,
                trailingAction = {
                    IconButton(
                        onClick = {},
                        modifier = Modifier.testTag("search_icon_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
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
            // Search Input Field
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Search expenses, tags, canteen, metro...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                            tint = TextSecondarySlate
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("expense_search_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceLight,
                        unfocusedContainerColor = SurfaceLight,
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = BorderHairline
                    ),
                    singleLine = true
                )
            }

            // Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterOptions.forEach { filter ->
                        val isSelected = selectedFilter == filter
                        val bg = if (isSelected) BrandPrimaryContainer else SurfaceContainerLow
                        val textColor = if (isSelected) Color.White else TextHeadlineSlate

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(bg)
                                .clickable { selectedFilter = filter }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("filter_chip_$filter"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = filter,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }
                    }
                }
            }

            // Monthly Summary Banner
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BrandSkyTint,
                    borderColor = BrandSkyBorder
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarToday,
                                contentDescription = null,
                                tint = BrandPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "October: ₹$totalSpent spent",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = BrandPrimary
                            )
                        }

                        Text(
                            text = "₹$remainingBudget left",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SuccessDark
                        )
                    }
                }
            }

            // Grouped Transaction Sections
            grouped.forEach { (dateHeader, itemsInDate) ->
                val dayTotal = itemsInDate.sumOf { it.amount }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (dateHeader.startsWith("Today")) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(BrandPrimary)
                                )
                            }
                            Text(
                                text = dateHeader,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextHeadlineSlate
                            )
                        }

                        Text(
                            text = "₹$dayTotal total",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondarySlate
                        )
                    }
                }

                items(itemsInDate.size) { idx ->
                    val expense = itemsInDate[idx]
                    ExpenseItemRow(expense = expense)
                }
            }
        }
    }
}

@Composable
fun ExpenseItemRow(expense: Expense) {
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
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandSkyTint),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when (expense.category) {
                        ExpenseCategory.FOOD_MESS -> Icons.Outlined.Restaurant
                        ExpenseCategory.TRANSIT -> Icons.Outlined.DirectionsSubway
                        ExpenseCategory.STUDY_PRINT -> Icons.Outlined.MenuBook
                        ExpenseCategory.HANGOUTS -> Icons.Outlined.SportsEsports
                        ExpenseCategory.GROOMING -> Icons.Outlined.Face
                        ExpenseCategory.OTHER -> Icons.Outlined.ReceiptLong
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = expense.category.displayName,
                        tint = BrandPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = expense.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextHeadlineSlate
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        StatusPill(
                            text = expense.category.displayName,
                            backgroundColor = BrandSkyTint,
                            textColor = BrandPrimary
                        )
                        Text(
                            text = "•",
                            color = TextMuted
                        )
                        Text(
                            text = expense.dateTimeText.split("•").lastOrNull()?.trim() ?: "",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondarySlate
                        )
                        Text(
                            text = "•",
                            color = TextMuted
                        )
                        Text(
                            text = expense.paymentMode.name,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary
                        )
                    }
                }
            }

            Text(
                text = "-₹${expense.amount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextHeadlineSlate
            )
        }
    }
}
