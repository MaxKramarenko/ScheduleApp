package com.charleyskills.schedulesapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Week
{
    @SerializedName("Number")
    @Expose
    public int number;

    @SerializedName("Days")
    @Expose
    public List<Day> days = null;

}