package com.example.expensetracker.ui.theme.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DashboardUiState {
    val totalExpenses: String
    val expenseChartData: List<ExpenseChartItem>
}

data class ExpenseChartItem(
    val category: String,
    val amount: Float
)

private class MutableDashboardUiState : DashboardUiState {
    override var totalExpenses: String by mutableStateOf("0")
    override var expenseChartData: List<ExpenseChartItem> by mutableStateOf(emptyList())
}

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableDashboardUiState()
    val uiState: DashboardUiState = _uiState

    init {
        viewModelScope.launch {
            loadDashboardData()
        }
    }

    private suspend fun loadDashboardData() = withContext(Dispatchers.IO) {

        // Normally you'd fetch from a repo
        _uiState.totalExpenses = "$1,250"
        _uiState.expenseChartData = listOf(
            ExpenseChartItem("Food", 400f),
            ExpenseChartItem("Transport", 150f),
            ExpenseChartItem("Shopping", 300f),
            ExpenseChartItem("Bills", 400f)
        )
    }
}