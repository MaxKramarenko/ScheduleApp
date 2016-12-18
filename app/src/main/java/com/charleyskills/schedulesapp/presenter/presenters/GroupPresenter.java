package com.charleyskills.schedulesapp.presenter.presenters;

import com.charleyskills.schedulesapp.API.Service;
import com.charleyskills.schedulesapp.presenter.views.GroupActivity;

public class GroupPresenter
{
    private GroupActivity mGroupView;
    private Service mService;

    public GroupPresenter(GroupActivity groupActivity, Service service)
    {
        mGroupView = groupActivity;
        mService = service;
    }

    public void getTimetable(String groupname)
    {
//        mService.getApi().getSchedure(groupname)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<BaseResponse<Schedule>>()
//                {
//                    @Override
//                    public void onCompleted()
//                    {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e)
//                    {
//                        Log.e("schedure error", e.toString());
//                    }
//
//                    @Override
//                    public void onNext(BaseResponse<Schedule> scheduleBaseResponse)
//                    {
//                        mGroupView.displaySchedule(scheduleBaseResponse.getData().getData());
//                    }
//                });
    }
}
