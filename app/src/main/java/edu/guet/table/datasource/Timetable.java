package edu.guet.table.datasource;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Mr.小世界 on 2018/10/13.
 */

public interface Timetable
{
    final class Course
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

    final class Selected
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

    final class Answer
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

    final class Experimental
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

    Answer[] getAnswers();

    Course[] getCourses();

    Selected[] getSelects();

    Experimental[] getExperimentals();

    String getSemester();

    String getUsername();
}
