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
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComponent(
    selectedDate: Date?,
    onDateSelected: (Date?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.time ?: System.currentTimeMillis()
    )

    // Button to trigger the date picker
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    OutlinedButton(
        onClick = { showDatePicker = true },
        modifier = modifier
    ) {
        Text(
            text = selectedDate?.let { dateFormat.format(it) } ?: "Select Date",
            fontSize = 18.sp
        )
    }

    // Date picker dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Convert UTC midnight to local date
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        calendar.timeInMillis = millis

                        val localCalendar = Calendar.getInstance()
                        localCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                        localCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                        localCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
                        localCalendar.set(Calendar.HOUR_OF_DAY, 0)
                        localCalendar.set(Calendar.MINUTE, 0)
                        localCalendar.set(Calendar.SECOND, 0)
                        localCalendar.set(Calendar.MILLISECOND, 0)

                        onDateSelected(localCalendar.time)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
