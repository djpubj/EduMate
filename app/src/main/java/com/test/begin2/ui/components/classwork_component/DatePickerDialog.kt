package com.test.begin2.ui.components.classwork_component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(onSuccess: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val selectedDate = remember { mutableStateOf("Set Due Date") }
    val showDatePicker = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Initialize the DatePicker state
    val initialDate = System.currentTimeMillis()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate,
        yearRange = 2000..2030
    )

    // Main UI
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { coroutineScope.launch { showDatePicker.value = true } },
                modifier = Modifier.height(60.dp),
                colors = ButtonDefaults.buttonColors(Color.LightGray.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(0.dp),
            ) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.padding(start = 0.dp)
                )
                Text(
                    text = "${selectedDate.value}", modifier = Modifier
                        .weight(1f)
                        .padding(start = 30.dp), color = Color.Black

                )
            }

        }

        // Show DatePickerDialog
        if (showDatePicker.value) {
            androidx.compose.material3.DatePickerDialog(
                onDismissRequest = { showDatePicker.value = false },
                confirmButton = {
                    Button(onClick = {
                        coroutineScope.launch {
                            // Get the selected date in milliseconds
                            val selectedMillis = datePickerState.selectedDateMillis
                            if (selectedMillis != null) {
                                // Format the date
                                calendar.timeInMillis = selectedMillis
                                val day = calendar.get(Calendar.DAY_OF_MONTH)
                                val month = calendar.get(Calendar.MONTH) + 1 // Months are 0-based
                                val year = calendar.get(Calendar.YEAR)
                                selectedDate.value = "Due $day-$month-$year"
                            }
                            onSuccess(selectedDate.value)
                            showDatePicker.value = false
                        }
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDatePicker.value = false }) {
                        Text("Cancel")
                    }
                },
                content = {
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    }
}
