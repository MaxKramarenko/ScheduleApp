package com.charleyskills.scheduleapp.presenter.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.charleyskills.scheduleapp.API.Service;
import com.charleyskills.scheduleapp.R;
import com.charleyskills.scheduleapp.models.BaseResponse;
import com.charleyskills.scheduleapp.models.ownSchedule.Pair;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddPairActivity extends AppCompatActivity
{
    @InjectView(R.id.nameEditText)
    EditText nameEditText;

    @InjectView(R.id.numberSpinner)
    Spinner numberSpinner;

    @InjectView(R.id.teacherEditText)
    EditText teacherEditText;

    @InjectView(R.id.pairTypeSpinner)
    Spinner pairTypeSpinner;

    @InjectView(R.id.weekSpinner)
    Spinner weekSpinner;

    @InjectView(R.id.daySpinner)
    Spinner daySpinner;

    @InjectView(R.id.roomEditText)
    EditText roomEditText;

    @InjectView(R.id.startTimeEditText)
    EditText startTimeEditText;

    @InjectView(R.id.endTimeEditText)
    EditText endTimeEditText;

    @InjectView(R.id.accept)
    Button accept;

    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    Pair pair = new Pair();
    Boolean editMode;
    Intent intent;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pair);

        overridePendingTransition(R.anim.custom_fade_in, R.anim.custom_fade_out);

        final Service service = new Service();

        ButterKnife.inject(this);

        weekSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new String[]{ "1", "2" }));
        numberSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new String[]{ "1", "2", "3", "4", "5", "6", "7", "8" }));
        daySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new String[]{"Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота", "Неділя"}));
        pairTypeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new String[]{"Лаб", "Лек"}));

        intent = getIntent();
        editMode = intent.getBooleanExtra("Edit", false);
        if(editMode)
        {
            nameEditText.setText(intent.getStringExtra("Name"));
            numberSpinner.setSelection(intent.getIntExtra("Number", 0) - 1);
            teacherEditText.setText(intent.getStringExtra("Teacher"));
            pairTypeSpinner.setSelection(intent.getIntExtra("PairType", 0));
            weekSpinner.setSelection(intent.getIntExtra("Week", 0) - 1);
            daySpinner.setSelection(intent.getIntExtra("Day", 0) - 1);
            roomEditText.setText(intent.getStringExtra("Room"));
            startTimeEditText.setText(intent.getStringExtra("StartTime"));
            endTimeEditText.setText(intent.getStringExtra("EndTime"));
        }

        accept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(editMode)
                    pair.id = intent.getStringExtra("ID");
                else
                    pair.id = "";

                pair.name = nameEditText.getText().toString();
                pair.teacher = teacherEditText.getText().toString();
                pair.room = roomEditText.getText().toString();
                pair.startTime = startTimeEditText.getText().toString();
                pair.endTime = endTimeEditText.getText().toString();

                progressBar.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
                accept.setEnabled(false);

                service.api.updateManageSchedule(pair)
                        .subscribeOn(Schedulers.computation())
                        .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BaseResponse<String>>()
                    {
                        @Override
                        public void onCompleted()
                        {

                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            Log.e("RxAddPair", e.getMessage());
                            progressBar.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).start();
                            accept.setEnabled(true);
                        }

                        @Override
                        public void onNext(BaseResponse<String> stringBaseResponse)
                        {
                            finish();
                        }
                    });
            }
        });

        pairTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                pair.type = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                adapterView.getItemAtPosition(0);
            }
        });

        weekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                pair.week = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                adapterView.getItemAtPosition(0);
            }
        });

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                pair.day = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                adapterView.getItemAtPosition(0);
            }
        });

        numberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                pair.number = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                adapterView.getItemAtPosition(0);
            }
        });
    }
}
