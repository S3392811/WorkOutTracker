package com.example.workouttracker

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class WorkourEntriesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dbHelper = WorkoutDatabaseHelper(this)
            WorkoutEntryListScreen(helper = dbHelper)
        }
    }
}

@Composable
fun WorkoutEntryListScreen(helper: WorkoutDatabaseHelper) {
    val workoutList = remember { mutableStateListOf<WorkoutEntity>() }

    val context = LocalContext.current


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
                        (context as Activity).finish()
                    }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Workout History",
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

            // Load workouts from database
            LaunchedEffect(Unit) {
                val data = helper.getWorkouts()
                workoutList.clear()
                workoutList.addAll(data)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (workoutList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No workout entries found.")
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(workoutList.size) { entry ->
                            WorkoutEntryCard(workoutList[entry])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutEntryCard(entry: WorkoutEntity) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (entry.status) Color(0xFFC8E6C9) else Color(
                0xFFFFCDD2
            )
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("${entry.date} at ${entry.time}", style = MaterialTheme.typography.titleMedium)
            Text("Workout Type: ${entry.type}", style = MaterialTheme.typography.bodyMedium)
            Text("Workouts: ${entry.completed}", style = MaterialTheme.typography.bodyMedium)
            Text("Duration: ${entry.duration} min", style = MaterialTheme.typography.bodyMedium)
            Text("Calories: ${entry.calories} kcal", style = MaterialTheme.typography.bodyMedium)
            Text(
                "Completed: ${if (entry.status) "Yes" else "No"}",
                style = MaterialTheme.typography.bodyMedium
            )

            val goalDuration = WorkoutTrackerData.getDuration(context = context)
            val calories = WorkoutTrackerData.getCalories(context)

            if (entry.duration > goalDuration && entry.calories > calories) {
                Text("Status : ✅ Goal Achieved")
            } else {
                Text("Status : ❌ Goal Missed")
            }
        }
    }
}


