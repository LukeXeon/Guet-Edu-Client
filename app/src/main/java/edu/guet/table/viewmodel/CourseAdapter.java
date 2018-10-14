package edu.guet.table.viewmodel;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;

import java.util.ArrayList;

import edu.guet.table.datasource.TimetableLoader;

/**
 * Created by Mr.小世界 on 2018/10/10.
 */

public final class CourseAdapter implements ScheduleEnable
{
    private final TimetableLoader.Course course;

    public CourseAdapter(TimetableLoader.Course course)
    {
        this.course = course;
    }

    @Override
    public Schedule getSchedule()
    {
        Schedule schedule = new Schedule();
        schedule.setName(course.name);
        schedule.setRoom(course.classroom);
        schedule.setTeacher(course.teacher);
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = course.beginWeek; i <= course.endWeek; i++)
        {
            list.add(i);
        }
        schedule.setWeekList(list);
        schedule.setDay(course.dayOfWeek);
        schedule.setStart(course.start);
        schedule.setStep(course.step);
        return schedule;
    }
}
