package com.example.expensetracker.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepo @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val BALANCE_KEY = intPreferencesKey("balance")

    val BalanceFlow: Flow<Int> = dataStore.data
        .catch { exception ->
            if(exception is IOException) emit(emptyPreferences())
            else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[BALANCE_KEY] ?: 0
        }

    suspend fun updateBalance(balance: Int){
        dataStore.edit { preferences ->
            preferences[BALANCE_KEY] = balance
        }
    }
}