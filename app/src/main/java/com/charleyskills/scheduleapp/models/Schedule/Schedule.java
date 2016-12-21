package com.charleyskills.scheduleapp.models.Schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Schedule
{
    @SerializedName("Group")
    @Expose
    public Group Group;

    @SerializedName("Weeks")
    @Expose
    public List<Week> weeks;
}
