package com.horrorcrux.finperapp.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComponent(
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit, // Changed to be non-nullable for clearer intent
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    // Initialize the state with the selected date or today's date in UTC.
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.time ?: System.currentTimeMillis()
    )

    // Use a consistent formatter with Spanish locale
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("es-ES")) }

    // Button to trigger the date picker
    OutlinedButton(
        onClick = { showDatePicker = true },
        modifier = modifier
    ) {
        Text(
            text = selectedDate?.let { dateFormat.format(it) } ?: "Seleccionar Fecha",
            fontSize = 18.sp
        )
    }

    // Date picker dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            // The selectedDateMillis is a UTC timestamp.
                            // Simply create a Date object from it.
                            // The Date object itself doesn't have a time zone, but when you
                            // format it or convert it to a Calendar, it will use the
                            // system's default time zone.
                            onDateSelected(Date(millis))
                        }
                    },
                    // Ensure the button is only enabled when a date is selected.
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}