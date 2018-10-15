package edu.guet.table.datasource;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by Mr.小世界 on 2018/10/15.
 */
public final class Experimental extends LitePalSupport
{
    @JSONField(name = "星期")
    int dayOfWeek;
    @JSONField(name = "课程名")
    String course;
    @JSONField(name = "周")
    int week;
    @JSONField(name = "上课地点")
    String classroom;
    @JSONField(name = "批次")
    int batch;
    @JSONField(name = "实验名称")
    String name;
    @JSONField(name = "开课专业")
    String major;
    @JSONField(name = "节数")
    int step;
    @JSONField(name = "开始")
    int start;

    Experimental()
    {
    }

    public String getClassroom()
    {
        return classroom;
    }

    public int getStep()
    {
        return step;
    }

    public int getStart()
    {
        return start;
    }

    public int getBatch()
    {
        return batch;
    }

    public int getDayOfWeek()
    {
        return dayOfWeek;
    }

    public int getWeek()
    {
        return week;
    }

    public String getCourse()
    {
        return course;
    }

    public String getMajor()
    {
        return major;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return JSONObject.toJSONString(this);
    }
}
