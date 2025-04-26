package com.example.workouttracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WorkoutDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "workout.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE workouts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT,
                time TEXT,
                type TEXT,
                completed TEXT,
                duration INTEGER,
                calories INTEGER,
                status INTEGER
            )
        """
        )
        db.execSQL(
            """
            CREATE TABLE goals (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                goal TEXT,
                achieved INTEGER
            )
        """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS workouts")
        db.execSQL("DROP TABLE IF EXISTS goals")
        onCreate(db)
    }

    fun insertWorkout(entry: WorkoutEntity) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("date", entry.date)
            put("time", entry.time)
            put("type", entry.type)
            put("completed", entry.completed)
            put("duration", entry.duration)
            put("calories", entry.calories)
            put("status", if (entry.status) 1 else 0)
        }
        db.insert("workouts", null, values)
    }

    fun getWorkouts(): List<WorkoutEntity> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM workouts ORDER BY date DESC", null)
        val list = mutableListOf<WorkoutEntity>()

        while (cursor.moveToNext()) {
            list.add(
                WorkoutEntity(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    date = cursor.getString(cursor.getColumnIndexOrThrow("date")),
                    time = cursor.getString(cursor.getColumnIndexOrThrow("time")),
                    type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
                    completed = cursor.getString(cursor.getColumnIndexOrThrow("completed")),
                    duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration")),
                    calories = cursor.getInt(cursor.getColumnIndexOrThrow("calories")),
                    status = cursor.getInt(cursor.getColumnIndexOrThrow("status")) == 1
                )
            )
        }

        cursor.close()
        return list
    }

    fun insertGoal(goal: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("goal", goal)
            put("achieved", 0)
        }
        db.insert("goals", null, values)
    }

    fun getGoals(): List<Pair<String, Boolean>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM goals", null)
        val list = mutableListOf<Pair<String, Boolean>>()
        while (cursor.moveToNext()) {
            list.add(cursor.getString(1) to (cursor.getInt(2) == 1))
        }
        cursor.close()
        return list
    }

    fun getMilestoneData(goalDuration: Int, goalCalories: Int): List<Pair<String, Boolean>> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT date, SUM(duration) as totalDuration, SUM(calories) as totalCalories
            FROM workouts
            GROUP BY date
            ORDER BY date DESC
            """.trimIndent(), null
        )

        val result = mutableListOf<Pair<String, Boolean>>()

        while (cursor.moveToNext()) {
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            val totalDuration = cursor.getInt(cursor.getColumnIndexOrThrow("totalDuration"))
            val totalCalories = cursor.getInt(cursor.getColumnIndexOrThrow("totalCalories"))

            val completed = totalDuration >= goalDuration && totalCalories >= goalCalories
            result.add(date to completed)
        }

        cursor.close()
        return result
    }

    fun getWeeklyGoalSummary(weeklyGoalDuration: Int, weeklyGoalCalories: Int): List<Triple<String, Int, Int>> {
        val db = readableDatabase
        val calendar = Calendar.getInstance()

        val results = mutableListOf<Triple<String, Int, Int>>() // WeekRange, percentage, totalDuration

        for (i in 0 until 4) {
            val end = calendar.time
            calendar.add(Calendar.DATE, -6)
            val start = calendar.time
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val startDate = formatter.format(start)
            val endDate = formatter.format(end)

            val cursor = db.rawQuery(
                """
            SELECT SUM(duration) as totalDuration, SUM(calories) as totalCalories 
            FROM workouts 
            WHERE date BETWEEN ? AND ?
            """, arrayOf(startDate, endDate)
            )

            if (cursor.moveToFirst()) {
                val duration = cursor.getInt(cursor.getColumnIndexOrThrow("totalDuration"))
                val calories = cursor.getInt(cursor.getColumnIndexOrThrow("totalCalories"))

                val percentage = if (weeklyGoalDuration > 0 && weeklyGoalCalories > 0) {
                    val durationPercent = (duration * 100) / weeklyGoalDuration
                    val caloriesPercent = (calories * 100) / weeklyGoalCalories
                    minOf(durationPercent, caloriesPercent)
                } else 0

                results.add(Triple("$startDate to $endDate", percentage.coerceAtMost(100), duration))
            }

            cursor.close()
            calendar.add(Calendar.DATE, -1) // Move to the previous week
        }

        return results.reversed() // So the oldest is first
    }

}

data class WorkoutEntity(
    val id: Int = 0,
    val date: String,
    val time: String,
    val type: String,
    val completed: String,
    val duration: Int,
    val calories: Int,
    val status: Boolean
)
