package com.charleyskills.scheduleapp.presenter.views;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.charleyskills.scheduleapp.API.Service;
import com.charleyskills.scheduleapp.AppSettings;
import com.charleyskills.scheduleapp.R;
import com.charleyskills.scheduleapp.models.BaseResponse;
import com.charleyskills.scheduleapp.models.ownSchedule.OwnSchedule;
import com.charleyskills.scheduleapp.presenter.adapters.SmartFragmentStatePagerAdapter;
import com.charleyskills.scheduleapp.presenter.views.fragments.OwnScheduleFragment;
import com.charleyskills.scheduleapp.presenter.views.fragments.ScheduleFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TimelineActivity extends AppCompatActivity implements ScheduleFragment.OnFragmentInteractionListener, OwnScheduleFragment.OnFragmentInteractionListener
{
    @InjectView(R.id.vpPager)
    ViewPager vpPager;

    @InjectView(R.id.firstWeekTextView)
    TextView firstWeekTextView;

    @InjectView(R.id.secondWeekTextView)
    TextView secondWeekTextView;

    @InjectView(R.id.coverFrameLayout)
    FrameLayout coverFrameLayout;

    @InjectView(R.id.coverProgressBar)
    ProgressBar coverProgressBar;

    @InjectView(R.id.toolbarTextView)
    TextView toolbarTextView;

    @InjectView(R.id.addPair)
    TextView addPair;

    @InjectView(R.id.absentPairsTextView)
    TextView absentPairsTextView;

//    TimelinePresenter mGroupPresenter;
    public Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ButterKnife.inject(this);

        coverProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        if (AppSettings.currentWeek == 1)
        {
            firstWeekTextView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        else
        {
            secondWeekTextView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

        SmartFragmentStatePagerAdapter adapterViewPager = new ScheduleAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        checkDayIsAbsent();

        firstWeekTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(AppSettings.showWeek == 1)
                    return;

                firstWeekTextView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                secondWeekTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                AppSettings.showWeek = 1;

                if (toolbarTextView.getText().toString().equals("KPI Schedule"))
                {
                    coverFrameLayout.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            SmartFragmentStatePagerAdapter adapterViewPager = new ScheduleAdapter(getSupportFragmentManager());
                            vpPager.setAdapter(adapterViewPager);

                            checkDayIsAbsent();

                            coverFrameLayout.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
                        }
                    }).start();
                }
                else
                {
                    coverFrameLayout.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            SmartFragmentStatePagerAdapter adapterViewPager = new OwnScheduleAdapter(getSupportFragmentManager());
                            vpPager.setAdapter(adapterViewPager);

                            checkDayIsAbsent();

                            coverFrameLayout.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
                        }
                    }).start();
                }
            }
        });

        secondWeekTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(AppSettings.showWeek == 2)
                    return;

                secondWeekTextView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                firstWeekTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                AppSettings.showWeek = 2;

                if (toolbarTextView.getText().toString().equals("KPI Schedule"))
                {
                    coverFrameLayout.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            SmartFragmentStatePagerAdapter adapterViewPager = new ScheduleAdapter(getSupportFragmentManager());
                            vpPager.setAdapter(adapterViewPager);

                            checkDayIsAbsent();

                            coverFrameLayout.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
                        }
                    }).start();
                }
                else
                {
                    coverFrameLayout.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            SmartFragmentStatePagerAdapter adapterViewPager = new OwnScheduleAdapter(getSupportFragmentManager());
                            vpPager.setAdapter(adapterViewPager);

                            checkDayIsAbsent();

                            coverFrameLayout.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
                        }
                    }).start();
                }
            }
        });

        service = new Service();

        addPair.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivityForResult(new Intent(getApplicationContext(), AddPairActivity.class), 0);
                coverFrameLayout.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).start();
            }
        });

        toolbarTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (AppSettings.ownSchedule == null)
                {
                    coverFrameLayout.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).start();
                    toolbarTextView.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).start();

                    service.api.getManageSchedule()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BaseResponse<OwnSchedule>>()
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
                            public void onNext(BaseResponse<OwnSchedule> ownScheduleBaseResponse)
                            {
                                AppSettings.ownSchedule = ownScheduleBaseResponse.getData().getData();

                                if (toolbarTextView.getText().toString().equals("KPI Schedule"))
                                {
                                    toolbarTextView.setText("My Schedule");

                                    SmartFragmentStatePagerAdapter adapterViewPager = new OwnScheduleAdapter(getSupportFragmentManager());
                                    vpPager.setAdapter(adapterViewPager);

                                    checkDayIsAbsent();
                                }
                                else
                                {
                                    toolbarTextView.setText("KPI Schedule");

                                    SmartFragmentStatePagerAdapter adapterViewPager = new ScheduleAdapter(getSupportFragmentManager());
                                    vpPager.setAdapter(adapterViewPager);

                                    checkDayIsAbsent();
                                }

                                coverFrameLayout.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).withStartAction(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        addPair.setVisibility(View.VISIBLE);
                                        addPair.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
                                    }
                                }).start();
                                toolbarTextView.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
                            }
                        });
                }
                else
                {
                    toolbarTextView.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).start();
                    coverFrameLayout.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).withStartAction(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (toolbarTextView.getText().toString().equals("My Schedule"))
                            {
                                addPair.animate().alpha(0).setDuration(220).setInterpolator(new AccelerateInterpolator()).withEndAction(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        addPair.setVisibility(View.INVISIBLE);
                                    }
                                }).start();
                            }
                        }
                    }).withEndAction(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (toolbarTextView.getText().toString().equals("KPI Schedule"))
                            {
                                toolbarTextView.setText("My Schedule");

                                SmartFragmentStatePagerAdapter adapterViewPager = new OwnScheduleAdapter(getSupportFragmentManager());
                                vpPager.setAdapter(adapterViewPager);

                                checkDayIsAbsent();
                            }
                            else
                            {
                                toolbarTextView.setText("KPI Schedule");

                                SmartFragmentStatePagerAdapter adapterViewPager = new ScheduleAdapter(getSupportFragmentManager());
                                vpPager.setAdapter(adapterViewPager);

                                checkDayIsAbsent();
                            }

                            coverFrameLayout.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).withStartAction(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (toolbarTextView.getText().toString().equals("My Schedule"))
                                    {
                                        addPair.setVisibility(View.VISIBLE);
                                        addPair.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
                                    }
                                    else
                                    {
                                        addPair.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                addPair.setVisibility(View.INVISIBLE);
                                            }
                                        }).start();
                                    }
                                }
                            }).start();
                            toolbarTextView.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
                        }
                    }).start();
                }
            }
        });
    }

    public FrameLayout getMainFrameLayout()
    {
        return coverFrameLayout;
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        service.api.getManageSchedule()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<OwnSchedule>>()
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
                           public void onNext(BaseResponse<OwnSchedule> ownScheduleBaseResponse)
                           {
                               AppSettings.ownSchedule = ownScheduleBaseResponse.getData().getData();

                               coverFrameLayout.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).withStartAction(new Runnable()
                               {
                                   @Override
                                   public void run()
                                   {
                                       SmartFragmentStatePagerAdapter adapterViewPager = new OwnScheduleAdapter(getSupportFragmentManager());
                                       vpPager.setAdapter(adapterViewPager);

                                       checkDayIsAbsent();
                                   }
                               }).start();
                           }
                       });
    }

    public void recreateOwnSchedule()
    {
        coverFrameLayout.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).withStartAction(new Runnable()
        {
            @Override
            public void run()
            {
                SmartFragmentStatePagerAdapter adapterViewPager = new OwnScheduleAdapter(getSupportFragmentManager());
                vpPager.setAdapter(adapterViewPager);
            }
        }).start();
    }

    public void checkDayIsAbsent()
    {
        if (toolbarTextView.getText().toString().equals("KPI Schedule"))
        {
            if (AppSettings.schedule.weeks.get(AppSettings.showWeek - 1).days.size() == 0)
            {
                coverProgressBar.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).start();
                absentPairsTextView.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
            }
            else
            {
                coverProgressBar.animate().alpha(1).setDuration(0).setInterpolator(new DecelerateInterpolator()).start();
                absentPairsTextView.animate().alpha(0).setDuration(0).setInterpolator(new AccelerateInterpolator()).start();
            }
        }
        else
        {
            if (AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.size() == 0)
            {
                coverProgressBar.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).start();
                absentPairsTextView.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
            }
            else
            {
                coverProgressBar.animate().alpha(1).setDuration(0).setInterpolator(new DecelerateInterpolator()).start();
                absentPairsTextView.animate().alpha(0).setDuration(0).setInterpolator(new AccelerateInterpolator()).start();
            }
        }
    }

    public static class ScheduleAdapter extends SmartFragmentStatePagerAdapter
    {
        ScheduleAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

        @Override
        public int getCount()
        {
            return AppSettings.schedule.weeks.get(AppSettings.showWeek - 1).days.size();
        }

        @Override
        public Fragment getItem(final int position)
        {
            return ScheduleFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return AppSettings.schedule.weeks.get(AppSettings.showWeek - 1).days.get(position).dayName;
        }
    }

    public static class OwnScheduleAdapter extends SmartFragmentStatePagerAdapter
    {
        OwnScheduleAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

        @Override
        public int getCount()
        {
            return AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.size();
        }

        @Override
        public Fragment getItem(final int position)
        {
            return OwnScheduleFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).dayName;
        }
    }
}