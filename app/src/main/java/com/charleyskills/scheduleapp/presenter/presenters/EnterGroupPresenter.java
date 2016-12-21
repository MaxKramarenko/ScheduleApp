package com.charleyskills.scheduleapp.presenter.presenters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.charleyskills.scheduleapp.API.Service;
import com.charleyskills.scheduleapp.AppSettings;
import com.charleyskills.scheduleapp.models.BaseResponse;
import com.charleyskills.scheduleapp.models.Schedule.CurrentWeek;
import com.charleyskills.scheduleapp.models.Schedule.Group;
import com.charleyskills.scheduleapp.models.Schedule.Schedule;
import com.charleyskills.scheduleapp.presenter.views.EnterGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class EnterGroupPresenter
{
    private EnterGroup mView;
    private Service service;

    public EnterGroupPresenter(EnterGroup mView, Service service)
    {
        this.mView = mView;
        this.service = service;
    }

    public void getAllGroup()
    {
        Subscription getAllGroupsSubscription = service.api.getAllGroups()
                .subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseResponse<List<Group>>, List<String>>()
                {
                    @Override
                    public List<String> call(BaseResponse<List<Group>> listBaseResponse)
                    {
                        List<String> groupslist = new ArrayList<>();
                        for(Group group : listBaseResponse.getData().getData())
                        {
                            groupslist.add(group.fullName);
                        }
                        return groupslist;
                    }
                })
                .subscribe(new Subscriber<List<String>>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.e("RxError", e.getMessage());
                    }

                    @Override
                    public void onNext(List<String> strings)
                    {
                        mView.loadAllGroups(strings);
                    }
                });
    }

    public void setTextChangeEvents(final AutoCompleteTextView searchGroupEditText, final Button nextButton)
    {
        Subscription rxTextChangeSubscription = RxTextView.textChangeEvents(searchGroupEditText)
                .map(new Func1<TextViewTextChangeEvent, String>()
                {
                    @Override
                    public String call(TextViewTextChangeEvent textViewTextChangeEvent)
                    {
                        return textViewTextChangeEvent.text().toString();
                    }
                })
                .filter(new Func1<String, Boolean>()
                {
                    @Override
                    public Boolean call(String s)
                    {
                        mView.setEnableNextButton(s.length() > 0);
                        return s.length() > 1;
                    }
                })
                .subscribe(new Subscriber<String>()
                {
                    @Override
                    public void onCompleted()
                    {
                        mView.setEditTextProgressBarVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mView.setEditTextProgressBarVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onNext(String s)
                    {
                        mView.setArrayAdapter(s);
                        mView.setEditTextProgressBarVisibility(View.INVISIBLE);
                    }
                });
    }

    public void setNextButtonOnClickListener(final Button nextButton, final AutoCompleteTextView searchGroupEditText, final Context context)
    {
        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mView.setEditTextProgressBarVisibility(View.VISIBLE);
                nextButton.setEnabled(false);
                searchGroupEditText.setEnabled(false);
                service.api.getSchedule(searchGroupEditText.getText().toString())
                        .subscribeOn(Schedulers.computation())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(new Action1<Throwable>()
                        {
                            @Override
                            public void call(Throwable throwable)
                            {
                                mView.setEditTextProgressBarVisibility(View.INVISIBLE);
                                nextButton.setEnabled(true);
                                searchGroupEditText.setEnabled(true);
                                Log.e("RxError", throwable.getMessage());
                                Toast.makeText(mView.context, "Данной группы нету в расписании", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .doOnNext(new Action1<BaseResponse<Schedule>>()
                        {
                            @Override
                            public void call(BaseResponse<Schedule> scheduleBaseResponse)
                            {
                                AppSettings.schedule = scheduleBaseResponse.getData().getData();
                                AppSettings.group = searchGroupEditText.getText().toString();

                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(searchGroupEditText.getWindowToken(), 0);
                            }
                        })
                        .flatMap(new Func1<BaseResponse<Schedule>, Observable<BaseResponse<CurrentWeek>>>()
                        {
                            @Override
                            public Observable<BaseResponse<CurrentWeek>> call(BaseResponse<Schedule> scheduleBaseResponse)
                            {
                                return service.api.getCurrentWeek()
                                        .subscribeOn(Schedulers.computation())
                                        .unsubscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread());
                            }
                        })
                        .subscribe(new Subscriber<BaseResponse<CurrentWeek>>()
                        {
                            @Override
                            public void onCompleted()
                            {
                                mView.setEditTextProgressBarVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError(Throwable e)
                            {
                                mView.setEditTextProgressBarVisibility(View.INVISIBLE);
                                nextButton.setEnabled(true);
                                searchGroupEditText.setEnabled(true);
                                Log.e("RxError", e.getMessage());
                            }

                            @Override
                            public void onNext(BaseResponse<CurrentWeek> currentWeekBaseResponse)
                            {
                                mView.setEditTextProgressBarVisibility(View.INVISIBLE);
                                AppSettings.currentWeek = AppSettings.showWeek = currentWeekBaseResponse.getData().getData().currentWeek;
                                mView.startGroupActivity();
                            }
                        });
            }
        });
    }
}
