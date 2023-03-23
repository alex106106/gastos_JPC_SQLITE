package com.example.gastos_jpc.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gastos_jpc.ViewModel.TransactionViewModel
import com.example.gastos_jpc.constant.Screen.AddTransactionScreen
import com.example.gastos_jpc.constant.Screen.EditTransactionScreen
import com.example.gastos_jpc.model.Transaction
import com.example.gastos_jpc.ui.TransactionForm
import com.example.gastos_jpc.ui.TransactionsScreen
import com.example.gastos_jpc.ui.mainScreen

sealed class Screens(val route: String){
    object Add: Screens(route = AddTransactionScreen)
    object Edit: Screens(route = EditTransactionScreen)
}

@Composable
fun SetupNavHost(
    navHostController: NavHostController,
    TransactionViewModel: TransactionViewModel,
    transaction: Transaction,
    onCancel: () -> Unit,
    onTransactionSelected: (Transaction) -> Unit) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.Add.route
    ){
        composable(route = Screens.Add.route){
            mainScreen(
                viewModel = TransactionViewModel,
                navHostController)
        }
        composable(route = Screens.Edit.route){
            TransactionForm(
                navController = navHostController,
                transaction = transaction,
                onCancel = onCancel,
                viewModel = TransactionViewModel,
                onUpdateTransaction = {})
        }
    }
}