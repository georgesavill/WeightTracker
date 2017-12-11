package com.gpsavill.weighttracker;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddExercise extends AppCompatActivity {
    private static final String TAG = "AddExercise";
    private AlertDialog mDialog = null;

    private EditText mExerciseName;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        mExerciseName = (EditText) findViewById(R.id.add_exercise_name);

        final ContentResolver contentResolver = this.getContentResolver();
        final ContentValues values = new ContentValues();

        // Show cross instead of back arrow in toolbar
        final Drawable cross = getResources().getDrawable(R.drawable.ic_clear_white_24dp);
        if(cross !=null){
            cross.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add exercise to current workout
                if(mExerciseName.length()>0) {
                    Log.d(TAG, "onClick: adding new exercise");
                    values.put(ExerciseContract.Columns.EXERCISE_NAME, mExerciseName.getText().toString());
                    contentResolver.insert(ExerciseContract.CONTENT_URI, values);
                }
            }
        });

        final TextView addExerciseReps = (TextView) findViewById(R.id.add_exercise_reps);
        addExerciseReps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: CALLED");
                addExerciseRepsDialog();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(cross);
    }

    public void addExerciseRepsDialog(){
        Log.d(TAG, "addExerciseRepsDialog: STARTS");
        final TextView mReps = (TextView) findViewById(R.id.add_exercise_reps);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_reps_dialog_title)
                .setItems(R.array.reps, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: clicked on " + which);
                        switch(which){
                            case 0:
                                mReps.setText(R.string.reps_5x5);
                                break;
                            case 1:
                                mReps.setText(R.string.reps_3x5);
                                break;
                            case 2:
                                mReps.setText(R.string.reps_1x5);
                                break;
                            case 3:
                                mReps.setText(R.string.reps_3x3);
                                break;
                            case 4:
                                mReps.setText(R.string.reps_1x3);
                                break;
                            default:
                                break;
                        }

                    }
                });
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }
}
