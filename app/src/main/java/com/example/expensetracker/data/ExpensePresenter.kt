package com.example.expensetracker.data

data class ExpensePresenter(
    val id: Int,
    val name: String,
    val category: CategoryPresenter,
    val value: Int,
    val date: String,
)
