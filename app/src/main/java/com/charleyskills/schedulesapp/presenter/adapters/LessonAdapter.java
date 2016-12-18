package com.charleyskills.schedulesapp.presenter.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.charleyskills.schedulesapp.R;
import com.charleyskills.schedulesapp.models.Lesson;

import java.util.List;

public class LessonAdapter extends ArrayAdapter<Lesson>
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
    private List<Lesson> lessons;

    public LessonAdapter(Context ctx, List<Lesson> lessons)
    {
        super(ctx, R.layout.fragment_day, lessons);
        this.context = ctx;
        this.lessons = lessons;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        Lesson lesson = lessons.get(position);
        ViewHolder viewHolder;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lesson, parent, false);

            viewHolder.number = (TextView) convertView.findViewById(R.id.number);
            viewHolder.lessonName = (TextView) convertView.findViewById(R.id.lessonName);
            viewHolder.teacher = (TextView) convertView.findViewById(R.id.teacher);
            viewHolder.room = (TextView) convertView.findViewById(R.id.room);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            viewHolder.startTime = (TextView) convertView.findViewById(R.id.startTime);
            viewHolder.endTime = (TextView) convertView.findViewById(R.id.endTime);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.number.setText(String.valueOf(lesson.number));
        viewHolder.lessonName.setText(lesson.name);
        viewHolder.teacher.setText(lesson.teacher);
        viewHolder.room.setText(String.format("%s - %s", lesson.classRoom.Room, lesson.classRoom.Building));
        viewHolder.type.setText(lesson.type);
        viewHolder.startTime.setText(lesson.start.substring(0, 5));
        viewHolder.endTime.setText(lesson.end.substring(0, 5));

        return convertView;
    }
}