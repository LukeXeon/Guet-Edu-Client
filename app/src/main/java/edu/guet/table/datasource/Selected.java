package edu.guet.table.datasource;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by Mr.小世界 on 2018/10/15.
 */
public final class Selected extends LitePalSupport
{
    @JSONField(name = "课程名")
    String name;
    @JSONField(name = "教师")
    String teacher;
    @JSONField(name = "类型")
    String type;
    @JSONField(name = "课程代码")
    String code;
    @JSONField(name = "课号")
    String number;

    Selected() {}

    Selected(String name, String teacher, String type, String code, String number)
    {
        this.name = name;
        this.teacher = teacher;
        this.type = type;
        this.code = code;
        this.number = number;
    }

    public String getName()
    {
        return name;
    }

    public String getTeacher()
    {
        return teacher;
    }

    public String getCode()
    {
        return code;
    }

    public String getNumber()
    {
        return number;
    }

    public String getType()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return JSONObject.toJSONString(this);
    }
}
