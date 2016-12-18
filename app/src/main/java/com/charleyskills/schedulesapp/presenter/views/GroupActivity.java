package com.charleyskills.schedulesapp.presenter.views;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.charleyskills.schedulesapp.AppSettings;
import com.charleyskills.schedulesapp.R;
import com.charleyskills.schedulesapp.presenter.adapters.SmartFragmentStatePagerAdapter;
import com.charleyskills.schedulesapp.presenter.views.fragments.DayFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GroupActivity extends AppCompatActivity implements DayFragment.OnFragmentInteractionListener
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

//    GroupPresenter mGroupPresenter;
//    Service mService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ButterKnife.inject(this);

        coverProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        if(AppSettings.currentWeek == 1)
        {
            firstWeekTextView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        else
        {
            secondWeekTextView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

        SmartFragmentStatePagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        firstWeekTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                firstWeekTextView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                secondWeekTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                AppSettings.showedWeek = 1;

                coverFrameLayout.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                    @Override
                    public void run()
                    {
                        SmartFragmentStatePagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
                        vpPager.setAdapter(adapterViewPager);
                        coverFrameLayout.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
                    }
                }).start();
            }
        });

        secondWeekTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                secondWeekTextView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                firstWeekTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                AppSettings.showedWeek = 2;

                coverFrameLayout.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                    @Override
                    public void run()
                    {
                        SmartFragmentStatePagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
                        vpPager.setAdapter(adapterViewPager);
                        coverFrameLayout.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
                    }
                }).start();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter
    {
        MyPagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

        @Override
        public int getCount()
        {
            return AppSettings.currentSchedule.Weeks.get(AppSettings.showedWeek - 1).days.size();
        }

        @Override
        public Fragment getItem(int position)
        {
            return DayFragment.newInstance(position - 1, AppSettings.currentSchedule.Weeks.get(AppSettings.showedWeek - 1).days.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return AppSettings.currentSchedule.Weeks.get(AppSettings.showedWeek - 1).days.get(position).dayName;
        }
    }
}