package com.charleyskills.scheduleapp.presenter.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.charleyskills.scheduleapp.AppSettings;
import com.charleyskills.scheduleapp.R;
import com.charleyskills.scheduleapp.presenter.adapters.LessonAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment
{
    private int position;
    LessonAdapter mLessonAdapter;

    ListView mLessonsView;

    private OnFragmentInteractionListener mListener;

    public ScheduleFragment()
    {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance(int day)
    {
        ScheduleFragment fragment = new ScheduleFragment();
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
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fragment_schedule, container, false);
        mLessonsView = (ListView) view.findViewById(R.id.lessonsListView);

        mLessonAdapter = new LessonAdapter(view.getContext(), AppSettings.schedule.weeks.get(AppSettings.showWeek - 1).days.get(position).lessons);
        mLessonsView.setAdapter(mLessonAdapter);
        mLessonAdapter.notifyDataSetInvalidated();

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