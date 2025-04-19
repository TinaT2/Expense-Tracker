package com.example.expensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expensetracker.data.local.entities.ExpenseDb
import com.example.expensetracker.data.local.dao.ExpenseDao

@Database(entities = [ExpenseDb::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}