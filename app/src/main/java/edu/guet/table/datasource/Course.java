package edu.guet.table.datasource;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Mr.小世界 on 2018/10/15.
 */
public final class Course extends LitePalSupport
{
    @JSONField(name = "星期")
    public final int dayOfWeek;

    @JSONField(name = "节数")
    public final int step;

    @JSONField(name = "开始")
    public final int start;

    @JSONField(name = "课程名")
    public final String name;

    @JSONField(name = "开始周")
    public final int beginWeek;

    @JSONField(name = "结束周")
    public final int endWeek;

    @JSONField(name = "课号")
    public final String number;

    @JSONField(name = "上课地点")
    public final String classroom;

    @JSONField(name = "教师")
    public final String teacher;

    @JSONField(name = "课程代码")
    public final String code;

    Course(int dayOfWeek, int step, int start, String name,
           int beginWeek,
           int endWeek,
           String number,
           String classroom,
           String teacher,
           String code)
    {
        this.dayOfWeek = dayOfWeek;
        this.step = step;
        this.start = start;
        this.name = name;
        this.beginWeek = beginWeek;
        this.endWeek = endWeek;
        this.number = number;
        this.classroom = classroom;
        this.teacher = teacher;
        this.code = code;
    }

    @Override
    public String toString()
    {
        return JSONObject.toJSONString(this);
    }
}
