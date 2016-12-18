package com.charleyskills.schedulesapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassRoom
{
    @SerializedName("Room")
    @Expose
    public String Room;

    @SerializedName("Building")
    @Expose
    public String Building;
}
