package com.charleyskills.schedulesapp.API;

import com.charleyskills.schedulesapp.models.BaseResponse;
import com.charleyskills.schedulesapp.models.CurrentWeek;
import com.charleyskills.schedulesapp.models.Group;
import com.charleyskills.schedulesapp.models.Schedule;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

public class Service
{
    private static final String SERVER_URL = "http://192.168.0.100/skapi/";
    private Service.api mAPI;

    public Service()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mAPI = retrofit.create(api.class);
    }

    public api getApi()
    {
        return mAPI;
    }

    public interface api
    {
        @Headers({
                "Accept: application/json",
        })
        @GET("schedule/groups/{name}")
        public Observable<BaseResponse<Group>> getGroupByName(@Path("name") String groupname);

        @Headers({
                "Accept: application/json",
        })
        @GET("schedule/groups")
        public Observable<BaseResponse<List<Group>>> getAllGroups();

        @Headers({
                "Accept: application/json",
        })
        @GET("schedule/groups/search/{name}")
        public Observable<BaseResponse<List<Group>>> searchGroups(@Path("name") String groupname);

        @Headers({
                "Accept: application/json",
        })
        @GET("schedule/timetable/{name}")
        public Observable<BaseResponse<Schedule>> getSchedure(@Path("name") String groupname);

        @GET("schedule/weeks")
        Observable<BaseResponse<CurrentWeek>> getCurrentWeek();
    }
}
