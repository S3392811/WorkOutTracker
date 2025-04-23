package com.example.workouttracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
