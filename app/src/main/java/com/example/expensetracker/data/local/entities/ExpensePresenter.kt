package com.example.expensetracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ExpensePresenter(
    val id: Int,
    val name: String,
    val category: CategoryPresenter,
    val value: Int,
    val date: String,
)

@Entity(tableName = "expense")
data class ExpenseDb(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val categoryId: Int,
    val value: Int,
    val date: String,
)

fun ExpensePresenter.toDb() = ExpenseDb(
    id = id,
    name = name,
    categoryId = category.id,
    value = value,
    date = date
)

//todo fun ExpenseDb.toPresenter() = ExpensePresenter(
//    id = id,
//    name = name,
//    category = category,
//    value = value,
//    date = date
//)
