package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.unit.sp
import com.example.model.ExpenseCategory
import com.example.model.PaymentMode
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.PlannerViewModel

@Composable
fun AddExpenseScreen(
    viewModel: PlannerViewModel,
    onNavigateBack: () -> Unit
) {
    var amountText by remember { mutableStateOf("80") }
    var selectedCategory by remember { mutableStateOf(ExpenseCategory.FOOD_MESS) }
    var selectedPaymentMode by remember { mutableStateOf(PaymentMode.UPI) }
    var noteText by remember { mutableStateOf("") }
    var dateTimeText by remember { mutableStateOf("Today, 26 Oct • 1:45 PM") }

    val amountInt = amountText.toIntOrNull() ?: 0

    Scaffold(
        topBar = {
            DetailTopBar(
                title = "Add Expense",
                onBackClick = onNavigateBack,
                trailingAction = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("close_add_expense")
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
            // Amount Input Card with quick chips
            item {
                FinCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceLight
                ) {
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
                                color = TextHeadlineSlate
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
                                .widthIn(min = 100.dp, max = 220.dp)
                                .testTag("expense_amount_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Campus Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val campusShortcuts = listOf(
                            Triple("20", "Chai", ExpenseCategory.FOOD_MESS),
                            Triple("80", "Thali", ExpenseCategory.FOOD_MESS),
                            Triple("150", "Metro", ExpenseCategory.TRANSIT),
                            Triple("40", "Xerox", ExpenseCategory.STUDY_PRINT)
                        )

                        campusShortcuts.forEach { (amt, label, cat) ->
                            val isSelected = amountText == amt
                            val bg = if (isSelected) BrandSkyTint else SurfaceContainerLow
                            val textColor = if (isSelected) BrandPrimary else TextHeadlineSlate

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(bg)
                                    .clickable {
                                        amountText = amt
                                        selectedCategory = cat
                                    }
                                    .padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "₹$amt",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondarySlate
                                )
                            }
                        }
                    }
                }
            }

            // Campus Category Grid
            item {
                Text(
                    text = "CAMPUS CATEGORY",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondarySlate,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                val categories = ExpenseCategory.entries.toList()
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (i in categories.indices step 3) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (j in 0 until 3) {
                                val index = i + j
                                if (index < categories.size) {
                                    val cat = categories[index]
                                    ExpenseCategoryTile(
                                        category = cat,
                                        isSelected = selectedCategory == cat,
                                        onClick = { selectedCategory = cat },
                                        modifier = Modifier.weight(1f)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // Date & Time Box
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
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(BrandSkyTint),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CalendarToday,
                                    contentDescription = null,
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Date & Time",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondarySlate
                                )
                                Text(
                                    text = dateTimeText,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextHeadlineSlate
                                )
                            }
                        }

                        Button(
                            onClick = {
                                dateTimeText = "Today • Just now"
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceContainer,
                                contentColor = BrandPrimary
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = "Change",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Paid Via Selector
            item {
                Text(
                    text = "PAID VIA",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondarySlate,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PaymentMode.entries.forEach { mode ->
                        val isSelected = selectedPaymentMode == mode
                        val bg = if (isSelected) BrandSkyTint else SurfaceLight
                        val border = if (isSelected) BrandPrimaryContainer else BorderHairline

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, border, RoundedCornerShape(12.dp))
                                .background(bg)
                                .clickable { selectedPaymentMode = mode }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            val icon = when (mode) {
                                PaymentMode.UPI -> Icons.Outlined.Contactless
                                PaymentMode.CASH -> Icons.Outlined.Payments
                                PaymentMode.CAMPUS_CARD -> Icons.Outlined.CreditCard
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) BrandPrimary else TextBodySlate,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = mode.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) BrandPrimary else TextHeadlineSlate
                            )
                        }
                    }
                }
            }

            // Note (Optional)
            item {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    placeholder = {
                        Text(
                            text = "Note (optional)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Notes,
                            contentDescription = null,
                            tint = TextSecondarySlate
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("expense_note_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceLight,
                        unfocusedContainerColor = SurfaceLight,
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = BorderHairline
                    )
                )
            }

            // Save CTA & Cancel
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PrimaryFinButton(
                        text = "Save Expense",
                        icon = Icons.Default.CheckCircle,
                        onClick = {
                            val defaultTitle = when (selectedCategory) {
                                ExpenseCategory.FOOD_MESS -> "Campus Food & Canteen"
                                ExpenseCategory.TRANSIT -> "Metro / Travel"
                                ExpenseCategory.STUDY_PRINT -> "Study Print & Xerox"
                                ExpenseCategory.HANGOUTS -> "Hangout & Snacks"
                                ExpenseCategory.GROOMING -> "Personal Care"
                                ExpenseCategory.OTHER -> "Campus Expense"
                            }
                            val finalTitle = if (noteText.isNotBlank()) noteText else defaultTitle
                            viewModel.addExpense(
                                title = finalTitle,
                                amount = amountInt,
                                category = selectedCategory,
                                paymentMode = selectedPaymentMode,
                                note = noteText
                            )
                            onNavigateBack()
                        },
                        testTag = "save_expense_button"
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
                            .testTag("cancel_expense_button")
                    )
                }
            }
        }
    }
}

@Composable
fun ExpenseCategoryTile(
    category: ExpenseCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (isSelected) BrandPrimaryContainer else SurfaceLight
    val contentColor = if (isSelected) Color.White else TextHeadlineSlate
    val borderColor = if (isSelected) BrandPrimaryContainer else BorderHairline

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val icon = when (category) {
            ExpenseCategory.FOOD_MESS -> Icons.Outlined.Restaurant
            ExpenseCategory.TRANSIT -> Icons.Outlined.DirectionsSubway
            ExpenseCategory.STUDY_PRINT -> Icons.Outlined.MenuBook
            ExpenseCategory.HANGOUTS -> Icons.Outlined.SportsEsports
            ExpenseCategory.GROOMING -> Icons.Outlined.Face
            ExpenseCategory.OTHER -> Icons.Outlined.Category
        }
        Icon(
            imageVector = icon,
            contentDescription = category.displayName,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = contentColor
        )
    }
}
