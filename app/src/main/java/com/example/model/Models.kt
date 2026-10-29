package com.example.model

enum class PayoutCadence(val label: String, val subtitle: String) {
    MONTHLY("Monthly", "Regular stipend"),
    BI_WEEKLY("Bi-Weekly", "Every 2 weeks"),
    IRREGULAR("Irregular", "Flexible top-ups")
}

enum class CommitmentCategory(val displayName: String, val iconName: String) {
    HOUSING("Housing", "home"),
    TRANSIT("Transit", "train"),
    FOOD("Food", "restaurant"),
    SUPPLIES("Supplies", "menu_book"),
    UTILITIES("Utilities", "wifi"),
    OTHER("Other", "category")
}

enum class ExpenseCategory(val displayName: String, val shortName: String, val colorHex: Long) {
    FOOD_MESS("Food & Mess", "Food", 0xFF2563EB),
    TRANSIT("Transit", "Travel", 0xFF10B981),
    STUDY_PRINT("Study & Print", "Study", 0xFF3B82F6),
    HANGOUTS("Hangouts", "Other", 0xFF93C5FD),
    GROOMING("Grooming", "Other", 0xFFF59E0B),
    OTHER("Other", "Other", 0xFF64748B)
}

enum class PaymentMode(val displayName: String) {
    UPI("UPI / GPay"),
    CASH("Cash"),
    CAMPUS_CARD("Campus Card")
}

enum class SipStatus {
    PLANNED,
    COMPLETED,
    SKIP_MONTH
}

data class StudentProfile(
    val name: String = "Darvesh Kalsi",
    val college: String = "IIT Delhi • 3rd Year B.Tech",
    val hostel: String = "Hostel Karakoram",
    val term: String = "Term 5",
    val allowanceDay: String = "1st of Month",
    val budgetHealth: String = "Optimal (Safe)",
    val avatarUrl: String = "https://lh3.googleusercontent.com/aida-public/AB6AXuAbvzvQE5s2oWl2E92BXqxHdYET-SObLW4OYF0EPRgj0TAeTYO2h0HKYu7XRDCCT9NVaM2bzNBF0_a9H1t3OBTOtvurKWNML5aJDhGffOGOqUAtqa6NFynyqwbtKpTox_JdZPUI7UwEOohqNsFZDSsrOZUk_odPEFDDvGtvq4XhZpCuwg8WhO9QW1qudGxe0Ojy37PcjyWcmSikuG_VIrQt_edh9VDO7RIIx3U5BE7A",
    val isVerified: Boolean = true,
    val morningBriefing: Boolean = true,
    val billReminders: Boolean = true,
    val cushionProtection: Boolean = true,
    val zeroGuiltMode: Boolean = true,
    val campusLanguage: String = "English"
)

data class AllowanceConfig(
    val monthlyAmount: Int = 10000,
    val nextAllowanceDate: String = "1 Dec 2024",
    val daysRemaining: Int = 19,
    val payoutCadence: PayoutCadence = PayoutCadence.MONTHLY
)

data class Commitment(
    val id: String,
    val name: String,
    val amount: Int,
    val category: CommitmentCategory,
    val dueDate: String,
    val isReserved: Boolean = true,
    val isSettled: Boolean = false,
    val relativeDueNotice: String = ""
)

data class Expense(
    val id: String,
    val title: String,
    val amount: Int,
    val category: ExpenseCategory,
    val dateTimeText: String,
    val dateGroup: String,
    val paymentMode: PaymentMode = PaymentMode.UPI,
    val note: String = ""
)

data class SipPastMonth(
    val monthName: String,
    val status: SipStatus,
    val amount: Int
)

data class SipPlan(
    val targetPercentage: Int = 20,
    val monthlyTarget: Int = 2000,
    val planName: String = "Campus Wealth Foundation",
    val nextExecutionDate: String = "5 Nov (12 days)",
    val currentMonthStatus: SipStatus = SipStatus.PLANNED,
    val monthsDone: Int = 3,
    val totalMonths: Int = 4,
    val totalInvested: Int = 6000,
    val overdraftsCount: Int = 0,
    val pastMonths: List<SipPastMonth> = listOf(
        SipPastMonth("October", SipStatus.COMPLETED, 2000),
        SipPastMonth("September", SipStatus.COMPLETED, 2000),
        SipPastMonth("August", SipStatus.SKIP_MONTH, 0)
    )
)

data class WeeklyAllocation(
    val weekNumber: Int,
    val label: String,
    val tag: String,
    val dateRange: String,
    val plannedBudget: Int,
    val spent: Int,
    val cushionNote: String = "",
    val isCurrentWeek: Boolean = false,
    val isSafeZone: Boolean = true
)
