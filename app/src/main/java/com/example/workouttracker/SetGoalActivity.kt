package com.example.workouttracker

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

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

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                        (context as Activity).finish()
                    }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Daily Goal",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {


            Spacer(modifier = Modifier.height(16.dp))

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

            Button(onClick = {
                val durationGoal = goalDuration.toIntOrNull() ?: 0
                val calorieGoal = goalCalories.toIntOrNull() ?: 0

                if (durationGoal > 0 && calorieGoal > 0) {
                    // Get milestone data from database based on goal

                    WorkoutTrackerData.setGoal(context, durationGoal, calorieGoal)
                    Toast.makeText(context, "Goal set Successfully", Toast.LENGTH_SHORT).show()

//                milestoneData = dbHelper.getMilestoneData(durationGoal, calorieGoal)
                } else {
                    Toast.makeText(context, "Please enter valid goals", Toast.LENGTH_SHORT).show()
                }

            }, modifier = Modifier.fillMaxWidth()) {
                Text("Set Goal")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFC8E6C9)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Current Goal", style = MaterialTheme.typography.titleMedium)

                    Text(
                        "Duration : ${WorkoutTrackerData.getDuration(context)}",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Text(
                        "Calories : ${WorkoutTrackerData.getCalories(context)}",
                        style = MaterialTheme.typography.titleSmall
                    )


                }
            }

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

