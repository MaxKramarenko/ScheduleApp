package com.charleyskills.scheduleapp.models.ownSchedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pair
{
    @SerializedName("ID")
    @Expose
    public String id;

    @SerializedName("Week")
    @Expose
    public int week;

    @SerializedName("Day")
    @Expose
    public int day;

    @SerializedName("Number")
    @Expose
    public int number;

    @SerializedName("Name")
    @Expose
    public String name;

    @SerializedName("Teacher")
    @Expose
    public String teacher;

    @SerializedName("Room")
    @Expose
    public String room;

    @SerializedName("Type")
    @Expose
    public int type;

    @SerializedName("StartTime")
    @Expose
    public String startTime;

    @SerializedName("EndTime")
    @Expose
    public String endTime;
}
