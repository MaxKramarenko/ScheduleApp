package com.charleyskills.schedulesapp.models;

public class BaseResponse<T>
{
    public String ContentEncoding;
    public String ContentType;
    public Data<T> Data;
    public int JsonRequestBehaviour;
    public int MaxJsonLength;
    public int RecursionLimit;

    public Data<T> getData()
    {
        return Data;
    }
}
