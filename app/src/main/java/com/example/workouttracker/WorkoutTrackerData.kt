package com.example.workouttracker

import android.content.Context




object WorkoutTrackerData {

    fun writeLS(context: Context, value: Boolean) {
        val userLogin = context.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)
        val editor = userLogin.edit()
        editor.putBoolean("LOGIN_STATUS", value).apply()
    }

    fun readLS(context: Context): Boolean {
        val userLogin = context.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)
        return userLogin.getBoolean("LOGIN_STATUS", false)
    }

    fun writeUserName(context: Context, value: String) {
        val userLogin = context.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)
        val editor = userLogin.edit()
        editor.putString("USERNAME", value).apply()
    }

    fun readUserName(context: Context): String {
        val userLogin = context.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)
        return userLogin.getString("USERNAME", "")!!
    }

    fun writeMail(context: Context, value: String) {
        val userLogin = context.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)
        val editor = userLogin.edit()
        editor.putString("USERMAIL", value).apply()
    }

    fun readMail(context: Context): String {
        val userLogin = context.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)
        return userLogin.getString("USERMAIL", "")!!
    }

    fun setGoal(context: Context, duration: Int,calories: Int) {
        val userLogin = context.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)
        val editor = userLogin.edit()
        editor.putInt("DURATION", duration).apply()
        editor.putInt("CALORIES",calories).apply()
    }

    fun getDuration(context: Context): Int {
        val userLogin = context.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)
        return userLogin.getInt("DURATION", 0)
    }

    fun getCalories(context: Context): Int {
        val userLogin = context.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)
        return userLogin.getInt("CALORIES", 0)
    }




}