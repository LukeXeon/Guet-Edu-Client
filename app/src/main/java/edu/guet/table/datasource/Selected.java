package edu.guet.table.datasource;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Mr.小世界 on 2018/10/15.
 */
public final class Selected extends LitePalSupport
{
    @JSONField(name = "课程名")
    public final String name;
    @JSONField(name = "教师")
    public final String teacher;
    @JSONField(name = "类型")
    public final String type;
    @JSONField(name = "课程代码")
    public final String code;
    @JSONField(name = "课号")
    public final String number;

    Selected(String name, String teacher, String type, String code, String number)
    {
        this.name = name;
        this.teacher = teacher;
        this.type = type;
        this.code = code;
        this.number = number;
    }

    @Override
    public String toString()
    {
        return JSONObject.toJSONString(this);
    }
}
