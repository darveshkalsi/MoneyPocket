package com.example

import com.example.model.CommitmentCategory
import com.example.model.ExpenseCategory
import com.example.model.PaymentMode
import com.example.viewmodel.PlannerViewModel
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testPlannerInitialCalculations() {
        val viewModel = PlannerViewModel()
        assertEquals(10000, viewModel.allowance.value.monthlyAmount)
        assertEquals(2200, viewModel.reservedPool)
        assertEquals(6000, viewModel.availableCash)
        assertEquals(3800, viewModel.discretionaryPool)
        assertEquals(126, viewModel.dailySafeToSpend)
        assertEquals(7800, viewModel.monthlyLiquidPool)
    }

    @Test
    fun testAddExpenseUpdatesCalculations() {
        val viewModel = PlannerViewModel()
        val initialDaily = viewModel.dailySafeToSpend

        viewModel.addExpense(
            title = "Canteen Chai",
            amount = 190,
            category = ExpenseCategory.FOOD_MESS,
            paymentMode = PaymentMode.UPI
        )

        // Expense added reduces available cash & discretionary pool by 190
        // 190 / 19 days = -10 / day reduction
        assertEquals(initialDaily - 10, viewModel.dailySafeToSpend)
    }

    @Test
    fun testCommitmentSettlingReleasesReservedPool() {
        val viewModel = PlannerViewModel()
        val initialReserved = viewModel.reservedPool
        val firstCommitment = viewModel.commitments.value.first { it.isReserved && !it.isSettled }

        viewModel.toggleCommitmentSettled(firstCommitment.id)

        assertEquals(initialReserved - firstCommitment.amount, viewModel.reservedPool)
    }
}
