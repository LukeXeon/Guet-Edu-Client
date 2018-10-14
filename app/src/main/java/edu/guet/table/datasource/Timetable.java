package edu.guet.table.datasource;

import android.os.Looper;
import android.support.annotation.UiThread;
import android.text.TextUtils;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.guet.table.support.CookieCache;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;
import javalab.util.Callback;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.schedulers.Schedulers;
import javalab.util.Callbacks;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import javalab.util.Tuple2;
import javalab.util.Tuples;

/**
 * Created by Mr.小世界 on 2018/10/8.
 */


public final class Timetable
{
    public static final class Builder
    {
        private String username;
        private String password;
        private String semester;

        private String login;
        private String table;
        private String selected;
        private int timeout;
        private Observable<TimetableCache> cache;

        public Builder account(String username, String password)
        {
            this.username = username;
            this.password = password;
            return this;
        }

        public Builder semester(String semester)
        {
            this.semester = semester;
            return this;
        }

        @UiThread
        public void async(final Callback<Timetable> callback)
        {
            observable().subscribe(new Observer<Timetable>()
            {
                @Override
                public void onSubscribe(Disposable d)
                {
                }

                @Override
                public void onNext(Timetable value)
                {
                    Callbacks.safeCallback(callback, value);
                }

                @Override
                public void onError(Throwable e)
                {
                    e.printStackTrace();
                    Callbacks.safeCallback(callback, null);
                }

                @Override
                public void onComplete()
                {
                }
            });
        }

        public Observable<Timetable> observable()
        {
            check();
            return build(this);
        }

        public Builder login(String login)
        {
            this.login = login;
            return this;
        }

        public Builder table(String table)
        {
            this.table = table;
            return this;
        }

        public Builder selected(String selected)
        {
            this.selected = selected;
            return this;
        }

        public Builder timeout(int timeout)
        {
            this.timeout = timeout;
            return this;
        }

        private void check()
        {
            Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{4}_(1|2)");
            if (TextUtils.isEmpty(this.username)
                    || TextUtils.isEmpty(this.password))
            {
                throw new IllegalArgumentException("username or password is empty !");
            }
            if (TextUtils.isEmpty(this.semester)
                    || !pattern.matcher(this.semester).matches())
            {
                throw new IllegalArgumentException(
                        "semester is empty or illegal format ! (regex = "
                                + pattern + ")");
            }
            if (!Looper.getMainLooper().equals(Looper.myLooper()))
            {
                throw new RuntimeException("must call in main thread !");
            }
            this.timeout = this.timeout <= 0 ? 10 : this.timeout;
            this.login = this.login == null
                    ? "http://bkjw2.guet.edu.cn/student/public/login.asp"
                    : this.login;
            this.table = this.table == null
                    ? "http://bkjw2.guet.edu.cn/student/coursetable.asp"
                    : this.table;
            this.selected = this.selected == null
                    ? "http://bkjw2.guet.edu.cn/student/Selected.asp"
                    : this.selected;
        }

        private static String post(OkHttpClient httpClient,
                                   String url, String semester) throws IOException
        {
            FormBody formBody = new FormBody.Builder()
                    .add("term", semester)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody).build();
            Response response = httpClient.newCall(request)
                    .execute();
            if (response.code() != 200)
            {
                response.close();
                throw new IOException();
            }
            BufferedReader responseReader
                    = new BufferedReader(new InputStreamReader(response.body()
                    .byteStream(), "GBK"));
            StringBuilder stringBuilder = new StringBuilder();
            String temp = responseReader.readLine();
            while (temp != null)
            {
                stringBuilder.append(temp);
                temp = responseReader.readLine();
            }
            response.close();
            return stringBuilder.toString();
        }

        private static Observable<Timetable> build(Builder source)
        {
            Observable<Builder> builder = Observable
                    .just(source)
                    .subscribeOn(Schedulers.io());
            Observable<OkHttpClient> loginClient = builder
                    .map(new Function<Builder, OkHttpClient>()
                    {
                        @Override
                        public OkHttpClient apply(Builder builder) throws Exception
                        {
                            try
                            {
                                OkHttpClient httpClient
                                        = new OkHttpClient.Builder()
                                        .connectTimeout(builder.timeout, TimeUnit.SECONDS)
                                        .cookieJar(new CookieCache())
                                        .build();
                                FormBody formBody = new FormBody.Builder()
                                        .add("username", builder.username)
                                        .add("passwd", builder.password)
                                        .add("login", "(unable to decode value)")
                                        .build();
                                Request request = new Request.Builder()
                                        .url(builder.login)
                                        .post(formBody)
                                        .build();
                                Response response = httpClient.newCall(request)
                                        .execute();
                                if (response.code() != 200)
                                {
                                    response.close();
                                    throw new IOException();
                                } else
                                {
                                    response.close();
                                    return httpClient;
                                }
                            } catch (IOException exception)
                            {
                                throw Exceptions.propagate(exception);
                            }
                        }
                    });

            Observable<ArrayList<Selected>> selected
                    = loginClient.zipWith(builder, new BiFunction<OkHttpClient, Builder, String>()
            {
                @Override
                public String apply(OkHttpClient client,
                                    Builder builder) throws Exception
                {
                    try
                    {
                        return post(client, builder.selected, builder.semester);
                    } catch (IOException e)
                    {
                        throw Exceptions.propagate(e);
                    }
                }
            }).map(new Function<String,
                    Element>()
            {
                @Override
                public Element apply(String s) throws Exception
                {
                    return Jsoup
                            .parse(s)
                            .getElementsByTag("tbody").get(0);
                }
            }).map(new Function<Element, ArrayList<Selected>>()
            {
                @Override
                public ArrayList<Selected> apply(Element tbody) throws Exception
                {
                    ArrayList<Selected> buffer = new ArrayList<>();
                    Elements trs = tbody.getElementsByTag("tr");
                    for (int i = 1; i < trs.size() - 1; i++)
                    {
                        Elements tds = trs.get(i).getElementsByTag("td");
                        buffer.add(new Selected(tds.get(2).text(),
                                tds.get(3).text(),
                                tds.get(4).text(),
                                tds.get(1).text(),
                                tds.get(0).text()));
                    }
                    return buffer;
                }
            });

            final Observable<Elements> table = loginClient
                    .zipWith(builder,
                            new BiFunction<OkHttpClient,
                                    Builder,
                                    String>()
                            {
                                @Override
                                public String apply(OkHttpClient client,
                                                    Builder builder) throws Exception
                                {
                                    try
                                    {
                                        return post(client, builder.table, builder.semester);
                                    } catch (IOException e)
                                    {
                                        throw Exceptions.propagate(e);
                                    }
                                }
                            }).map(new Function<String,
                            Elements>()
                    {
                        @Override
                        public Elements apply(String s) throws Exception
                        {
                            return Jsoup.parse(s).getElementsByTag("tbody");
                        }
                    });

            Observable<ArrayList<Answer>> answers
                    = table.map(new Function<Elements,
                    Element>()
            {
                @Override
                public Element apply(Elements tbodys) throws Exception
                {
                    return tbodys.get(3);
                }
            }).map(new Function<Element, ArrayList<Answer>>()
            {
                @Override
                public ArrayList<Answer> apply(Element tbody) throws Exception
                {
                    ArrayList<Answer> buffer = new ArrayList<>();
                    Elements trs = tbody.getElementsByTag("tr");
                    for (int i = 1; i < trs.size(); i++)
                    {
                        Elements tds = trs.get(i).getElementsByTag("td");
                        String teacher = tds.get(3).text();
                        if (!TextUtils.isEmpty(teacher))
                        {
                            buffer.add(new Answer(tds.get(1).text(),
                                    tds.get(0).text(),
                                    tds.get(2).text(),
                                    teacher,
                                    tds.get(4).text(),
                                    Integer.parseInt(tds.get(5).text()),
                                    Integer.parseInt(tds.get(6).text()),
                                    Integer.parseInt(tds.get(7).text()),
                                    tds.get(8).text()));
                        }
                    }
                    return buffer;
                }
            });

            Observable<ArrayList<Experimental>> experimental = table
                    .map(new Function<Elements, Element>()
                    {
                        @Override
                        public Element apply(Elements tbodys) throws Exception
                        {
                            return tbodys.get(1);
                        }
                    }).map(new Function<Element, ArrayList<Experimental>>()
                    {
                        @Override
                        public ArrayList<Experimental> apply(Element tbody) throws Exception
                        {
                            Pattern pattern1 = Pattern
                                    .compile("第([0-9]){1,2}周,星期([一二三四五六日]),第(([0-9]、)*?)([0-9])大节");
                            Pattern pattern2 = Pattern.compile("[0-9]、");
                            HashMap<Character, Integer> dayOfWeekTable
                                    = new HashMap<Character, Integer>()
                            {
                                {
                                    put('一', 1);
                                    put('二', 2);
                                    put('三', 3);
                                    put('四', 4);
                                    put('五', 5);
                                    put('六', 6);
                                    put('日', 7);
                                }
                            };
                            ArrayList<Experimental> buffer = new ArrayList<>();
                            ArrayList<Integer> temp = new ArrayList<>();
                            Elements trs = tbody.getElementsByTag("tr");
                            if (trs.size() > 1)
                            {
                                for (int i = 1; i < trs.size(); i++)
                                {
                                    Elements tds = trs.get(i).getElementsByTag("td");
                                    Matcher matcher = pattern1.matcher(tds.get(3).text());
                                    if (matcher.find())
                                    {
                                        String course = tds.get(0).text();
                                        String name = tds.get(1).text();
                                        int batch = Integer.parseInt(tds.get(2).text().trim());
                                        Matcher matcher2 = pattern2.matcher(matcher.group(3));
                                        while (matcher2.find())
                                        {
                                            temp.add(Character.getNumericValue(matcher2.group().charAt(0)));
                                        }
                                        temp.add(Integer.valueOf(matcher.group(5)));
                                        String classroom = tds.get(4).text();
                                        String major = tds.get(5).text();
                                        int week = Integer.parseInt(matcher.group(1).trim());
                                        int dayOfWeek = dayOfWeekTable.get(matcher.group(2).charAt(0));
                                        if (temp.size() != 0)
                                        {
                                            for (int value : temp)
                                            {
                                                buffer.add(new Experimental(dayOfWeek,
                                                        course,
                                                        week,
                                                        classroom,
                                                        batch,
                                                        name,
                                                        major,
                                                        2,
                                                        value * 2 - 1));
                                            }
                                            temp.clear();
                                        }
                                    }
                                }
                            }
                            return buffer;
                        }
                    });

            Observable<Tuple2<ArrayList<Course>, ArrayList<Selected>>> coursesAndSelected = table
                    .map(new Function<Elements, Element>()
                    {
                        @Override
                        public Element apply(Elements tbodys) throws Exception
                        {
                            return tbodys.get(0);
                        }
                    }).zipWith(selected, new BiFunction<Element, ArrayList<Selected>,
                            Tuple2<ArrayList<Course>, ArrayList<Selected>>>()
                    {

                        private Selected getSelectByNumber(ArrayList<Selected> selects,
                                                           String number)
                        {
                            for (Selected select : selects)
                            {
                                if (select.number.equals(number))
                                {
                                    return select;
                                }
                            }
                            return null;
                        }

                        @Override
                        public Tuple2<ArrayList<Course>
                                , ArrayList<Selected>> apply(Element tbody,
                                                             ArrayList<Selected> selecteds)
                                throws Exception
                        {
                            Pattern pattern = Pattern
                                    .compile("((.*?) \\(([0-9]{1,2})-([0-9]{1,2})\\)([A-Za-z0-9*]*?) 课号：([0-9]*))");
                            Elements trs1 = tbody.getElementsByTag("tr");
                            ArrayList<Course> buffer = new ArrayList<>();
                            //class
                            for (int i = 1; i < trs1.size() - 1; i++)
                            {
                                Element tr = trs1.get(i);
                                Elements tds = tr.getElementsByTag("td");
                                //day
                                for (int j = 0; j < tds.size(); j++)
                                {
                                    Element td = tds.get(j);
                                    String text = td.text();
                                    if (text.length() != 1)
                                    {
                                        Matcher matcher = pattern
                                                .matcher(text);
                                        while (matcher.find())
                                        {
                                            String number = matcher.group(6);
                                            Selected select = getSelectByNumber(selecteds,
                                                    number);
                                            buffer.add(new Course(j + 1, 2, i * 2 - 1,
                                                    matcher.group(2).trim(),
                                                    Integer.parseInt(matcher.group(3)),
                                                    Integer.parseInt(matcher.group(4)),
                                                    number,
                                                    matcher.group(5),
                                                    select == null ? "" : select.teacher,
                                                    select == null ? "" : select.code));
                                        }
                                    }
                                }
                            }
                            return Tuples.create(buffer, selecteds);
                        }
                    });
            return Observable.zip(answers,
                    experimental,
                    coursesAndSelected, builder, new Function4<ArrayList<Answer>,
                            ArrayList<Experimental>,
                            Tuple2<ArrayList<Course>,
                                    ArrayList<Selected>>,
                            Builder, Timetable>()
                    {
                        @Override
                        public Timetable apply(ArrayList<Answer> answers,
                                               ArrayList<Experimental> experimentals,
                                               Tuple2<ArrayList<Course>,
                                                             ArrayList<Selected>> objects,
                                               Builder builder) throws Exception
                        {
                            return new Timetable(builder, answers.toArray(new Answer[answers.size()]),
                                    objects.item2.toArray(new Selected[objects.item2.size()]),
                                    objects.item1.toArray(new Course[objects.item1.size()]),
                                    experimentals.toArray(new Experimental[experimentals.size()]));
                        }
                    }).observeOn(AndroidSchedulers.mainThread());
        }
    }

    public static final class Course
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

    public static final class Selected
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

    public static final class Answer
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

    public static final class Experimental
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

    private String semester;
    private Course[] courses;
    private Selected[] selects;
    private Answer[] answers;
    private Experimental[] experimentals;

    public String getSemester()
    {
        return semester;
    }


    public Experimental[] getExperimentals()
    {
        return Arrays.copyOf(experimentals, experimentals.length);
    }

    public Answer[] getAnswers()
    {
        return answers;
    }

    public Course[] getCourses()
    {
        return courses;
    }

    public Selected[] getSelects()
    {
        return selects;
    }

    private Timetable()
    {
    }

    private Timetable(Builder builder,
                      Answer[] answers,
                      Selected[] selects,
                      Course[] courses,
                      Experimental[] experimentals)
    {
        this.semester = builder.semester;
        this.answers = answers;
        this.selects = selects;
        this.courses = courses;
        this.experimentals = experimentals;
    }
}