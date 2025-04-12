package com.example.expensetracker.ui.theme.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    padding: PaddingValues,
    onAddExpenseClick: () -> Unit
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

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.monthly_summary),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${stringResource(id = R.string.total_expenses)}: ${uiState.totalExpenses}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.expense_chart_title),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExpensePieChart(items = uiState.expenseChartData)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onAddExpenseClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.add_expense))
        }
    }
}

@Composable
fun ExpensePieChart(items: List<ExpenseChartItem>) {

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

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DashboardScreenPreview() {
    val mockState = object : DashboardUiState {
        override val totalExpenses = "$1,250"
        override val expenseChartData = listOf(
            ExpenseChartItem("Food", 400f),
            ExpenseChartItem("Transport", 150f),
            ExpenseChartItem("Shopping", 300f),
            ExpenseChartItem("Bills", 400f)
        )
    }

    ExpenseTrackerTheme {
        DashboardScreen(uiState = mockState, padding = PaddingValues(16.dp), onAddExpenseClick = {})
    }
}
