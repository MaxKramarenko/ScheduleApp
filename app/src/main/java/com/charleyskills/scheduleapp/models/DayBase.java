package com.charleyskills.scheduleapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DayBase
{
    @SerializedName("day_name")
    @Expose
    public String dayName;

    @SerializedName("day_number")
    @Expose
    public Integer dayNumber;
}
