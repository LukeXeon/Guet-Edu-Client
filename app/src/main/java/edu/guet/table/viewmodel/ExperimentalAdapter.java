package edu.guet.table.viewmodel;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;

import java.util.ArrayList;

import edu.guet.table.datasource.Experimental;

/**
 * Created by Mr.小世界 on 2018/10/12.
 */

public final class ExperimentalAdapter implements ScheduleEnable
{
    private final Experimental experimental;

    public ExperimentalAdapter(Experimental experimental)
    {
        this.experimental = experimental;
    }


    @Override
    public Schedule getSchedule()
    {
        Schedule schedule = new Schedule();
        schedule.setStart(experimental.getStart());
        schedule.setStep(experimental.getStep());
        schedule.setWeekList(new ArrayList<Integer>()
        {{
            add(experimental.getWeek());
        }});
        schedule.setDay(experimental.getDayOfWeek());
        schedule.setName("[实验]" + experimental.getCourse());
        schedule.setRoom(experimental.getClassroom());
        return schedule;
    }
}
