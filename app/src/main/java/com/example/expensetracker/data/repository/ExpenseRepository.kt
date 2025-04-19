package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.dao.ExpenseDao
import com.example.expensetracker.data.local.entities.ExpenseDb
import javax.inject.Inject

class ExpenseRepository @Inject constructor( private val expenseDao: ExpenseDao){

    fun getExpenseList() = expenseDao.getAll()

    fun insertExpense(expense: ExpenseDb) = expenseDao.insert(expense)

}