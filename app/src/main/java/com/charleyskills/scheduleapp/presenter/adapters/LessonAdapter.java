package com.charleyskills.scheduleapp.presenter.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.charleyskills.scheduleapp.R;
import com.charleyskills.scheduleapp.models.Schedule.Lesson;

import java.util.List;

public class LessonAdapter extends ArrayAdapter<Lesson>
{
    private static class ViewHolder
    {
        TextView number;
        TextView lessonName;
        TextView teacher;
        TextView room;
        TextView building;
        TextView type;
        TextView startTime;
        TextView endTime;
    }

    private Context context;
    private List<Lesson> lessons;

    public LessonAdapter(Context ctx, List<Lesson> lessons)
    {
        super(ctx, R.layout.fragment_schedule, lessons);
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

            viewHolder.number = (TextView) convertView.findViewById(R.id.lessonNumber);
            viewHolder.lessonName = (TextView) convertView.findViewById(R.id.lessonName);
            viewHolder.teacher = (TextView) convertView.findViewById(R.id.lessonTeacher);
            viewHolder.room = (TextView) convertView.findViewById(R.id.lessonRoom);
            viewHolder.building = (TextView) convertView.findViewById(R.id.lessonBuilding);
            viewHolder.type = (TextView) convertView.findViewById(R.id.lessonType);
            viewHolder.startTime = (TextView) convertView.findViewById(R.id.lessonStartTime);
            viewHolder.endTime = (TextView) convertView.findViewById(R.id.lessonEndTime);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.number.setText(String.valueOf(lesson.number));
        viewHolder.lessonName.setText(lesson.name);
        viewHolder.teacher.setText(lesson.teacher);

        if(lesson.classRoom.Room != null)
        {
            viewHolder.room.setText(lesson.classRoom.Room);
        }
        else
        {
            viewHolder.room.setText("//");
        }

        if(lesson.classRoom.Building != null)
        {
            viewHolder.building.setText(lesson.classRoom.Building);
        }
        else
        {
            viewHolder.building.setText("//");
        }

        viewHolder.type.setText(lesson.type);
        viewHolder.startTime.setText(lesson.start.substring(0, 5));
        viewHolder.endTime.setText(lesson.end.substring(0, 5));

        return convertView;
    }
}