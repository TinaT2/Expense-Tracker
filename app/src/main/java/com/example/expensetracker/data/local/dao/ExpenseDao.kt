package com.example.expensetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetracker.data.local.entities.ExpenseDb

@Dao
interface ExpenseDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(expense: ExpenseDb)

    @Query("SELECT * FROM expense")
    fun getAll(): List<ExpenseDb>


}
