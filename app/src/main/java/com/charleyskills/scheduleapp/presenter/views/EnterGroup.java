package com.charleyskills.scheduleapp.presenter.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.charleyskills.scheduleapp.API.Service;
import com.charleyskills.scheduleapp.R;
import com.charleyskills.scheduleapp.presenter.presenters.EnterGroupPresenter;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

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

    public Context context;
    private List<String> allGroupsPair;

    private EnterGroupPresenter enterGroupPresenter;
    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_group);

        overridePendingTransition(R.anim.custom_fade_in, R.anim.custom_fade_out);

        ButterKnife.inject(this);

        context = this;

        loadAllGroupsProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        enterGroupPresenter = new EnterGroupPresenter(this, service = new Service());
        enterGroupPresenter.getAllGroup();
        enterGroupPresenter.setTextChangeEvents(searchGroupEditText, nextButton);
        enterGroupPresenter.setNextButtonOnClickListener(nextButton, searchGroupEditText, this);
    }

    public void loadAllGroups(List<String> strings)
    {
        searchGroupLayout.setVisibility(View.VISIBLE);
        searchGroupLayout.animate().alpha(1).setDuration(220).setInterpolator(new AccelerateInterpolator()).start();
        loadAllGroupsProgressBar.animate().alpha(0).setDuration(220).setInterpolator(new DecelerateInterpolator()).start();

        allGroupsPair = strings;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, 0);
    }

    public void setEnableNextButton(boolean b)
    {
        nextButton.setEnabled(b);
    }

    public void setEditTextProgressBarVisibility(int visibility)
    {
        editTextProgressBar.setVisibility(visibility);
    }

    public boolean isSame(String a, String b)
    {
        Collator insenstiveStringComparator = Collator.getInstance();
        insenstiveStringComparator.setStrength(Collator.PRIMARY);

        return insenstiveStringComparator.compare(a, b) == 0;
    }

    public void setArrayAdapter(String s)
    {
        List<String> temp = new ArrayList<String>();
        for(String aas : allGroupsPair)
        {
            if(isSame(aas.substring(0, s.length()), s.toLowerCase()))
                temp.add(aas);
        }

        searchGroupEditText.setAdapter(new ArrayAdapter<String>(EnterGroup.this, android.R.layout.simple_dropdown_item_1line, temp));
        searchGroupEditText.showDropDown();
    }

    public void startGroupActivity()
    {
        Intent intent = new Intent(getApplicationContext(), TimelineActivity.class);
        startActivity(intent);
        finish();
    }
}
