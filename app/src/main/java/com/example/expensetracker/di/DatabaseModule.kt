package com.example.expensetracker.di

import android.content.Context
import androidx.room.Room
import com.example.expensetracker.data.offline.AppDatabase
import com.example.expensetracker.data.offline.Dao
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "offline_database_for_transactions"
        )
            .fallbackToDestructiveMigration(true) // only for development process
            .build()
    }
    @Provides
    @Singleton
    fun provideDao(database: AppDatabase): Dao{
        return database.dao()
    }
}