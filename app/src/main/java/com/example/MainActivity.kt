package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ViewWeek
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.PocketMoneyPlannerTheme
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextBodySlate
import com.example.viewmodel.PlannerViewModel

enum class MainTab(val label: String, val activeIcon: ImageVector, val inactiveIcon: ImageVector) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    COMMITMENTS("Commitments", Icons.Filled.Lock, Icons.Outlined.Lock),
    WEEKLY_PLAN("Weekly Plan", Icons.Filled.ViewWeek, Icons.Outlined.ViewWeek),
    INSIGHTS("Insights", Icons.Filled.InsertChart, Icons.Outlined.InsertChart),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

sealed class AppDestination {
    data class TabDestination(val tab: MainTab) : AppDestination()
    object AllowanceSetup : AppDestination()
    data class AddEditCommitment(val commitmentId: String? = null) : AppDestination()
    object AddExpense : AppDestination()
    object ExpenseHistory : AppDestination()
    object SafeToSpendExplanation : AppDestination()
    object SmartSipPlanner : AppDestination()
    object SipTracker : AppDestination()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PocketMoneyPlannerTheme {
                PocketMoneyApp()
            }
        }
    }
}

@Composable
fun PocketMoneyApp(plannerViewModel: PlannerViewModel = viewModel()) {
    var currentDestination by remember { mutableStateOf<AppDestination>(AppDestination.TabDestination(MainTab.HOME)) }
    var selectedTab by remember { mutableStateOf(MainTab.HOME) }

    // Back handling for sub-screens
    if (currentDestination !is AppDestination.TabDestination) {
        BackHandler {
            currentDestination = AppDestination.TabDestination(selectedTab)
        }
    } else if (selectedTab != MainTab.HOME) {
        BackHandler {
            selectedTab = MainTab.HOME
            currentDestination = AppDestination.TabDestination(MainTab.HOME)
        }
    }

    val isTopLevelTab = currentDestination is AppDestination.TabDestination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isTopLevelTab) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_nav_bar")
                ) {
                    MainTab.entries.forEach { tab ->
                        val isSelected = selectedTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                selectedTab = tab
                                currentDestination = AppDestination.TabDestination(tab)
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.activeIcon else tab.inactiveIcon,
                                    contentDescription = tab.label
                                )
                            },
                            label = {
                                Text(
                                    text = tab.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BrandPrimary,
                                selectedTextColor = BrandPrimary,
                                indicatorColor = SurfaceContainerLow,
                                unselectedIconColor = TextBodySlate,
                                unselectedTextColor = TextBodySlate
                            ),
                            modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val dest = currentDestination) {
                is AppDestination.TabDestination -> {
                    when (dest.tab) {
                        MainTab.HOME -> HomeDashboardScreen(
                            viewModel = plannerViewModel,
                            onNavigateToAddExpense = { currentDestination = AppDestination.AddExpense },
                            onNavigateToExplanation = { currentDestination = AppDestination.SafeToSpendExplanation },
                            onNavigateToCommitments = {
                                selectedTab = MainTab.COMMITMENTS
                                currentDestination = AppDestination.TabDestination(MainTab.COMMITMENTS)
                            },
                            onNavigateToAllowanceSetup = { currentDestination = AppDestination.AllowanceSetup },
                            onNavigateToSipPlanner = { currentDestination = AppDestination.SmartSipPlanner },
                            onNavigateToProfile = {
                                selectedTab = MainTab.PROFILE
                                currentDestination = AppDestination.TabDestination(MainTab.PROFILE)
                            },
                            onNavigateToHistory = { currentDestination = AppDestination.ExpenseHistory }
                        )
                        MainTab.COMMITMENTS -> CommitmentsOverviewScreen(
                            viewModel = plannerViewModel,
                            onNavigateToAddCommitment = {
                                currentDestination = AppDestination.AddEditCommitment(null)
                            },
                            onNavigateToEditCommitment = { id ->
                                currentDestination = AppDestination.AddEditCommitment(id)
                            }
                        )
                        MainTab.WEEKLY_PLAN -> WeeklyPlannerScreen(
                            viewModel = plannerViewModel,
                            onNavigateToProfile = {
                                selectedTab = MainTab.PROFILE
                                currentDestination = AppDestination.TabDestination(MainTab.PROFILE)
                            }
                        )
                        MainTab.INSIGHTS -> SpendingInsightsScreen(
                            viewModel = plannerViewModel,
                            onNavigateToProfile = {
                                selectedTab = MainTab.PROFILE
                                currentDestination = AppDestination.TabDestination(MainTab.PROFILE)
                            }
                        )
                        MainTab.PROFILE -> StudentProfileScreen(
                            viewModel = plannerViewModel,
                            onNavigateToAllowanceSetup = { currentDestination = AppDestination.AllowanceSetup },
                            onNavigateToExplanation = { currentDestination = AppDestination.SafeToSpendExplanation },
                            onNavigateToSipPlanner = { currentDestination = AppDestination.SmartSipPlanner }
                        )
                    }
                }
                is AppDestination.AllowanceSetup -> AllowanceSetupScreen(
                    viewModel = plannerViewModel,
                    onNavigateBack = { currentDestination = AppDestination.TabDestination(selectedTab) }
                )
                is AppDestination.AddEditCommitment -> AddEditCommitmentScreen(
                    viewModel = plannerViewModel,
                    existingCommitmentId = dest.commitmentId,
                    onNavigateBack = { currentDestination = AppDestination.TabDestination(selectedTab) }
                )
                is AppDestination.AddExpense -> AddExpenseScreen(
                    viewModel = plannerViewModel,
                    onNavigateBack = { currentDestination = AppDestination.TabDestination(selectedTab) }
                )
                is AppDestination.ExpenseHistory -> ExpenseHistoryScreen(
                    viewModel = plannerViewModel,
                    onNavigateBack = { currentDestination = AppDestination.TabDestination(selectedTab) }
                )
                is AppDestination.SafeToSpendExplanation -> SafeToSpendExplanationScreen(
                    viewModel = plannerViewModel,
                    onNavigateBack = { currentDestination = AppDestination.TabDestination(selectedTab) }
                )
                is AppDestination.SmartSipPlanner -> SmartSipPlannerScreen(
                    viewModel = plannerViewModel,
                    onNavigateBack = { currentDestination = AppDestination.TabDestination(selectedTab) },
                    onNavigateToTracker = { currentDestination = AppDestination.SipTracker }
                )
                is AppDestination.SipTracker -> SipTrackerScreen(
                    viewModel = plannerViewModel,
                    onNavigateBack = { currentDestination = AppDestination.SmartSipPlanner }
                )
            }
        }
    }
}
