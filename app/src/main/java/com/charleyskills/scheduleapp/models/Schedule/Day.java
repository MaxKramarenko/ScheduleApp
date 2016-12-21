package com.charleyskills.scheduleapp.models.Schedule;

import com.charleyskills.scheduleapp.models.DayBase;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Day extends DayBase
{
    @SerializedName("Lessons")
    @Expose
    public List<Lesson> lessons = null;
}