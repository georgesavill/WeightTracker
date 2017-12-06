package com.gpsavill.weighttracker;

import java.io.Serializable;

/**
 * Created by gpsav on 05/12/2017.
 */

class Workout implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mDate;
    private String mSummary;

    public Workout(String date, String summary){
        mDate = date;
        mSummary = summary;
    }

    public String getDate() {
        return mDate;
    }

    public String getSummary() {
        return mSummary;
    }
}
