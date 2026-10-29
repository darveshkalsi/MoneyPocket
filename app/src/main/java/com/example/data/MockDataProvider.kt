package com.example.data

import com.example.model.*

object MockDataProvider {
    fun defaultProfile() = StudentProfile(
        name = "Darvesh Kalsi",
        college = "IIT Delhi • 3rd Year B.Tech",
        hostel = "Hostel Karakoram",
        term = "Term 5",
        allowanceDay = "1st of Month",
        budgetHealth = "Optimal (Safe)",
        avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAbvzvQE5s2oWl2E92BXqxHdYET-SObLW4OYF0EPRgj0TAeTYO2h0HKYu7XRDCCT9NVaM2bzNBF0_a9H1t3OBTOtvurKWNML5aJDhGffOGOqUAtqa6NFynyqwbtKpTox_JdZPUI7UwEOohqNsFZDSsrOZUk_odPEFDDvGtvq4XhZpCuwg8WhO9QW1qudGxe0Ojy37PcjyWcmSikuG_VIrQt_edh9VDO7RIIx3U5BE7A",
        isVerified = true,
        morningBriefing = true,
        billReminders = true,
        cushionProtection = true,
        zeroGuiltMode = true,
        campusLanguage = "English"
    )

    fun defaultAllowance() = AllowanceConfig(
        monthlyAmount = 10000,
        nextAllowanceDate = "1 Dec 2024",
        daysRemaining = 19,
        payoutCadence = PayoutCadence.MONTHLY
    )

    fun defaultCommitments() = listOf(
        Commitment(
            id = "c1",
            name = "Hostel Rent Share",
            amount = 1500,
            category = CommitmentCategory.HOUSING,
            dueDate = "28 Oct",
            isReserved = true,
            isSettled = false,
            relativeDueNotice = "Due in 2 days (Before Month-end)"
        ),
        Commitment(
            id = "c2",
            name = "Delhi Metro Smart Card",
            amount = 500,
            category = CommitmentCategory.TRANSIT,
            dueDate = "31 Oct",
            isReserved = true,
            isSettled = false,
            relativeDueNotice = "Due in 5 days"
        ),
        Commitment(
            id = "c3",
            name = "Campus Wi-Fi Split",
            amount = 200,
            category = CommitmentCategory.UTILITIES,
            dueDate = "2 Nov",
            isReserved = true,
            isSettled = false,
            relativeDueNotice = "Due in 7 days"
        ),
        Commitment(
            id = "c4",
            name = "College ID Replacement Fee",
            amount = 250,
            category = CommitmentCategory.OTHER,
            dueDate = "15 Oct",
            isReserved = false,
            isSettled = true,
            relativeDueNotice = "Settled on 15 Oct"
        )
    )

    fun defaultExpenses() = listOf(
        Expense(
            id = "e1",
            title = "Canteen Lunch Thali",
            amount = 80,
            category = ExpenseCategory.FOOD_MESS,
            dateTimeText = "Today, 26 Oct • 1:15 PM",
            dateGroup = "Today, 26 Oct",
            paymentMode = PaymentMode.UPI,
            note = "North Indian lunch with raita"
        ),
        Expense(
            id = "e2",
            title = "Morning Chai & Samosa",
            amount = 25,
            category = ExpenseCategory.FOOD_MESS,
            dateTimeText = "Today, 26 Oct • 10:30 AM",
            dateGroup = "Today, 26 Oct",
            paymentMode = PaymentMode.UPI,
            note = "Mess canteen snack"
        ),
        Expense(
            id = "e3",
            title = "Delhi Metro Hauz Khas Top-up",
            amount = 200,
            category = ExpenseCategory.TRANSIT,
            dateTimeText = "Yesterday, 25 Oct • 5:40 PM",
            dateGroup = "Yesterday, 25 Oct",
            paymentMode = PaymentMode.UPI,
            note = "Yellow line recharge"
        ),
        Expense(
            id = "e4",
            title = "Engineering Mathematics Xerox",
            amount = 45,
            category = ExpenseCategory.STUDY_PRINT,
            dateTimeText = "Yesterday, 25 Oct • 2:10 PM",
            dateGroup = "Yesterday, 25 Oct",
            paymentMode = PaymentMode.CASH,
            note = "Tutorial sheet copies"
        ),
        Expense(
            id = "e5",
            title = "Hostel Night Maggi & Coffee",
            amount = 60,
            category = ExpenseCategory.FOOD_MESS,
            dateTimeText = "23 Oct • 11:45 PM",
            dateGroup = "23 Oct",
            paymentMode = PaymentMode.UPI,
            note = "Late night project grind"
        ),
        Expense(
            id = "e6",
            title = "Mess Extras & Fruit Juice",
            amount = 220,
            category = ExpenseCategory.FOOD_MESS,
            dateTimeText = "20 Oct • 7:15 PM",
            dateGroup = "20 Oct",
            paymentMode = PaymentMode.UPI
        ),
        Expense(
            id = "e7",
            title = "Lab Record Spiral Notebook",
            amount = 235,
            category = ExpenseCategory.STUDY_PRINT,
            dateTimeText = "18 Oct • 3:30 PM",
            dateGroup = "18 Oct",
            paymentMode = PaymentMode.CAMPUS_CARD
        ),
        Expense(
            id = "e8",
            title = "Delhi Metro Weekend Trip",
            amount = 220,
            category = ExpenseCategory.TRANSIT,
            dateTimeText = "14 Oct • 6:00 PM",
            dateGroup = "14 Oct",
            paymentMode = PaymentMode.UPI
        ),
        Expense(
            id = "e9",
            title = "Hangouts & Cafe Coffee Day",
            amount = 150,
            category = ExpenseCategory.HANGOUTS,
            dateTimeText = "12 Oct • 4:20 PM",
            dateGroup = "12 Oct",
            paymentMode = PaymentMode.UPI
        ),
        Expense(
            id = "e10",
            title = "Hostel Barber & Grooming",
            amount = 120,
            category = ExpenseCategory.GROOMING,
            dateTimeText = "5 Oct • 11:00 AM",
            dateGroup = "5 Oct",
            paymentMode = PaymentMode.CASH
        ),
        Expense(
            id = "e11",
            title = "Semester Stationery Kit",
            amount = 445,
            category = ExpenseCategory.STUDY_PRINT,
            dateTimeText = "2 Oct • 1:00 PM",
            dateGroup = "2 Oct",
            paymentMode = PaymentMode.UPI
        )
    )

    fun defaultSipPlan() = SipPlan(
        targetPercentage = 20,
        monthlyTarget = 2000,
        planName = "Campus Wealth Foundation",
        nextExecutionDate = "5 Nov (12 days)",
        currentMonthStatus = SipStatus.PLANNED,
        monthsDone = 3,
        totalMonths = 4,
        totalInvested = 6000,
        overdraftsCount = 0
    )

    fun defaultWeeklyAllocations() = listOf(
        WeeklyAllocation(
            weekNumber = 1,
            label = "Week 1",
            tag = "Current Week",
            dateRange = "Oct 24 – Oct 30",
            plannedBudget = 1200,
            spent = 580,
            cushionNote = "Under daily run-rate by ₹40",
            isCurrentWeek = true,
            isSafeZone = true
        ),
        WeeklyAllocation(
            weekNumber = 2,
            label = "Week 2",
            tag = "Social Peak",
            dateRange = "Nov 1 – Nov 7",
            plannedBudget = 1300,
            spent = 0,
            cushionNote = "Diwali hostel dinner & sweets (+₹350 cushion)",
            isCurrentWeek = false,
            isSafeZone = false
        ),
        WeeklyAllocation(
            weekNumber = 3,
            label = "Week 3",
            tag = "Low Spend Focus",
            dateRange = "Nov 8 – Nov 14",
            plannedBudget = 1300,
            spent = 0,
            cushionNote = "Mid-term exam week (High Saver)",
            isCurrentWeek = false,
            isSafeZone = true
        ),
        WeeklyAllocation(
            weekNumber = 4,
            label = "Week 4",
            tag = "Month-end Safe Zone",
            dateRange = "Nov 15 – Nov 21",
            plannedBudget = 1000,
            spent = 0,
            cushionNote = "Buffer reserve protected",
            isCurrentWeek = false,
            isSafeZone = true
        )
    )
}
