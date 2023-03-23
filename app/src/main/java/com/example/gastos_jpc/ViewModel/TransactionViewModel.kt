package com.example.gastos_jpc.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gastos_jpc.DB.AppDatabase
import com.example.gastos_jpc.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transaction: LiveData<List<Transaction>> = _transactions

    init {
        loadTransactions()
    }

    private fun loadTransactions() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val transactions = db.transactionDAO().getAllTransactions()
            withContext(Dispatchers.Main){
                _transactions.value = transactions
            }
        }
    }

    fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            withContext(Dispatchers.IO){
                db.transactionDAO().insert(transaction)
                loadTransactions()
            }
        }
    }
    fun updateTransaction(transaction: Transaction){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                withContext(Dispatchers.IO){
                    db.transactionDAO().update(transaction)
                    loadTransactions()
                }
            }
        }
    }
    fun deleteTransaction(transaction: Transaction){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                withContext(Dispatchers.IO){
                    db.transactionDAO().delete(transaction)
                    loadTransactions()
                }
            }
        }
    }
}