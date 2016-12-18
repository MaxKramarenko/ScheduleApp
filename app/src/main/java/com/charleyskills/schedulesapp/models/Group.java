package com.charleyskills.schedulesapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group
{
    @SerializedName("ID")
    @Expose
    public Integer Id;

    @SerializedName("FullName")
    @Expose
    public String fullName;

    @SerializedName("Prefix")
    @Expose
    public String prefix;

    @SerializedName("Okr")
    @Expose
    public String okr;

    @SerializedName("Type")
    @Expose
    public String type;

    @SerializedName("Url")
    @Expose
    public String url;
}