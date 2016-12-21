package com.charleyskills.scheduleapp.presenter.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.charleyskills.scheduleapp.AppSettings;
import com.charleyskills.scheduleapp.R;
import com.charleyskills.scheduleapp.models.BaseResponse;
import com.charleyskills.scheduleapp.presenter.adapters.PairAdapter;
import com.charleyskills.scheduleapp.presenter.views.AddPairActivity;
import com.charleyskills.scheduleapp.presenter.views.TimelineActivity;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OwnScheduleFragment extends Fragment
{
    private int position;
    PairAdapter mPairAdapter;
    ListView mPairsView;
    private OnFragmentInteractionListener mListener;

    public OwnScheduleFragment()
    {
        // Required empty public constructor
    }

    public static OwnScheduleFragment newInstance(int day)
    {
        OwnScheduleFragment fragment = new OwnScheduleFragment();
        Bundle args = new Bundle();
        args.putInt("day", day);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            Bundle args = getArguments();
            position = args.getInt("day");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fragment_ownschedule, container, false);
        mPairsView = (ListView) view.findViewById(R.id.pairsListView);

        mPairAdapter = new PairAdapter(view.getContext(), AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs);
        mPairsView.setAdapter(mPairAdapter);
        mPairsView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l)
            {
                CharSequence[] ch = new CharSequence[] { "Edit", "Delete" };
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setItems(ch, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k)
                    {
                        switch (k)
                        {
                            case 0:
                                Intent intent = new Intent(getContext(), AddPairActivity.class);
                                intent.putExtra("Edit", true);
                                intent.putExtra("ID", AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs.get(i).id);
                                intent.putExtra("Name", AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs.get(i).name);
                                intent.putExtra("Number", AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs.get(i).number);
                                intent.putExtra("Teacher", AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs.get(i).teacher);
                                intent.putExtra("Week", AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs.get(i).week);
                                intent.putExtra("Day", AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs.get(i).day);
                                intent.putExtra("Room", AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs.get(i).room);
                                intent.putExtra("StartTime", AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs.get(i).startTime);
                                intent.putExtra("EndTime", AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs.get(i).endTime);
                                startActivityForResult(intent, 0);
                                break;
                            case 1:
                                ((TimelineActivity) getActivity()).getMainFrameLayout().animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        ((TimelineActivity) getActivity()).service.api.deleteManageSchedule(AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs.get(i).id)
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Subscriber<BaseResponse<Boolean>>()
                                                {
                                                    @Override
                                                    public void onCompleted()
                                                    {

                                                    }

                                                    @Override
                                                    public void onError(Throwable e)
                                                    {
                                                        Log.e("RxDelete pair", e.getMessage());
                                                    }

                                                    @Override
                                                    public void onNext(BaseResponse<Boolean> booleanBaseResponse)
                                                    {
                                                        if(booleanBaseResponse.getData().getData())
                                                        {
                                                            AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs.remove(i);
                                                            if (AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.get(position).pairs.size() == 0)
                                                            {
                                                                AppSettings.ownSchedule.weeks.get(AppSettings.showWeek - 1).days.remove(position);
                                                            }
                                                            ((TimelineActivity) getActivity()).checkDayIsAbsent();
                                                            ((TimelineActivity) getActivity()).recreateOwnSchedule();
                                                        }
                                                    }
                                                });
                                    }
                                }).start();

                                break;
                        }
                    }
                });
                adb.create();
                adb.show();
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}