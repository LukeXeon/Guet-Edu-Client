package edu.guet.table.viewmodel;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;

import java.util.ArrayList;

import edu.guet.table.datasource.Timetable;

/**
 * Created by Mr.小世界 on 2018/10/12.
 */

public final class ExperimentalAdapter implements ScheduleEnable
{
    private final Timetable.Experimental experimental;

    public ExperimentalAdapter(Timetable.Experimental experimental)
    {
        this.experimental = experimental;
    }


    @Override
    public Schedule getSchedule()
    {
        Schedule schedule = new Schedule();
        schedule.setStart(experimental.start);
        schedule.setStep(experimental.step);
        schedule.setWeekList(new ArrayList<Integer>()
        {{
            add(experimental.week);
        }});
        schedule.setDay(experimental.dayOfWeek);
        schedule.setName("[实验]" + experimental.course);
        schedule.setRoom(experimental.classroom);
        return schedule;
    }
}
