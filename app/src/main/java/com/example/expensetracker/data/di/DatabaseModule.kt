package com.example.expensetracker.data.di

import android.content.Context
import androidx.room.Room
import com.example.expensetracker.data.local.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase =
        Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "expense_db"
        ).build()

    @Provides
    fun provideExpenseDao(database: AppDataBase) = database.expenseDao()

}