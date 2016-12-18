package com.charleyskills.schedulesapp.models;

import java.util.ArrayList;
import java.util.List;

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
