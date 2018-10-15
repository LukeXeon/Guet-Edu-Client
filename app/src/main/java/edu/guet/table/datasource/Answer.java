package edu.guet.table.datasource;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by Mr.小世界 on 2018/10/15.
 */
public final class Answer extends LitePalSupport
{
    @JSONField(name = "课程代码")
    String code;
    @JSONField(name = "课号")
    String number;
    @JSONField(name = "课程名")
    String name;
    @JSONField(name = "辅导教师")
    String teacher;
    @JSONField(name = "答疑地点")
    String classroom;
    @JSONField(name = "开始周")
    int beginWeek;
    @JSONField(name = "结束周")
    int endWeek;
    @JSONField(name = "星期")
    int dayOfWeek;
    @JSONField(name = "时间")
    String time;

    Answer()
    {
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

    public String getClassroom()
    {
        return classroom;
    }

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

    public String getNumber()
    {
        return number;
    }

    public String getTeacher()
    {
        return teacher;
    }

    public String getTime()
    {
        return time;
    }

    @Override
    public String toString()
    {
        return JSONObject.toJSONString(this);
    }
}
