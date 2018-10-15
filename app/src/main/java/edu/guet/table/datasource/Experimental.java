package edu.guet.table.datasource;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Mr.小世界 on 2018/10/15.
 */
public final class Experimental extends LitePalSupport
{
    @JSONField(name = "星期")
    public final int dayOfWeek;

    @JSONField(name = "课程名")
    public final String course;

    @JSONField(name = "周")
    public final int week;

    @JSONField(name = "上课地点")
    public final String classroom;

    @JSONField(name = "批次")
    public final int batch;

    @JSONField(name = "实验名称")
    public final String name;

    @JSONField(name = "开课专业")
    public final String major;

    @JSONField(name = "节数")
    public final int step;

    @JSONField(name = "开始")
    public final int start;

    Experimental(int dayOfWeek,
                 String course,
                 int week,
                 String classroom,
                 int batch,
                 String name,
                 String major, int step, int start)
    {
        this.dayOfWeek = dayOfWeek;
        this.course = course;
        this.week = week;
        this.classroom = classroom;
        this.batch = batch;
        this.name = name;
        this.major = major;
        this.step = step;
        this.start = start;
    }

    @Override
    public String toString()
    {
        return JSONObject.toJSONString(this);
    }
}
