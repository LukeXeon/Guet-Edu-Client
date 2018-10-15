package edu.guet.table.datasource;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Mr.小世界 on 2018/10/15.
 */
public final class Answer extends LitePalSupport
{
    @JSONField(name = "课程代码")
    public final String code;
    @JSONField(name = "课号")
    public final String number;
    @JSONField(name = "课程名")
    public final String name;
    @JSONField(name = "辅导教师")
    public final String teacher;
    @JSONField(name = "答疑地点")
    public final String classroom;
    @JSONField(name = "开始周")
    public final int beginWeek;
    @JSONField(name = "结束周")
    public final int endWeek;
    @JSONField(name = "星期")
    public final int dayOfWeek;
    @JSONField(name = "时间")
    public final String time;

    Answer(String code,
           String number,
           String name,
           String teacher,
           String classroom,
           int beginWeek,
           int endWeek,
           int dayOfWeek,
           String time)
    {
        this.code = code;
        this.number = number;
        this.name = name;
        this.teacher = teacher;
        this.classroom = classroom;
        this.beginWeek = beginWeek;
        this.endWeek = endWeek;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
    }

    @Override
    public String toString()
    {
        return JSONObject.toJSONString(this);
    }
}
