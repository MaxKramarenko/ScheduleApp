package com.charleyskills.scheduleapp.presenter.presenters;

import com.charleyskills.scheduleapp.API.Service;
import com.charleyskills.scheduleapp.presenter.views.TimelineActivity;

public class TimelinePresenter
{
    private TimelineActivity mGroupView;
    private Service mService;

    public TimelinePresenter(TimelineActivity timelineActivity, Service service)
    {
        mGroupView = timelineActivity;
        mService = service;
    }
}
