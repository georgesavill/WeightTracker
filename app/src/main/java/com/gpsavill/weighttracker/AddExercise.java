package com.gpsavill.weighttracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.gpsavill.weighttracker.AppProvider.CONTENT_AUTHORITY_URI;

public class AddExercise extends AppCompatActivity {
    private static final String TAG = "AddExercise";

    private EditText mExerciseName;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        // Show cross instead of back arrow in toolbar
        final Drawable cross = getResources().getDrawable(R.drawable.ic_clear_white_24dp);
        if (cross != null) {
            cross.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add exercise to current workout
            }
        });

        // Create cursor containing all entries in "exercises" table
        Cursor mExerciseCursor = getContentResolver().query(
                Uri.withAppendedPath(CONTENT_AUTHORITY_URI, ExerciseContract.TABLE_NAME),
                null,
                null,
                null,
                null
        );

        // Create and populate array of exercise names
        final List<String> exercises = new ArrayList<>();
        if (mExerciseCursor.moveToFirst()) {
            do {
                exercises.add(mExerciseCursor.getString(1));
                Log.d(TAG, "Reading exercise: " + mExerciseCursor.getString(1));
            } while (mExerciseCursor.moveToNext());
        }
        mExerciseCursor.close();
        Collections.sort(exercises);

        // Create and populate spinner for number of exercise name
        final Spinner exerciseSpinner = findViewById(R.id.add_exercise_name);
        ArrayAdapter<CharSequence> exerciseAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, exercises);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(exerciseAdapter);

        // Create and populate spinner for number of reps
        final Spinner repsSpinner = findViewById(R.id.add_exercise_reps);
        ArrayAdapter<CharSequence> repsAdapter = ArrayAdapter.createFromResource(this,
                R.array.reps, android.R.layout.simple_spinner_item);
        repsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repsSpinner.setAdapter(repsAdapter);

        ImageButton addExercise = findViewById(R.id.btnAddExercise);
        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btnAddExercise pressed");
                // Open add exercise dialog
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddExercise.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
                final EditText mExerciseName = mView.findViewById(R.id.add_new_exercise);
                final TextView mAddExercise = mView.findViewById(R.id.btn_add_new_exercise);
                TextView mCancelExercise = mView.findViewById(R.id.btn_cancel_new_exercise);

                mBuilder.setView(mView);
                final AlertDialog addExerciseDialog = mBuilder.create();
                addExerciseDialog.show();

                mAddExercise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mExerciseName.getText().toString().isEmpty()) {
                            // Add exercise to database, if it isn't already present
                            if (exercises.contains(mExerciseName.getText().toString())) {
                                // If exercise already exists, prompt user
                                Toast.makeText(
                                        AddExercise.this,
                                        "Exercise already exists!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // Otherwise, add new exercise
                                Log.d(TAG, "adding exercise: " + mExerciseName.getText().toString());
                                ContentValues newExercise = new ContentValues();
                                newExercise.put(ExerciseContract.Columns.EXERCISE_NAME, mExerciseName.getText().toString());
                                getContentResolver().insert(
                                        Uri.withAppendedPath(CONTENT_AUTHORITY_URI,
                                                ExerciseContract.TABLE_NAME), newExercise); // add new exercise to database
                                exercises.add(mExerciseName.getText().toString()); // add new exercise to array displayed in spinner
                                Collections.sort(exercises); // and sort exercise array
                                addExerciseDialog.dismiss();
                            }
                        } else {
                            // EditText empty, prompt user
                            Toast.makeText(
                                    AddExercise.this,
                                    "Enter exercise name",
                                    Toast.LENGTH_SHORT).show();

//                            Snackbar.make(
//                                    findViewById(R.id.add_exercise_layout),
//                                    "Exercise name cannot be blank",
//                                    Snackbar.LENGTH_SHORT)
//                                    .show();
                        }

                    }
                });
                mCancelExercise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addExerciseDialog.dismiss();
                    }
                });
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(cross);


    }

//    public void addExerciseRepsDialog(){
//        Log.d(TAG, "addExerciseRepsDialog: STARTS");
//        final TextView mReps = (TextView) findViewById(R.id.add_exercise_reps);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.add_reps_dialog_title)
//                .setItems(R.array.reps, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d(TAG, "onClick: clicked on " + which);
//                        switch(which){
//                            case 0:
//                                mReps.setText(R.string.reps_5x5);
//                                break;
//                            case 1:
//                                mReps.setText(R.string.reps_3x5);
//                                break;
//                            case 2:
//                                mReps.setText(R.string.reps_1x5);
//                                break;
//                            case 3:
//                                mReps.setText(R.string.reps_3x3);
//                                break;
//                            case 4:
//                                mReps.setText(R.string.reps_1x3);
//                                break;
//                            default:
//                                break;
//                        }
//
//                    }
//                });
//        mDialog = builder.create();
//        mDialog.setCanceledOnTouchOutside(true);
//        mDialog.show();
//    }


}
