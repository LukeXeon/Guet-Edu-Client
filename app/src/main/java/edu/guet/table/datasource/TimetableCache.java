package edu.guet.table.datasource;

import com.orhanobut.logger.Logger;

import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import javalab.util.Optional;
import javalab.util.Tuple2;
import javalab.util.Tuples;

/**
 * Created by Mr.小世界 on 2018/10/13.
 */

public final class TimetableCache extends LitePalSupport implements Timetable
{
    @Column(unique = true)
    private String username;
    private String semester;
    private Course[] courses;
    private Selected[] selects;
    private Answer[] answers;
    private Experimental[] experimentals;

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public String getSemester()
    {
        return semester;
    }

    @Override
    public Answer[] getAnswers()
    {
        return Arrays.copyOf(answers,answers.length);
    }

    @Override
    public Course[] getCourses()
    {
        return Arrays.copyOf(courses,courses.length);
    }

    @Override
    public Selected[] getSelects()
    {
        return Arrays.copyOf(selects,selects.length);
    }

    @Override
    public Experimental[] getExperimentals()
    {
        return Arrays.copyOf(experimentals,experimentals.length);
    }

    public static Observable<TimetableCache> observable(String username, String semester)
    {
        return Observable.just(Tuples.create(username, semester)).map(new Function<Tuple2<String, String>, TimetableCache>()
                {
                    @Override
                    public TimetableCache apply(Tuple2<String, String> objects) throws Exception
                    {
                        return LitePal.where("username = ? and semester = ?",
                                objects.item1, objects.item2)
                                .findFirst(TimetableCache.class);

                    }
                }).subscribeOn(Schedulers.io());
    }

    public static TimetableCache cache(TimetableLoader loader)
    {
        TimetableCache cache = new TimetableCache(loader);
        cache.saveOrUpdate();
        return cache;
    }

    private TimetableCache()
    {
    }

    private TimetableCache(TimetableLoader timetableLoader)
    {
        username = timetableLoader.getUsername();
        semester = timetableLoader.getSemester();
        courses = timetableLoader.getCourses();
        selects = timetableLoader.getSelects();
        answers = timetableLoader.getAnswers();
        experimentals = timetableLoader.getExperimentals();
    }
}
