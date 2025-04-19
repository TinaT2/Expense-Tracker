package com.example.expensetracker.ui.theme.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.local.entities.CategoryPresenter
import com.example.expensetracker.data.local.entities.ExpenseChartPresenter
import com.example.expensetracker.data.local.entities.ExpensePresenter
import com.example.expensetracker.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DashboardUiState {
    val totalExpenses: String
    val expenseChartData: List<ExpenseChartPresenter>
    val expenseList: List<ExpensePresenter>
    val categoryList: List<CategoryPresenter>
    val selectedCategory: CategoryPresenter
}

private class MutableDashboardUiState : DashboardUiState {
    override var totalExpenses: String by mutableStateOf("0")
    override var expenseChartData: List<ExpenseChartPresenter> by mutableStateOf(emptyList())
    override var expenseList: List<ExpensePresenter> by mutableStateOf(emptyList())
    override var categoryList: List<CategoryPresenter> by mutableStateOf(emptyList())
    override var selectedCategory: CategoryPresenter by mutableStateOf(mockCategoryList()[0])
}

@HiltViewModel
class DashboardViewModel @Inject constructor(expenseRepository: ExpenseRepository) : ViewModel() {

    private val _uiState = MutableDashboardUiState()
    val uiState: DashboardUiState = _uiState

    init {
        viewModelScope.launch {
            loadDashboardData()
        }
    }

    fun loadExpenseList() {
        //todo getItFromDb
        _uiState.expenseList = mockExpenseList()
    }

    fun loadCategoryList() {
        //todo getItFromDb
        _uiState.categoryList = mockCategoryList()
    }

    fun updateSelectedCategory(categoryPresenter: CategoryPresenter){
        _uiState.selectedCategory = categoryPresenter
    }

    private suspend fun loadDashboardData() = withContext(Dispatchers.IO) {

        // Normally you'd fetch from a repo
        _uiState.totalExpenses = "$1,250"
        _uiState.expenseChartData = listOf(
            ExpenseChartPresenter("Food", 400f),
            ExpenseChartPresenter("Transport", 150f),
            ExpenseChartPresenter("Shopping", 300f),
            ExpenseChartPresenter("Bills", 400f)
        )
    }
}

fun mockExpenseList(): List<ExpensePresenter> = listOf(
    ExpensePresenter(
        id = 0,
        name = "Pizza",
        category = mockCategoryList()[0],
        value = 100,
        date = "15/1/1404"
    ),
    ExpensePresenter(
        id = 1,
        name = "TShirt",
        category = mockCategoryList()[1],
        value = 200,
        date = "12/1/1404"
    ),
    ExpensePresenter(
        id = 2,
        name = "Clening Serum",
        category = mockCategoryList()[2],
        value = 150,
        date = "10/1/1404"
    )

)

fun mockCategoryList()  = listOf(
    CategoryPresenter(id = 0, name = "Food", color = Color.Gray.value),
    CategoryPresenter(id = 1, name = "Cloth", color = Color.Cyan.value),
    CategoryPresenter(id = 2, name = "Medicine", color = Color.Green.value),
    CategoryPresenter(id = 3, name = "Transport", color = Color.Red.value),
    CategoryPresenter(id = 4, name = "Bills", color = Color.Yellow.value),
    CategoryPresenter(id = 5, name = "Other", color = Color.Magenta.value),
)