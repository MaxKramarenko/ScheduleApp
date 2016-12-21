package com.charleyskills.scheduleapp.models.Schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lesson
{
    @SerializedName("Name")
    @Expose
    public String name;

    @SerializedName("Type")
    @Expose
    public String type;

    @SerializedName("Number")
    @Expose
    public int number;

    @SerializedName("Start")
    @Expose
    public String start;

    @SerializedName("End")
    @Expose
    public String end;

    @SerializedName("ClassRoom")
    @Expose
    public ClassRoom classRoom;

    @SerializedName("Teacher")
    @Expose
    public String teacher;

}
