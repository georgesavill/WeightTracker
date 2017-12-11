package com.gpsavill.weighttracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by gpsav on 06/12/2017.
 */

public class AppProvider extends ContentProvider {
    private static final String TAG = "AppProvider";

    private AppDatabase mOpenHelper;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "com.gpsavill.weighttracker.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

//
//    private static final int SUMMARY = 100;
//    private static final int SUMMARY_ID = 101;
//    private static final int SUMMARY_DATE = 102;
//    private static final int SUMMARY_WORKOUT_IDS = 103;
//
//    private static final int WORKOUT = 200;
//    private static final int WORKOUT_ID = 201;
//    private static final int WORKOUT_REPS = 202;
//    private static final int WORKOUT_WEIGHT = 203;
//    private static final int WORKOUT_EXERCISE_IDS = 204;

    private static final int EXERCISE = 300;
    private static final int EXERCISE_ID = 301;
    private static final int EXERCISE_NAME = 302;


    private static UriMatcher buildUriMatcher(){
        final   UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

//        matcher.addURI(CONTENT_AUTHORITY, SummaryContract.TABLE_NAME, SUMMARY);
//        matcher.addURI(CONTENT_AUTHORITY, SummaryContract.TABLE_NAME + "/#", SUMMARY_ID);
//
//        matcher.addURI(CONTENT_AUTHORITY, WorkoutContract.TABLE_NAME, WORKOUT);
//        matcher.addURI(CONTENT_AUTHORITY, WorkoutContract.TABLE_NAME + "/#", WORKOUT_ID);
//
        matcher.addURI(CONTENT_AUTHORITY, ExerciseContract.TABLE_NAME, EXERCISE);
        matcher.addURI(CONTENT_AUTHORITY, ExerciseContract.TABLE_NAME + "/#", EXERCISE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: called with URI " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "query: match is " + match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch(match) {
//            case SUMMARY:
//                queryBuilder.setTables(SummaryContract.TABLE_NAME);
//                break;
//
//            case SUMMARY_ID:
//                queryBuilder.setTables(SummaryContract.TABLE_NAME);
//                long summaryId = SummaryContract.getSummaryId(uri);
//                queryBuilder.appendWhere(SummaryContract.Columns._ID + " = " + summaryId);
//                break;
//
//            case WORKOUT:
//                queryBuilder.setTables(WorkoutContract.TABLE_NAME);
//                break;
//
//            case WORKOUT_ID:
//                queryBuilder.setTables(WorkoutContract.TABLE_NAME);
//                long workoutId = WorkoutContract.getWorkoutId(uri);
//                queryBuilder.appendWhere(WorkoutContract.Columns._ID + " = " + workoutId);
//                break;

            case EXERCISE:
                queryBuilder.setTables(ExerciseContract.TABLE_NAME);
                break;

            case EXERCISE_ID:
                queryBuilder.setTables(ExerciseContract.TABLE_NAME);
                long exerciseId = ExerciseContract.getExerciseId(uri);
                queryBuilder.appendWhere(ExerciseContract.Columns._ID + " = " + exerciseId);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
//        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        Log.d(TAG, "query: rows in returned cursor = " + cursor.getCount()); // TODO remove this line

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
//            case SUMMARY:
//                return SummaryContract.CONTENT_TYPE;
//
//            case SUMMARY_ID:
//                return SummaryContract.CONTENT_ITEM_TYPE;
//
//            case WORKOUT:
//                return WorkoutContract.CONTENT_TYPE;
//
//            case WORKOUT_ID:
//                return WorkoutContract.CONTENT_ITEM_TYPE;

            case EXERCISE:
                return ExerciseContract.CONTENT_TYPE;

            case EXERCISE_ID:
                return ExerciseContract.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "Entering insert, called with uri:" + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;

        Uri returnUri;
        long recordId;

        switch(match) {
//            case SUMMARY:
//                db = mOpenHelper.getWritableDatabase();
//                recordId = db.insert(SummaryContract.TABLE_NAME, null, values);
//                if(recordId >=0) {
//                    returnUri = SummaryContract.buildSummaryUri(recordId);
//                } else {
//                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
//                }
//                break;

//            case WORKOUT:
//                db = mOpenHelper.getWritableDatabase();
//                recordId = db.insert(WorkoutContract.TABLE_NAME, null, values);
//                if(recordId >=0) {
//                    returnUri = WorkoutContract.buildWorkoutUri(recordId);
//                } else {
//                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
//                }
//                break;

            case EXERCISE:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(ExerciseContract.TABLE_NAME, null, values);
                if(recordId >=0) {
                    returnUri = ExerciseContract.buildExerciseUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if (recordId >= 0) {
            // something was inserted
            Log.d(TAG, "insert: Setting notifyChanged with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            Log.d(TAG, "insert: nothing inserted");
        }

        Log.d(TAG, "Exiting insert, returning " + returnUri);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "update called with uri " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch(match) {
//            case SUMMARY:
//                db = mOpenHelper.getWritableDatabase();
//                count = db.delete(SummaryContract.TABLE_NAME, selection, selectionArgs);
//                break;
//
//            case SUMMARY_ID:
//                db = mOpenHelper.getWritableDatabase();
//                long SummaryId = SummaryContract.getSummaryId(uri);
//                selectionCriteria = SummaryContract.Columns._ID + " = " + SummaryId;
//
//                if((selection != null) && (selection.length()>0)) {
//                    selectionCriteria += " AND (" + selection + ")";
//                }
//                count = db.delete(SummaryContract.TABLE_NAME, selectionCriteria, selectionArgs);
//                break;
//
//            case WORKOUT:
//                db = mOpenHelper.getWritableDatabase();
//                count = db.delete(WorkoutContract.TABLE_NAME, selection, selectionArgs);
//                break;
//
//            case WORKOUT_ID:
//                db = mOpenHelper.getWritableDatabase();
//                long workoutId = WorkoutContract.getWorkoutId(uri);
//                selectionCriteria = WorkoutContract.Columns._ID + " = " + workoutId;
//
//                if((selection != null) && (selection.length()>0)) {
//                    selectionCriteria += " AND (" + selection + ")";
//                }
//                count = db.delete(WorkouteContract.TABLE_NAME, selectionCriteria, selectionArgs);
//                break;

            case EXERCISE:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(ExerciseContract.TABLE_NAME, selection, selectionArgs);
                break;

            case EXERCISE_ID:
                db = mOpenHelper.getWritableDatabase();
                long exerciseId = ExerciseContract.getExerciseId(uri);
                selectionCriteria = ExerciseContract.Columns._ID + " = " + exerciseId;

                if((selection != null) && (selection.length()>0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.delete(ExerciseContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if(count > 0) {
            // something was deleted
            Log.d(TAG, "delete: Setting notifyChange with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            Log.d(TAG, "delete: nothing deleted");
        }

        Log.d(TAG, "Exiting update, returning " + count);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update called with uri " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch(match) {
//            case SUMMARY:
//                db = mOpenHelper.getWritableDatabase();
//                count = db.update(SummaryContract.TABLE_NAME, values, selection, selectionArgs);
//                break;
//
//            case SUMMARY_ID:
//                db = mOpenHelper.getWritableDatabase();
//                long summaryId = SummaryContract.getSummaryId(uri);
//                selectionCriteria = SummaryContract.Columns._ID + " = " + summaryId;
//
//                if((selection != null) && (selection.length()>0)) {
//                    selectionCriteria += " AND (" + selection + ")";
//                }
//                count = db.update(SummaryContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
//                break;
//
//            case WORKOUT:
//                db = mOpenHelper.getWritableDatabase();
//                count = db.update(WorkoutContract.TABLE_NAME, values, selection, selectionArgs);
//                break;
//
//            case WORKOUT_ID:
//                db = mOpenHelper.getWritableDatabase();
//                long workoutId = WorkoutContract.getWorkoutId(uri);
//                selectionCriteria = WorkoutContract.Columns._ID + " = " + workoutId;
//
//                if((selection != null) && (selection.length()>0)) {
//                    selectionCriteria += " AND (" + selection + ")";
//                }
//                count = db.update(WorkoutContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
//                break;

            case EXERCISE:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(ExerciseContract.TABLE_NAME, values, selection, selectionArgs);
                break;

            case EXERCISE_ID:
                db = mOpenHelper.getWritableDatabase();
                long exerciseId = ExerciseContract.getExerciseId(uri);
                selectionCriteria = ExerciseContract.Columns._ID + " = " + exerciseId;

                if((selection != null) && (selection.length()>0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.update(ExerciseContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if(count > 0) {
            // something was deleted
            Log.d(TAG, "update: Setting notifyChange with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            Log.d(TAG, "update: nothing deleted");
        }

        Log.d(TAG, "Exiting update, returning " + count);
        return count;
    }
}
