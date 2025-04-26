package com.example.workouttracker

import android.content.Context




object FitnessPrefs {

    private const val PREF_USER = "FITNESS_USER_PREFS"
    private const val PREF_GOALS = "FITNESS_GOALS_PREFS"
    private const val KEY_AUTH_FLAG = "IS_FITNESS_AUTHENTICATED"
    private const val KEY_DISPLAY_NAME = "FITNESS_DISPLAY_NAME"
    private const val KEY_CONTACT_EMAIL = "FITNESS_CONTACT_EMAIL"

    fun updateLoginFlag(context: Context, status: Boolean) {
        val prefs = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_AUTH_FLAG, status).apply()
    }

    fun isUserAuthenticated(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_AUTH_FLAG, false)
    }

    fun saveDisplayName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_DISPLAY_NAME, name).apply()
    }

    fun getDisplayName(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE)
        return prefs.getString(KEY_DISPLAY_NAME, "") ?: ""
    }

    fun saveContactEmail(context: Context, email: String) {
        val prefs = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CONTACT_EMAIL, email).apply()
    }

    fun getContactEmail(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CONTACT_EMAIL, "") ?: ""
    }

    fun storeWorkoutGoal(context: Context, workoutType: String, minutesTarget: Int, caloriesTarget: Int) {
        val prefs = context.getSharedPreferences(PREF_GOALS, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt("${workoutType}_minutes_goal", minutesTarget)
            putInt("${workoutType}_calories_goal", caloriesTarget)
            apply()
        }
    }

    fun fetchMinutesGoal(context: Context, workoutType: String): Int {
        val prefs = context.getSharedPreferences(PREF_GOALS, Context.MODE_PRIVATE)
        return prefs.getInt("${workoutType}_minutes_goal", 0)
    }

    fun fetchCaloriesGoal(context: Context, workoutType: String): Int {
        val prefs = context.getSharedPreferences(PREF_GOALS, Context.MODE_PRIVATE)
        return prefs.getInt("${workoutType}_calories_goal", 0)
    }
}
