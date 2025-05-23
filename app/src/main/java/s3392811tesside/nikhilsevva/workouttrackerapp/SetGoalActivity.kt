package s3392811tesside.nikhilsevva.workouttrackerapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SetGoalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dbHelper = WorkoutDatabaseHelper(this)
            GoalSettingScreen(dbHelper = dbHelper)
        }
    }
}

@Composable
fun GoalSettingScreen(
    dbHelper: WorkoutDatabaseHelper
) {
    var goalDuration by remember { mutableStateOf("") }
    var goalCalories by remember { mutableStateOf("") }
    var milestoneData by remember { mutableStateOf<List<Pair<String, Boolean>>>(emptyList()) }
    var selectedType by remember { mutableStateOf("Daily") }

    val context = LocalContext.current

    fun loadGoal() {
        val duration = FitnessPrefs.fetchMinutesGoal(context, selectedType)
        val calories = FitnessPrefs.fetchCaloriesGoal(context, selectedType)

        goalDuration = if (duration > 0) duration.toString() else ""
        goalCalories = if (calories > 0) calories.toString() else ""
    }

    LaunchedEffect(selectedType) {
        loadGoal()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        // Top AppBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF6200EE))
                .padding(vertical = 6.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_36),
                contentDescription = "Back",
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .clickable { (context as Activity).finish() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "$selectedType Goal",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Column(modifier = Modifier.padding(12.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            // Chip selector
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GoalChip("Daily", selectedType) { selectedType = "Daily" }
                GoalChip("Weekly", selectedType) { selectedType = "Weekly" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Inputs
            OutlinedTextField(
                value = goalDuration,
                onValueChange = { goalDuration = it },
                label = { Text("Duration Goal (min)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = goalCalories,
                onValueChange = { goalCalories = it },
                label = { Text("Calories Goal (kcal)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val durationGoal = goalDuration.toIntOrNull() ?: 0
                    val calorieGoal = goalCalories.toIntOrNull() ?: 0

                    if (durationGoal > 0 && calorieGoal > 0) {
                        FitnessPrefs.storeWorkoutGoal(
                            context,
                            selectedType,
                            durationGoal, calorieGoal
                        )
                        Toast.makeText(
                            context,
                            "$selectedType goal set successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadGoal()
                        (context as Activity).finish()
                    } else {
                        Toast.makeText(context, "Please enter valid goals", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (goalDuration.isNotEmpty() && goalCalories.isNotEmpty()) "Update Goal" else "Add Goal")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Current Goal Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Current $selectedType Goal",
                        style = MaterialTheme.typography.titleLarge
                    )
//                    Text(
//                        "Duration: ${WorkoutTrackerData.getDuration(context, selectedType)}",
//                        style = MaterialTheme.typography.titleSmall
//                    )
//                    Text(
//                        "Calories: ${WorkoutTrackerData.getCalories(context, selectedType)}",
//                        style = MaterialTheme.typography.titleSmall
//                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${FitnessPrefs.fetchMinutesGoal(context, selectedType)}",
                                fontSize = 18.sp
                            )

                            Text(
                                text = "Duration",
                                fontSize = 14.sp
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${FitnessPrefs.fetchCaloriesGoal(context, selectedType)}",
                                fontSize = 18.sp
                            )

                            Text(
                                text = "Calories",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Milestone Section
            if (milestoneData.isNotEmpty()) {
                Text("Milestone History", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(milestoneData) { (date, completed) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(date)
                            Text(if (completed) "✅ Goal Achieved" else "❌ Missed")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GoalChip(label: String, selected: String, onClick: () -> Unit) {
    AssistChip(
        modifier = Modifier,
        onClick = onClick,
        label = { Text(label) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected == label) Color(0xFF6200EE) else Color.LightGray,
            labelColor = if (selected == label) Color.White else Color.Black
        )
    )
}





