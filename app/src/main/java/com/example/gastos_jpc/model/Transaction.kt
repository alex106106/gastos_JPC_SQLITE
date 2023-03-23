package com.example.gastos_jpc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val amount: Double = 0.0,
    var date: String = ""
)
{
    // Método para formatear la fecha como una cadena de texto
    fun formatDate(date: Long): String {
        if (date == null || date == 0L) {
            return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        } else {
            return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(date))
        }
    }

}
