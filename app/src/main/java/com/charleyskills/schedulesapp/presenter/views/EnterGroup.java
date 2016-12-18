package com.charleyskills.schedulesapp.presenter.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.charleyskills.schedulesapp.API.Service;
import com.charleyskills.schedulesapp.AppSettings;
import com.charleyskills.schedulesapp.R;
import com.charleyskills.schedulesapp.models.BaseResponse;
import com.charleyskills.schedulesapp.models.CurrentWeek;
import com.charleyskills.schedulesapp.models.Group;
import com.charleyskills.schedulesapp.models.Schedule;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class EnterGroup extends AppCompatActivity
{
    @InjectView(R.id.nextButton)
    Button nextButton;

    @InjectView(R.id.searchGroupEditText)
    AutoCompleteTextView searchGroupEditText;

    @InjectView(R.id.editTextProgressBar)
    ProgressBar editTextProgressBar;

    @InjectView(R.id.searchGroupLayout)
    LinearLayout searchGroupLayout;

    @InjectView(R.id.loadAllGroupsProgressBar)
    ProgressBar loadAllGroupsProgressBar;

    public static Context context;
    private List<String> allGroupsPair = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_group);

        ButterKnife.inject(this);

        context = this;

        loadAllGroupsProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        final Service service = new Service();

        Subscription getAllGroupsSubscription = service.getApi().getAllGroups()
                .subscribeOn(Schedulers.newThread())
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
                        searchGroupLayout.setVisibility(View.VISIBLE);
                        loadAllGroupsProgressBar.setVisibility(View.GONE);
                        allGroupsPair = strings;
                    }
                });

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
                        nextButton.setEnabled(s.length() > 0);
                        return s.length() > 1;
                    }
                })
                .subscribe(new Subscriber<String>()
                {
                    @Override
                    public void onCompleted()
                    {
                        editTextProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        editTextProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onNext(String s)
                    {
                        List<String> temp = new ArrayList<String>();
                        for(String aas : allGroupsPair)
                        {
                            if(aas.startsWith(s.toLowerCase()))
                                temp.add(aas);
                        }
                        setArrayAdapter(temp);

                        editTextProgressBar.setVisibility(View.INVISIBLE);
                    }
                });

        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                editTextProgressBar.setVisibility(View.VISIBLE);
                service.getApi().getSchedure(searchGroupEditText.getText().toString())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(new Action1<Throwable>()
                        {
                            @Override
                            public void call(Throwable throwable)
                            {
                                editTextProgressBar.setVisibility(View.INVISIBLE);
                                Log.e("RxError", throwable.getMessage());
                                Toast.makeText(context, "Данной группы нету в расписании", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .doOnNext(new Action1<BaseResponse<Schedule>>()
                        {
                            @Override
                            public void call(BaseResponse<Schedule> scheduleBaseResponse)
                            {
                                AppSettings.currentSchedule = scheduleBaseResponse.getData().getData();
                                AppSettings.currentGroup = searchGroupEditText.getText().toString();
                            }
                        })
                        .flatMap(new Func1<BaseResponse<Schedule>, Observable<BaseResponse<CurrentWeek>>>()
                        {
                            @Override
                            public Observable<BaseResponse<CurrentWeek>> call(BaseResponse<Schedule> scheduleBaseResponse)
                            {
                                return service.getApi().getCurrentWeek()
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread());
                            }
                        })
                        .subscribe(new Subscriber<BaseResponse<CurrentWeek>>()
                        {
                            @Override
                            public void onCompleted()
                            {
                                editTextProgressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError(Throwable e)
                            {
                                editTextProgressBar.setVisibility(View.INVISIBLE);
                                Log.e("RxError", e.getMessage());
                            }

                            @Override
                            public void onNext(BaseResponse<CurrentWeek> currentWeekBaseResponse)
                            {
                                editTextProgressBar.setVisibility(View.INVISIBLE);
                                AppSettings.currentWeek = AppSettings.showedWeek = currentWeekBaseResponse.getData().getData().currentWeek;
                                Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });
    }

    private void setArrayAdapter(List<String> strings)
    {
        searchGroupEditText.setAdapter(new ArrayAdapter<String>(EnterGroup.this, android.R.layout.simple_dropdown_item_1line, strings));
        searchGroupEditText.showDropDown();
    }
}
