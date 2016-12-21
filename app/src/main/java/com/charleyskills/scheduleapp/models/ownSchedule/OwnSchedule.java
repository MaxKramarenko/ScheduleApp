package com.charleyskills.scheduleapp.models.ownSchedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OwnSchedule
{
    @SerializedName("Weeks")
    @Expose
    public List<OwnWeek> weeks = new ArrayList<>();
}
