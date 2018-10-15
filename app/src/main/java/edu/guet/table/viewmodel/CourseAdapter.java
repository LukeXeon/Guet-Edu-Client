package edu.guet.table.viewmodel;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;

import java.util.ArrayList;

import edu.guet.table.datasource.Course;

/**
 * Created by Mr.小世界 on 2018/10/10.
 */

public final class CourseAdapter implements ScheduleEnable
{
    private final Course course;

    public CourseAdapter(Course course)
    {
        this.course = course;
    }

    @Override
    public Schedule getSchedule()
    {
        Schedule schedule = new Schedule();
        schedule.setName(course.getName());
        schedule.setRoom(course.getClassroom());
        schedule.setTeacher(course.getTeacher());
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = course.getBeginWeek(); i <= course.getEndWeek(); i++)
        {
            list.add(i);
        }
        schedule.setWeekList(list);
        schedule.setDay(course.getDayOfWeek());
        schedule.setStart(course.getStart());
        schedule.setStep(course.getStep());
        return schedule;
    }
}
