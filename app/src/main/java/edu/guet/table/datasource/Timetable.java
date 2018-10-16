package edu.guet.table.datasource;

import android.os.Looper;
import android.support.annotation.UiThread;
import android.text.TextUtils;


import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.annotation.Encrypt;
import org.litepal.crud.LitePalSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.guet.table.support.CookieCache;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;
import javalab.util.AsyncCallback;
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


public final class Timetable extends LitePalSupport
{
    public static final class CacheLoader
    {
        private String semester;
        private String username;

        public CacheLoader username(String username)
        {
            this.username = username;
            return this;
        }

        public CacheLoader semester(String semester)
        {
            this.semester = semester;
            return this;
        }

        @UiThread
        public void async(final AsyncCallback<Timetable> callback)
        {
            observable().observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Timetable>()
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

        public void clear()
        {
            LitePal.deleteAll(Timetable.class,
                    "username = ? and semester = ?",
                    username,
                    semester);
        }

        public Observable<Timetable> observable()
        {
            check();
            return build(this);
        }

        private void check()
        {
            if (TextUtils.isEmpty(username))
            {
                throw new IllegalArgumentException();
            }
            Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{4}_(1|2)");
            if (TextUtils.isEmpty(this.semester)
                    || !pattern.matcher(this.semester).matches())
            {
                throw new IllegalArgumentException(
                        "semester is empty or illegal format ! (regex = "
                                + pattern + ")");
            }
        }

        public static Observable<Timetable> build(CacheLoader src)
        {
            return Observable.just(src).subscribeOn(Schedulers.io())
                    .map(new Function<CacheLoader, Timetable>()
                    {
                        @Override
                        public Timetable apply(CacheLoader cache) throws Exception
                        {
                            return LitePal.where("username = ? and semester = ?",
                                    cache.username, cache.semester)
                                    .findFirst(Timetable.class);
                        }
                    });
        }

    }

    public static final class NetworkLoader
    {
        private String username;
        private String password;
        private String semester;

        private String login;
        private String table;
        private String selected;
        private int timeout;

        public NetworkLoader account(String username, String password)
        {
            this.username = username;
            this.password = password;
            return this;
        }

        public NetworkLoader semester(String semester)
        {
            this.semester = semester;
            return this;
        }

        @UiThread
        public void async(final AsyncCallback<Timetable> callback)
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

        public NetworkLoader login(String login)
        {
            this.login = login;
            return this;
        }

        public NetworkLoader table(String table)
        {
            this.table = table;
            return this;
        }

        public NetworkLoader selected(String selected)
        {
            this.selected = selected;
            return this;
        }

        public NetworkLoader timeout(int timeout)
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

        private static Observable<Timetable> build(NetworkLoader source)
        {
            Observable<NetworkLoader> builder = Observable
                    .just(source)
                    .subscribeOn(Schedulers.io());
            Observable<OkHttpClient> loginClient = builder
                    .map(new Function<NetworkLoader, OkHttpClient>()
                    {
                        @Override
                        public OkHttpClient apply(NetworkLoader builder) throws Exception
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
                    = loginClient.zipWith(builder, new BiFunction<OkHttpClient, NetworkLoader, String>()
            {
                @Override
                public String apply(OkHttpClient client,
                                    NetworkLoader builder) throws Exception
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
                        Selected selected1 = new Selected();
                        selected1.name = tds.get(2).text();
                        selected1.teacher = tds.get(3).text();
                        selected1.type = tds.get(4).text();
                        selected1.code = tds.get(1).text();
                        selected1.number = tds.get(0).text();
                        buffer.add(selected1);
                    }
                    return buffer;
                }
            });

            final Observable<Elements> table = loginClient
                    .zipWith(builder,
                            new BiFunction<OkHttpClient,
                                    NetworkLoader,
                                    String>()
                            {
                                @Override
                                public String apply(OkHttpClient client,
                                                    NetworkLoader builder) throws Exception
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
                            Answer answer = new Answer();
                            answer.code = tds.get(1).text();
                            answer.number = tds.get(0).text();
                            answer.name = tds.get(2).text();
                            answer.teacher = teacher;
                            answer.classroom = tds.get(4).text();
                            answer.beginWeek = Integer.parseInt(tds.get(5).text());
                            answer.endWeek = Integer.parseInt(tds.get(6).text());
                            answer.dayOfWeek = Integer.parseInt(tds.get(7).text());
                            answer.time = tds.get(8).text();
                            buffer.add(answer);
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
                                                Experimental experimental1 = new Experimental();
                                                experimental1.dayOfWeek = dayOfWeek;
                                                experimental1.course = course;
                                                experimental1.week = week;
                                                experimental1.classroom = classroom;
                                                experimental1.batch = batch;
                                                experimental1.name = name;
                                                experimental1.major = major;
                                                experimental1.step = 2;
                                                experimental1.start = value * 2 - 1;
                                                buffer.add(experimental1);
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
                                            Course course = new Course();
                                            course.dayOfWeek = j + 1;
                                            course.step = 2;
                                            course.start = i * 2 - 1;
                                            course.name = matcher.group(2).trim();
                                            course.beginWeek = Integer.parseInt(matcher.group(3));
                                            course.endWeek = Integer.parseInt(matcher.group(4));
                                            course.number = number;
                                            course.classroom = matcher.group(5);
                                            course.teacher = select == null ? "" : select.teacher;
                                            course.code = select == null ? "" : select.code;
                                            buffer.add(course);
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
                            NetworkLoader, Timetable>()
                    {
                        @Override
                        public Timetable apply(ArrayList<Answer> answers,
                                               ArrayList<Experimental> experimentals,
                                               Tuple2<ArrayList<Course>,
                                                       ArrayList<Selected>> objects,
                                               NetworkLoader loader) throws Exception
                        {
                            LitePal.deleteAll(Timetable.class,
                                    "username = ? and semester = ?",
                                    loader.username,
                                    loader.semester);
                            Timetable timetable = new Timetable();
                            timetable.username = loader.username;
                            timetable.semester = loader.semester;
                            timetable.answers = answers;
                            timetable.selects = objects.item2;
                            timetable.courses = objects.item1;
                            timetable.experimentals = experimentals;
                            timetable.save();
                            return timetable;
                        }
                    }).observeOn(AndroidSchedulers.mainThread());
        }
    }

    private String username;
    private String semester;
    private List<Course> courses;
    private List<Selected> selects;
    private List<Answer> answers;
    private List<Experimental> experimentals;

    public String getSemester()
    {
        return semester;
    }

    public List<Experimental> getExperimentals()
    {
        return experimentals;
    }

    public List<Answer> getAnswers()
    {
        return answers;
    }

    public List<Course> getCourses()
    {
        return courses;
    }

    public List<Selected> getSelects()
    {
        return selects;
    }

    @Override
    public boolean save()
    {
        LitePal.saveAll(answers);
        LitePal.saveAll(courses);
        LitePal.saveAll(selects);
        LitePal.saveAll(experimentals);
        return super.save();
    }

    private Timetable()
    {
    }
}