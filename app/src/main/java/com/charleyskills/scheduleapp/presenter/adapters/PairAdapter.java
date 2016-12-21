package com.charleyskills.scheduleapp.presenter.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.charleyskills.scheduleapp.R;
import com.charleyskills.scheduleapp.models.ownSchedule.Pair;

import java.util.List;

public class PairAdapter extends ArrayAdapter<Pair>
{
    private static class ViewHolder
    {
        TextView number;
        TextView lessonName;
        TextView teacher;
        TextView room;
        TextView type;
        TextView startTime;
        TextView endTime;
    }

    private Context context;
    private List<Pair> pairs;

    public PairAdapter(Context ctx, List<Pair> pairs)
    {
        super(ctx, R.layout.fragment_ownschedule, pairs);
        this.context = ctx;
        this.pairs = pairs;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        Pair pair = pairs.get(position);
        ViewHolder viewHolder;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pair, parent, false);

            viewHolder.number = (TextView) convertView.findViewById(R.id.pairNumber);
            viewHolder.lessonName = (TextView) convertView.findViewById(R.id.pairName);
            viewHolder.teacher = (TextView) convertView.findViewById(R.id.pairTeacher);
            viewHolder.room = (TextView) convertView.findViewById(R.id.pairRoom);
            viewHolder.type = (TextView) convertView.findViewById(R.id.pairType);
            viewHolder.startTime = (TextView) convertView.findViewById(R.id.pairStartTime);
            viewHolder.endTime = (TextView) convertView.findViewById(R.id.pairEndTime);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.number.setText(String.valueOf(pair.number));
        viewHolder.lessonName.setText(pair.name);
        viewHolder.teacher.setText(pair.teacher);
        viewHolder.room.setText(pair.room);
        if(pair.type == 0)
            viewHolder.type.setText("лаб");
        else
            viewHolder.type.setText("лек");

        viewHolder.startTime.setText(pair.startTime);
        viewHolder.endTime.setText(pair.endTime);

        return convertView;
    }
}