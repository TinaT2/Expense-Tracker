package com.example.expensetracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

data class CategoryPresenter(val id: Int, val name: String, val color: ULong)

@Entity(tableName = "category")
data class CategoryDb(@PrimaryKey(autoGenerate = true) val id: Int, val name: String, val color: ULong)