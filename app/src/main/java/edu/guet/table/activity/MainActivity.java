package edu.guet.table.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.model.ScheduleEnable;
import com.zhuangfei.timetable.view.WeekView;

import org.litepal.LitePal;

import java.util.ArrayList;

import edu.guet.table.R;
import edu.guet.table.datasource.Course;
import edu.guet.table.datasource.Experimental;
import edu.guet.table.datasource.Timetable;
import edu.guet.table.support.CodeParser;
import edu.guet.table.viewmodel.CourseAdapter;
import edu.guet.table.viewmodel.ExperimentalAdapter;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import javalab.util.AsyncCallback;


public class MainActivity extends AppCompatActivity
{
    private TimetableView mTimetableView;
    private WeekView mWeekView;
    private Timetable mTimetable;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        LitePal.initialize(this);
        SQLiteDatabase database= LitePal.getDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWeekView = (WeekView) findViewById(R.id.id_weekview);
        mTimetableView = (TimetableView) findViewById(R.id.id_timetableView);
        new Timetable.NetworkLoader()
                .account("1600301122", "13878234627")
                .semester("2018-2019_1")
                .observable()
                .doOnNext(new Consumer<Timetable>()
        {
            @Override
            public void accept(Timetable timetable) throws Exception
            {
                MainActivity.this.mTimetable = timetable;
            }
        }).map(new Function<Timetable, ArrayList<ScheduleEnable>>()
        {
            @Override
            public ArrayList<ScheduleEnable> apply(Timetable mTimetable) throws Exception
            {
                Logger.d(LitePal.findFirst(Timetable.class,true).getCourses().size());
                ArrayList<ScheduleEnable> scheduleEnables = new ArrayList<>();
                for (Course course : mTimetable.getCourses())
                {
                    scheduleEnables.add(new CourseAdapter(course));
                }
                for (Experimental experimental: mTimetable.getExperimentals())
                {
                    scheduleEnables.add(new ExperimentalAdapter(experimental));
                }
                return scheduleEnables;
            }
        }).subscribe(new Observer<ArrayList<ScheduleEnable>>()
        {
            private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

            @Override
            public void onSubscribe(Disposable d)
            {
                mTimetableView.showView();
                dialog.setMessage("数据加载中...");
                dialog.show();
            }

            @Override
            public void onNext(ArrayList<ScheduleEnable> value)
            {
                mTimetableView.source(value).curWeek(8).showView();
                dialog.dismiss();

                Toasty.success(MainActivity.this, "加载成功").show();
            }

            @Override
            public void onError(Throwable e)
            {
                dialog.dismiss();
                e.printStackTrace();
                Toasty.error(MainActivity.this, "加载失败").show();
            }

            @Override
            public void onComplete()
            {

            }
        });
        new Thread()
        {
            @Override
            public void run()
            {
               Logger.d(CodeParser.parse(BitmapFactory.decodeStream(getResources().openRawResource(R.raw.code))));

            }
        }.start();






        requestPermissions();
    }

    private final String request[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private void requestPermissions()
    {
        final ArrayList<String> toApplyList = new ArrayList<>();
        for (String perm : request)
        {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat
                    .checkSelfPermission(this, perm))
            {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        if (!toApplyList.isEmpty())
        {
            String[] toApplys = new String[toApplyList.size()];
            toApplyList.toArray(toApplys);
            ActivityCompat.requestPermissions(this, toApplys,
                    100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
