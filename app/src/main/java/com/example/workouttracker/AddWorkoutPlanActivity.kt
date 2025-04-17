package com.example.workouttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddWorkoutPlanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddWorkoutScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddWorkoutScreenPreview() {

//    WorkoutEntryForm(onSubmit = {})
    AddWorkoutScreen()
}

@Composable
fun AddWorkoutScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF6200EE))
                .padding(vertical = 6.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_36),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .clickable {
                    }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Add Workout",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

        }

        WorkoutEntryForm(onSubmit = {})

    }
}

@Composable
fun WorkoutEntryForm(
    onSubmit: (WorkoutEntry) -> Unit
) {
    val workoutTypes = listOf("Cardio", "Strength", "HIIT", "Yoga", "Custom")
    val selectedType = remember { mutableStateOf(workoutTypes[0]) }
    val workoutsCompleted = remember { mutableStateOf("") }
    val duration = remember { mutableStateOf("") }
    val caloriesBurned = remember { mutableStateOf("") }
    val isCompleted = remember { mutableStateOf(false) }

    val currentDateTime = remember {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text("Today's Date & Time: $currentDateTime", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Workout Type:")
        DropdownMenuWithLabel(
            options = workoutTypes,
            selectedOption = selectedType.value,
            onOptionSelected = { selectedType.value = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = workoutsCompleted.value,
            onValueChange = { workoutsCompleted.value = it },
            label = { Text("Workouts Completed Today") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = duration.value,
            onValueChange = { duration.value = it },
            label = { Text("Duration (minutes)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = caloriesBurned.value,
            onValueChange = { caloriesBurned.value = it },
            label = { Text("Total Calories Burned") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isCompleted.value,
                onCheckedChange = { isCompleted.value = it }
            )
            Text("Mark as completed")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSubmit(
                    WorkoutEntry(
                        dateTime = currentDateTime,
                        type = selectedType.value,
                        workouts = workoutsCompleted.value,
                        duration = duration.value,
                        calories = caloriesBurned.value,
                        completed = isCompleted.value
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Workout")
        }
    }
}

@Composable
fun DropdownMenuWithLabel(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text("Workout Type") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}

data class WorkoutEntry(
    val dateTime: String,
    val type: String,
    val workouts: String,
    val duration: String,
    val calories: String,
    val completed: Boolean
)
