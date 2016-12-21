package com.charleyskills.scheduleapp.models.ownSchedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OwnWeek
{
    @SerializedName("Number")
    @Expose
    public int number;

    @SerializedName("Days")
    @Expose
    public List<OwnDay> days;
}
