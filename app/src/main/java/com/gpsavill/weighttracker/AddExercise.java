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
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private List<String> exercises = new ArrayList<>();
    private ArrayAdapter<String> exerciseAdapter = null;

    private Spinner exerciseSpinner;
    private Spinner repsSpinner;
    private EditText weight;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        // Show cross instead of back arrow in toolbar
        final Drawable cross = getResources().getDrawable(R.drawable.ic_clear_white_24dp);
        if (cross != null) {
            cross.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
        }

        Toolbar toolbar = findViewById(R.id.add_exercise_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(cross);

        // Create filter for weight input EditText
        InputFilter filter = new InputFilter() {
            final int maxDigitsBeforeDecimalPoint = 3;
            final int maxDigitsAfterDecimalPoint = 1;

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source.subSequence(start, end).toString());

                if (!builder.toString().matches(
                        "(([1-9]{1})([0-9]{0," +
                                (maxDigitsBeforeDecimalPoint - 1) +
                                "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?"
                )) {
                    if (source.length() == 0) {
                        return dest.subSequence(dstart, dend);
                    }
                    return "";
                }
                return null;
            }
        };
        weight = findViewById(R.id.add_exercise_weight);
        weight.setFilters(new InputFilter[]{filter});

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Add exercise to current workout
                if ((!exerciseSpinner.getSelectedItem().toString().isEmpty()) &&
                    (!repsSpinner.getSelectedItem().toString().isEmpty()) &&
                    (!weight.getText().toString().isEmpty())) {

                    String mAddExerciseName = exerciseSpinner.getSelectedItem().toString();
                    String mAddExerciseReps = repsSpinner.getSelectedItem().toString();
                    Float mAddExerciseWeight = Float.valueOf(weight.getText().toString());

                    Toast.makeText(
                            AddExercise.this,
                            mAddExerciseName + ", " + mAddExerciseReps + ", " + mAddExerciseWeight,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                            AddExercise.this,
                            "EMPTY FIELD",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Create cursor containing all entries in "exercises" table
        Cursor mExerciseCursor = getContentResolver().query(
                Uri.withAppendedPath(CONTENT_AUTHORITY_URI, ExerciseContract.TABLE_NAME),
                null, null, null, null);

        // Create and populate array of exercise names
        if (mExerciseCursor.moveToFirst()) {
            do {
                exercises.add(mExerciseCursor.getString(1));
                Log.d(TAG, "Reading exercise: " + mExerciseCursor.getString(1));
            } while (mExerciseCursor.moveToNext());
        }
        mExerciseCursor.close();
        Collections.sort(exercises);

        // Create and populate spinner for number of exercise name
        exerciseSpinner = findViewById(R.id.add_exercise_name);
        exerciseAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, exercises);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(exerciseAdapter);

        // Create and populate spinner for number of reps
        repsSpinner = findViewById(R.id.add_exercise_reps);
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
                final EditText mNewExerciseName = mView.findViewById(R.id.add_new_exercise);
                final TextView mAddNewExercise = mView.findViewById(R.id.btn_add_new_exercise);
                TextView mCancelExercise = mView.findViewById(R.id.btn_cancel_new_exercise);

                mBuilder.setView(mView);
                final AlertDialog addExerciseDialog = mBuilder.create();
                addExerciseDialog.show();

                mAddNewExercise.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!mNewExerciseName.getText().toString().isEmpty()) {
                            // Add exercise to database, if it isn't already present
                            if (exercises.contains(mNewExerciseName.getText().toString())) {
                                // If exercise already exists, prompt user
                                Toast.makeText(
                                        AddExercise.this,
                                        "Exercise already exists!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // Otherwise, add new exercise
                                Log.d(TAG, "adding exercise: " + mNewExerciseName.getText().toString());
                                ContentValues newExercise = new ContentValues();
                                newExercise.put(ExerciseContract.Columns.EXERCISE_NAME, mNewExerciseName.getText().toString());
                                getContentResolver().insert(
                                        Uri.withAppendedPath(CONTENT_AUTHORITY_URI,
                                                ExerciseContract.TABLE_NAME), newExercise); // add new exercise to database
                                exercises.add(mNewExerciseName.getText().toString()); // add new exercise to array displayed in spinner
                                Collections.sort(exercises); // and sort exercise array
                                exerciseAdapter.notifyDataSetChanged();
                                addExerciseDialog.dismiss();
                            }
                        } else {
                            // EditText empty, prompt user
                            Toast.makeText(
                                    AddExercise.this,
                                    "Enter exercise name",
                                    Toast.LENGTH_SHORT).show();
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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.exercise_remove) {
            Log.d(TAG, "Exercise removed clicked");
            // Open add exercise dialog
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddExercise.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_remove_exercise, null);
            final Spinner mRemoveExerciseName = mView.findViewById(R.id.remove_exercise_name);
            final TextView mRemoveExercise = mView.findViewById(R.id.btn_remove_exercise);
            TextView mCancelRemoveExercise = mView.findViewById(R.id.btn_cancel_remove_exercise);

            // Create and populate spinner for number of exercise name
            final ArrayAdapter<String> removeExerciseAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, exercises);
            removeExerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mRemoveExerciseName.setAdapter(removeExerciseAdapter);

            // Build and display dialog
            mBuilder.setView(mView);
            final AlertDialog removeExerciseDialog = mBuilder.create();
            removeExerciseDialog.show();

            mRemoveExercise.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Remove exercise
                    Log.d(TAG, "removing exercise: " + mRemoveExerciseName.getSelectedItem().toString() + " from " + ExerciseContract.Columns.EXERCISE_NAME);
                    String mExerciseForRemoval = mRemoveExerciseName.getSelectedItem().toString();
                    getContentResolver().delete(
                            Uri.withAppendedPath(CONTENT_AUTHORITY_URI,
                                    ExerciseContract.TABLE_NAME),
                            ExerciseContract.Columns.EXERCISE_NAME + " = '" + mRemoveExerciseName.getSelectedItem().toString() + "' ", null); // remove exercise from database

                    exercises.remove(mRemoveExerciseName.getSelectedItem().toString()); // remove exercise from array displayed in spinner
                    Collections.sort(exercises); // and sort new exercise array

                    exerciseAdapter.notifyDataSetChanged();

                    // Notify user of exercise removed
                    Toast.makeText(
                            AddExercise.this,
                            mExerciseForRemoval + " removed",
                            Toast.LENGTH_SHORT).show();
                    removeExerciseDialog.dismiss();

                }
            });

            mCancelRemoveExercise.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    removeExerciseDialog.dismiss();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}
