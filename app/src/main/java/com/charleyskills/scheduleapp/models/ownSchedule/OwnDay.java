package com.charleyskills.scheduleapp.models.ownSchedule;

import com.charleyskills.scheduleapp.models.DayBase;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OwnDay extends DayBase
{
    @SerializedName("Pairs")
    @Expose
    public List<Pair> pairs;
}
