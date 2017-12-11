package com.gpsavill.weighttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gpsav on 06/12/2017.
 */

class AppDatabase extends SQLiteOpenHelper {
    private static final String TAG = "AppDatabase";

    public static final String DATABASE_NAME = "WeightTracker.db";
    public static final int DATABASE_VERSION = 1;

    // Implement AppDatabase as a singleton
    private static AppDatabase instance = null;

    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "AppDatabase: constructor");
    }

    /**
     * Get an instance of the app's singleton database helper object
     *
     * @param context the content providers context.
     * @return a SQLite database helper object
     */
    static AppDatabase getInstance(Context context) {
        if (instance == null) {
            Log.d(TAG, "getInstance: creating new instance");
            instance = new AppDatabase(context);
        }
        Log.d(TAG, "getInstance: instance already exists");
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "INITIALISING DATA TABLES");
        // Create summary table
        String summarySQL;
        summarySQL = "CREATE TABLE " + SummaryContract.TABLE_NAME + " ("
                + SummaryContract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SummaryContract.Columns.SUMMARY_DATE + " INTEGER NOT NULL, "
                + SummaryContract.Columns.SUMMARY_WORKOUT_IDS + " TEXT NOT NULL);";
        Log.d(TAG, summarySQL);
        // Create workouts table
        String workoutSQL;
        workoutSQL = "CREATE TABLE " + WorkoutContract.TABLE_NAME + " ("
                + WorkoutContract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WorkoutContract.Columns.WORKOUT_EXERCISE_IDs + " TEXT NOT NULL, "
                + WorkoutContract.Columns.WORKOUT_REPS + " TEXT NOT NULL, "
                + WorkoutContract.Columns.WORKOUT_WEIGHT + " REAL NOT NULL);";
        Log.d(TAG, workoutSQL);
        // Create exercises table
        String exerciseSQL;
        exerciseSQL = "CREATE TABLE " + ExerciseContract.TABLE_NAME + " ("
                + ExerciseContract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExerciseContract.Columns.EXERCISE_NAME + " TEXT NOT NULL);";
        Log.d(TAG, exerciseSQL);

        db.execSQL(summarySQL);
        db.execSQL(workoutSQL);
        db.execSQL(exerciseSQL);

        // Pre-populate exercises table with common exercises
        ContentValues initialExercises = new ContentValues();

        initialExercises.put(ExerciseContract.Columns.EXERCISE_NAME, "Benchpress");
        db.insert(ExerciseContract.TABLE_NAME, null, initialExercises);

        initialExercises.put(ExerciseContract.Columns.EXERCISE_NAME, "Barbell row");
        db.insert(ExerciseContract.TABLE_NAME, null, initialExercises);

        initialExercises.put(ExerciseContract.Columns.EXERCISE_NAME, "Deadlift");
        db.insert(ExerciseContract.TABLE_NAME, null, initialExercises);

        initialExercises.put(ExerciseContract.Columns.EXERCISE_NAME, "Overhead press");
        db.insert(ExerciseContract.TABLE_NAME, null, initialExercises);

        initialExercises.put(ExerciseContract.Columns.EXERCISE_NAME, "Squat");
        db.insert(ExerciseContract.TABLE_NAME, null, initialExercises);

        Log.d(TAG, "DATA TABLES INITIALISED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        switch (oldVersion) {
            case 1:
                // upgrade from version 1
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion: " + newVersion);
        }
        Log.d(TAG, "onUpgrade: ends");
    }
}