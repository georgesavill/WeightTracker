package com.gpsavill.weighttracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gpsav on 05/12/2017.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private List<Workout> mWorkoutList;
    private Context mContext;

    public RecyclerViewAdapter(Context context, List<Workout> workoutList){
        mContext = context;
        mWorkoutList = workoutList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Called when new view is required
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_main, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ItemViewHolder holder, int position) {
        // Called when new row is required
        if((mWorkoutList == null) || (mWorkoutList.size() == 0)){
            // Set placeholder text when no workouts exist
            holder.date.setText(R.string.no_workouts_placeholder);
            holder.summary.setText("");
        } else {
            // TODO: code to populate TextViews
        }
    }

    @Override
    public int getItemCount() {
        return ((mWorkoutList != null) && (mWorkoutList.size() !=0) ? mWorkoutList.size() : 1);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "ItemViewHolder";
        TextView date = null;
        TextView summary = null;

        public ItemViewHolder(View itemView){
            super(itemView);
            Log.d(TAG, "ItemViewHolder: starts");
            this.date = (TextView) itemView.findViewById(R.id.item_date);
            this.summary = (TextView) itemView.findViewById(R.id.item_summary);
        }
    }
}
