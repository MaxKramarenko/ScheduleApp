package com.charleyskills.schedulesapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Day
{
    @SerializedName("Lessons")
    @Expose
    public List<Lesson> lessons = null;

    @SerializedName("day_name")
    @Expose
    public String dayName;

    @SerializedName("day_number")
    @Expose
    public Integer dayNumber;

}