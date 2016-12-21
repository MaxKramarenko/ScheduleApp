package com.charleyskills.scheduleapp.models;

import java.util.ArrayList;

public class Data<T>
{
    public boolean Success;
    public int Status;
    public T Data;
    public ArrayList<String> Errors;

    public T getData()
    {
        return Data;
    }
}
