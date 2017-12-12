package com.gpsavill.weighttracker;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.gpsavill.weighttracker.AppProvider.CONTENT_AUTHORITY;
import static com.gpsavill.weighttracker.AppProvider.CONTENT_AUTHORITY_URI;

/**
 * Created by gpsav on 06/12/2017.
 */

public class WorkoutContract {

    static final String TABLE_NAME = "Workout";

    // Summary fields
    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String WORKOUT_REPS = "Reps";
        public static final String WORKOUT_WEIGHT = "Weight";
        public static final String WORKOUT_EXERCISE_NAME = "Exercise_Name";
        public static final String WORKOUT_STATUS = "Workout_Status"; // 1 for complete, 0 for in-progress

        private Columns(){
            // private constructor to prevent accidental instantiation
        }
    }

    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    static Uri buildWorkoutUri(long summaryId){
        return ContentUris.withAppendedId(CONTENT_URI, summaryId);
    }

    static long getWorkoutId(Uri uri){
        return ContentUris.parseId(uri);
    }
}
