package com.example.gastos_jpc.ui

import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gastos_jpc.ViewModel.TransactionViewModel
import com.example.gastos_jpc.model.Transaction
import com.example.gastos_jpc.navigation.Screens
import java.util.Date


@Composable
fun mainScreen(viewModel: TransactionViewModel, navController: NavController) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            TransactionsScreen(
                navController = navController,
                viewModel = viewModel,
                onTransactionSelected = {transaction ->  })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screens.Edit.route)
            }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar transacción")
            }
        }
    )
}
@Composable
fun TransactionsScreen(
    navController: NavController,
    viewModel: TransactionViewModel,
    onTransactionSelected: (Transaction) -> Unit) {
    val transactions by viewModel.transaction.observeAsState(emptyList())
    Column {
        Text(
            text = "Gestion",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        if (transactions != null) {
            TransactionList(
                transactions = transactions,
                onTransactionSelected = onTransactionSelected,
                viewModel = viewModel
            )
        } else {
            // Manejar el caso en que transactions es nulo
        }

    }
}

@Composable
fun TransactionList(
    transactions: List<Transaction>,
    onTransactionSelected: (Transaction) -> Unit,
    viewModel: TransactionViewModel
) {
    var isExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        state = scrollState){
        items(transactions) { transaction ->
            TransactionItem(
                transaction = transaction,
                onClick = { onTransactionSelected(transaction) },
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit,
    viewModel: TransactionViewModel,
    onClickDelete: () -> Unit = {viewModel.deleteTransaction(transaction = transaction)}
) {
//    val viewModel: YourViewModel = viewModel()
    val (isEditing, setIsEditing) = remember { mutableStateOf(false) }

    if (isEditing) {
        TransactionEditScreen(
            transaction = transaction,
            onDismiss = { setIsEditing(false) },
            onUpdate = { updatedTransaction ->
                viewModel.updateTransaction(updatedTransaction)
            }
        )
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = transaction.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${transaction.amount}",
                    fontSize = 14.sp,
                    color = if (transaction.amount >= 0) Color.Green else Color.Red
                )

                Text(
                    text = transaction.formatDate(0),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Box() {
                LazyRow(){
                 item {
                     IconButton(
                         onClick = { setIsEditing(true) },
                         Modifier.fillMaxWidth(1f)) {
                         Icon(
                             imageVector = Icons.Default.Edit,
                             contentDescription = "Edit transaction",
                             Modifier.fillMaxWidth(1f)
                         )
                     }
                     IconButton(onClick = onClickDelete,
                         Modifier.fillMaxWidth(1f)) {
                         Icon(
                             imageVector = Icons.Default.Delete,
                             contentDescription = "Delete transaction",
                             Modifier.fillMaxWidth(1f)
                         )
                     }
                 }
                }
            }
        }
    }
}


@Composable
fun TransactionEditScreen(
    transaction: Transaction,
    onDismiss: () -> Unit,
    onUpdate: (Transaction) -> Unit
) {
//    val viewModel: YourViewModel = viewModel()
    val titleState = remember { mutableStateOf(transaction.title) }
    val amountState = remember { mutableStateOf(transaction.amount) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = titleState.value,
                onValueChange = { titleState.value = it },
                label = { Text(text = "Title") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = amountState.value.toString(),
                onValueChange = {
                    if (it.isNotEmpty()) {
                        amountState.value = it.toDouble()
                    }
                },
                label = { Text(text = "Amount") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = { /* hide keyboard */ })
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Cancel")
                }
                Button(
                    onClick = {
                        val updatedTransaction = transaction.copy(
                            title = titleState.value,
                            amount = amountState.value
                        )
                        onUpdate(updatedTransaction)
                        onDismiss()
                    }
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}



@Composable
fun TransactionForm(
    navController: NavController,
    viewModel: TransactionViewModel,
    transaction: Transaction,
    onUpdateTransaction: (Transaction) -> Unit,
    onCancel: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(transaction.title) }
    var amount by rememberSaveable { mutableStateOf(transaction.amount.toString()) }
    var date by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }
    val format = transaction.formatDate(date)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = if (transaction.id != 0) "Editar transaccion" else "Agregar transaccion",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(text = "Titulo")},
            modifier = Modifier.padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text(text = "Monto")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = format,
            onValueChange = { format },
            label = { format },
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onCancel,
                modifier = Modifier
                    .padding(end = 8.dp)
            ) {
                Text(text = "Cancelar")
            }
            Button(onClick = {

                val newTransaction = Transaction(
                    id = transaction.id,
                    title = title,
                    amount = amount.toDoubleOrNull() ?: 0.0,
                    date = Date(date).toString()
                )
                viewModel.addTransaction(newTransaction)
                navController.popBackStack()

            },
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Text(text = "Guardar")
            }
        }
    }

}


//




























