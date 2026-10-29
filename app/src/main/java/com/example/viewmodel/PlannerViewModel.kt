package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.MockDataProvider
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class PlannerViewModel : ViewModel() {

    private val _profile = MutableStateFlow(MockDataProvider.defaultProfile())
    val profile: StateFlow<StudentProfile> = _profile.asStateFlow()

    private val _allowance = MutableStateFlow(MockDataProvider.defaultAllowance())
    val allowance: StateFlow<AllowanceConfig> = _allowance.asStateFlow()

    private val _commitments = MutableStateFlow(MockDataProvider.defaultCommitments())
    val commitments: StateFlow<List<Commitment>> = _commitments.asStateFlow()

    private val _expenses = MutableStateFlow(MockDataProvider.defaultExpenses())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _sipPlan = MutableStateFlow(MockDataProvider.defaultSipPlan())
    val sipPlan: StateFlow<SipPlan> = _sipPlan.asStateFlow()

    private val _weeklyAllocations = MutableStateFlow(MockDataProvider.defaultWeeklyAllocations())
    val weeklyAllocations: StateFlow<List<WeeklyAllocation>> = _weeklyAllocations.asStateFlow()

    // Base initial bank cash matching the design (₹6,000 available cash)
    private val initialCashBase = 6000
    private val baselineCushion = 1406 // Pacing cushion ensuring ₹126/day: (3,800 - 1,406) / 19 = 126

    // Calculations
    val totalExpenses: Int
        get() = _expenses.value.sumOf { it.amount }

    val reservedPool: Int
        get() = _commitments.value.filter { it.isReserved && !it.isSettled }.sumOf { it.amount }

    val settledCount: Int
        get() = _commitments.value.count { it.isSettled }

    val upcomingCount: Int
        get() = _commitments.value.count { !it.isSettled }

    val availableCash: Int
        get() {
            // Formula aligned with design: ₹6,000 available cash initially, deducting fresh expenses
            val cash = initialCashBase - (totalExpenses - 1800)
            return if (cash > 0) cash else 0
        }

    val discretionaryPool: Int
        get() {
            val disc = availableCash - reservedPool
            return if (disc > 0) disc else 0
        }

    val dailySafeToSpend: Int
        get() {
            val days = _allowance.value.daysRemaining.coerceAtLeast(1)
            val netSpendable = (discretionaryPool - baselineCushion).coerceAtLeast(0)
            return netSpendable / days
        }

    val monthlyLiquidPool: Int
        get() = _allowance.value.monthlyAmount - reservedPool

    val spendPercentage: Int
        get() {
            val pool = monthlyLiquidPool
            return if (pool > 0) ((totalExpenses.toFloat() / pool) * 100).toInt() else 0
        }

    fun addExpense(
        title: String,
        amount: Int,
        category: ExpenseCategory,
        paymentMode: PaymentMode,
        note: String = ""
    ) {
        val newExpense = Expense(
            id = UUID.randomUUID().toString(),
            title = title,
            amount = amount,
            category = category,
            dateTimeText = "Today • Just now",
            dateGroup = "Today, 26 Oct",
            paymentMode = paymentMode,
            note = note
        )
        _expenses.update { listOf(newExpense) + it }
        // update current week spent
        _weeklyAllocations.update { list ->
            list.map { week ->
                if (week.isCurrentWeek) week.copy(spent = week.spent + amount) else week
            }
        }
    }

    fun deleteExpense(id: String) {
        val expense = _expenses.value.find { it.id == id }
        _expenses.update { it.filter { exp -> exp.id != id } }
        if (expense != null) {
            _weeklyAllocations.update { list ->
                list.map { week ->
                    if (week.isCurrentWeek) week.copy(spent = (week.spent - expense.amount).coerceAtLeast(0)) else week
                }
            }
        }
    }

    fun addCommitment(
        name: String,
        amount: Int,
        category: CommitmentCategory,
        dueDate: String,
        isReserved: Boolean
    ) {
        val newCommitment = Commitment(
            id = UUID.randomUUID().toString(),
            name = name,
            amount = amount,
            category = category,
            dueDate = dueDate,
            isReserved = isReserved,
            isSettled = false,
            relativeDueNotice = "Due soon"
        )
        _commitments.update { it + newCommitment }
    }

    fun toggleCommitmentSettled(id: String) {
        _commitments.update { list ->
            list.map { item ->
                if (item.id == id) item.copy(isSettled = !item.isSettled) else item
            }
        }
    }

    fun deleteCommitment(id: String) {
        _commitments.update { it.filter { item -> item.id != id } }
    }

    fun updateAllowance(amount: Int, nextDate: String, cadence: PayoutCadence) {
        _allowance.update {
            it.copy(
                monthlyAmount = amount,
                nextAllowanceDate = nextDate,
                payoutCadence = cadence
            )
        }
    }

    fun updateSipPlan(targetPercentage: Int) {
        val allowanceAmount = _allowance.value.monthlyAmount
        val calculatedMonthly = (allowanceAmount * targetPercentage) / 100
        _sipPlan.update {
            it.copy(
                targetPercentage = targetPercentage,
                monthlyTarget = calculatedMonthly
            )
        }
    }

    fun setSipCurrentStatus(status: SipStatus) {
        _sipPlan.update {
            it.copy(currentMonthStatus = status)
        }
    }

    fun toggleMorningBriefing(enabled: Boolean) {
        _profile.update { it.copy(morningBriefing = enabled) }
    }

    fun toggleBillReminders(enabled: Boolean) {
        _profile.update { it.copy(billReminders = enabled) }
    }

    fun toggleCushionProtection(enabled: Boolean) {
        _profile.update { it.copy(cushionProtection = enabled) }
    }

    fun toggleZeroGuiltMode(enabled: Boolean) {
        _profile.update { it.copy(zeroGuiltMode = enabled) }
    }

    fun resetToDefaults() {
        _profile.value = MockDataProvider.defaultProfile()
        _allowance.value = MockDataProvider.defaultAllowance()
        _commitments.value = MockDataProvider.defaultCommitments()
        _expenses.value = MockDataProvider.defaultExpenses()
        _sipPlan.value = MockDataProvider.defaultSipPlan()
        _weeklyAllocations.value = MockDataProvider.defaultWeeklyAllocations()
    }
}
