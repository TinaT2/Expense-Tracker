package com.example.expensetracker.ui.theme.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.R
import com.example.expensetracker.data.CategoryPresenter
import com.example.expensetracker.data.ExpenseChartPresenter
import com.example.expensetracker.data.ExpensePresenter
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun Dashboard(viewModel: DashboardViewModel = hiltViewModel(), padding: PaddingValues) {
    LaunchedEffect(Unit) {
        viewModel.loadExpenseList()
    }

    DashboardScreen(viewModel.uiState, padding, onAddExpenseClick = {
        //todo
    }, updateSelectedCategory = { viewModel.updateSelectedCategory(it) })
}

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    padding: PaddingValues,
    onAddExpenseClick: () -> Unit,
    updateSelectedCategory: (CategoryPresenter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.dashboard_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onAddExpenseClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.add_expense))
        }

        Spacer(modifier = Modifier.height(16.dp))


        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.expense_chart_title),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExpensePieChart(items = uiState.expenseChartData)

        Spacer(modifier = Modifier.height(24.dp))

        ExpenseList(uiState = uiState) {
            updateSelectedCategory(it)
        }


    }
}

@Composable
fun ExpensePieChart(items: List<ExpenseChartPresenter>) {

    val total = items.sumOf { it.amount.toDouble() }.toFloat()
    val proportions = items.map { it.amount / total }
    val colors = listOf(
        Color(0xFFEF5350), Color(0xFFAB47BC), Color(0xFF42A5F5), Color(0xFFFFCA28)
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    ) {
        var startAngle = -90f
        proportions.forEachIndexed { i, proportion ->
            val sweepAngle = proportion * 360f
            drawArc(
                color = colors[i % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
fun ExpenseList(uiState: DashboardUiState, updateSelectedCategory: (CategoryPresenter) -> Unit) {
    val list = uiState.expenseList
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LazyColumn {
            item {
                CategoryMenu(uiState.categoryList, uiState.selectedCategory) {
                    updateSelectedCategory(it)
                }
            }
            items(items = list) { expense ->
                ExpenseItem(expense)
            }
        }
    }
}

@Composable
fun CategoryMenu(
    categoryList: List<CategoryPresenter>,
    selectedCategory: CategoryPresenter,
    updateSelectedCategory: (CategoryPresenter) -> Unit
) {
    var expanded: Boolean by remember { mutableStateOf<Boolean>(false) }
    Row(modifier = Modifier.clickable { expanded = true }) {
        Text(text = "دسته بندی", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.weight(1f))
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categoryList.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.name) },
                    onClick = {
                        updateSelectedCategory(it)
                        expanded = false
                    })
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: ExpensePresenter) {
//    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(20)
            )
            .padding(
                vertical = 24.dp,
                horizontal = 8.dp
            )
    ) {

        Row {
            Text(text = expense.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.weight(1f))

            Text(text = expense.value.toString(), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(16.dp))
            Text(text = expense.date, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(16.dp))
            Icon(Icons.Default.MoreVert, contentDescription = "expense_more")
        }
//        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DashboardScreenPreview() {
    val mockState = object : DashboardUiState {
        override val totalExpenses = "$1,250"
        override val expenseChartData = listOf(
            ExpenseChartPresenter("Food", 400f),
            ExpenseChartPresenter("Transport", 150f),
            ExpenseChartPresenter("Shopping", 300f),
            ExpenseChartPresenter("Bills", 400f)
        )
        override val expenseList: List<ExpensePresenter>
            get() = mockExpenseList()
        override val categoryList: List<CategoryPresenter>
            get() = mockCategoryList()
        override val selectedCategory: CategoryPresenter
            get() = categoryList.first()
    }

    ExpenseTrackerTheme {
        DashboardScreen(
            uiState = mockState,
            padding = PaddingValues(16.dp),
            onAddExpenseClick = {},
            updateSelectedCategory = {})
    }
}
