package com.charleyskills.scheduleapp.API;

import com.charleyskills.scheduleapp.models.BaseResponse;
import com.charleyskills.scheduleapp.models.Schedule.CurrentWeek;
import com.charleyskills.scheduleapp.models.Schedule.Group;
import com.charleyskills.scheduleapp.models.Schedule.Schedule;
import com.charleyskills.scheduleapp.models.ownSchedule.OwnSchedule;
import com.charleyskills.scheduleapp.models.ownSchedule.Pair;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public class Service
{
    private static final String SERVER_URL = "http://192.168.0.100/skapi/";
    public Api api;

    public Service()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);
    }

    public interface Api
    {
        @Headers({ "Accept: application/json" })
        @GET("schedule/weeks")
        public Observable<BaseResponse<CurrentWeek>> getCurrentWeek();

        @Headers({ "Accept: application/json" })
        @GET("schedule/groups/{name}")
        public Observable<BaseResponse<Group>> getGroupByName(@Path("name") String groupname);

        @Headers({ "Accept: application/json" })
        @GET("schedule/groups")
        public Observable<BaseResponse<List<Group>>> getAllGroups();

        @Headers({ "Accept: application/json" })
        @GET("schedule/groups/search/{name}")
        public Observable<BaseResponse<List<Group>>> searchGroups(@Path("name") String groupname);

        @Headers({ "Accept: application/json" })
        @GET("schedule/timetable/{name}")
        public Observable<BaseResponse<Schedule>> getSchedule(@Path("name") String groupname);

        //////////////////////////

        @Headers({ "Accept: application/json" })
        @PUT("managedschedule")
        public Observable<BaseResponse<String>> updateManageSchedule(@Body Pair pair);

        @Headers({ "Accept: application/json" })
        @GET("managedschedule")
        public Observable<BaseResponse<OwnSchedule>> getManageSchedule();

        @Headers({ "Accept: application/json" })
        @DELETE("managedschedule/{id}")
        public Observable<BaseResponse<Boolean>> deleteManageSchedule(@Path("id") String id);
    }
}
