package com.example.gastos_jpc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.example.gastos_jpc.ViewModel.TransactionViewModel
import com.example.gastos_jpc.model.Transaction
import com.example.gastos_jpc.navigation.Screens
import com.example.gastos_jpc.navigation.SetupNavHost
import com.example.gastos_jpc.ui.TransactionsScreen

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<TransactionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            val scaffoldState = rememberScaffoldState()
//            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()
//
//            Scaffold(
//                scaffoldState = scaffoldState,
//                topBar = {
//                    TopAppBar(
//                        title = {
//                            Text(text = "gestion")
//                        }
//                    )
//                },
//
//                floatingActionButton = {
//                    FloatingActionButton(onClick = {
//                        navController.navigate(Screens.Edit.route)
//                    }
//                    ) {
//                        Icon(Icons.Default.Add, contentDescription = "Agregar transacción")
//                    }
//                }
//            ){
                val asd = Transaction()
                SetupNavHost(
                    navHostController = navController,
                    TransactionViewModel = viewModel,
                    onTransactionSelected = ::handleTransactionSelected,
                    onCancel = ::handleCancel,
                    transaction = asd
//                    transaction = Transaction("", "", "","")
                )
//                TransactionsScreen(
//                    viewModel = viewModel,
//                    navController = navController,
//                    onTransactionSelected = { transaction ->
//                    }
//                )
//            }
        }
    }
}

fun handleTransactionSelected(transaction: Transaction) {
    // Aquí puedes hacer lo que quieras con el objeto Transaction
    Log.d("SetupNavHost", "Transaction seleccionada: $transaction")
}

fun handleCancel() {
    // Acciones a tomar cuando el usuario cancele la transacción
}
