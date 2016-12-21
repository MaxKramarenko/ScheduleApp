package com.charleyskills.scheduleapp.presenter.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
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

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, 0);

        final Service service = new Service();

        ButterKnife.inject(this);

        weekSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new String[]{ "1", "2" }));
        numberSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new String[]{ "1", "2", "3", "4", "5", "6", "7", "8" }));
        daySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new String[]{"Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота", "Неділя"}));

        intent = getIntent();
        editMode = intent.getBooleanExtra("Edit", false);
        if(editMode)
        {
            nameEditText.setText(intent.getStringExtra("Name"));
            numberSpinner.setSelection(intent.getIntExtra("Number", 0) - 1);
            teacherEditText.setText(intent.getStringExtra("Teacher"));
            weekSpinner.setSelection(intent.getIntExtra("Week", 0) - 1);
            daySpinner.setSelection(intent.getIntExtra("Day", 0));
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
                    .subscribeOn(Schedulers.newThread())
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
