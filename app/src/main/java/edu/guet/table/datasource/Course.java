package edu.guet.table.datasource;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by Mr.小世界 on 2018/10/15.
 */
public final class Course extends LitePalSupport
{
    @JSONField(name = "星期")
    int dayOfWeek;
    @JSONField(name = "节数")
    int step;
    @JSONField(name = "开始")
    int start;
    @JSONField(name = "课程名")
    String name;
    @JSONField(name = "开始周")
    int beginWeek;
    @JSONField(name = "结束周")
    int endWeek;
    @JSONField(name = "课号")
    String number;
    @JSONField(name = "上课地点")
    String classroom;
    @JSONField(name = "教师")
    String teacher;
    @JSONField(name = "课程代码")
    String code;

    Course() {}


    public String getTeacher()
    {
        return teacher;
    }

    public String getNumber()
    {
        return number;
    }

    public String getName()
    {
        return name;
    }

    public String getCode()
    {
        return code;
    }

    public int getBeginWeek()
    {
        return beginWeek;
    }

    public int getDayOfWeek()
    {
        return dayOfWeek;
    }

    public int getEndWeek()
    {
        return endWeek;
    }

    public int getStart()
    {
        return start;
    }

    public int getStep()
    {
        return step;
    }

    public String getClassroom()
    {
        return classroom;
    }

    @Override
    public String toString()
    {
        return JSONObject.toJSONString(this);
    }
}
