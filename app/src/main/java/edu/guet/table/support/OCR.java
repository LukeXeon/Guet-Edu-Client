package edu.guet.table.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.UiThread;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import javalab.util.AsyncCallback;
import javalab.util.Callbacks;

/**
 * Created by Mr.小世界 on 2018/10/11.
 */

public final class OCR
{
    private static final String LANG = "eng";
    private final Context context;
    private final TessBaseAPI api;
    private final Scheduler scheduler;

    public OCR(Context context)
    {
        this.context = context.getApplicationContext();
        this.api = new TessBaseAPI();
        scheduler = Schedulers.from(Executors.newSingleThreadExecutor());
    }

    private Observable<String> check()
    {
        return Observable.create(new ObservableOnSubscribe<String>()
        {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception
            {
                File tessdataDir = new File(context
                        .getFilesDir(), "tessdata");
                File tessdata = new File(tessdataDir,
                        LANG + ".traineddata");
                if (!tessdataDir.exists())
                {
                    tessdataDir.mkdir();
                }
                if (!tessdata.exists())
                {
                    InputStream is = context
                            .getAssets()
                            .open("tessdata/" + LANG + ".traineddata");
                    FileOutputStream os = new FileOutputStream(tessdata);
                    byte[] bytes = new byte[2048];
                    for (int len; (len = is.read(bytes)) != -1; )
                    {
                        os.write(bytes, 0, len);
                    }
                    os.flush();
                    is.close();
                    os.close();
                }
                e.onNext(context.getFilesDir().getAbsolutePath());
                e.onComplete();
            }
        }).subscribeOn(scheduler);
    }

    public Observable<String> observable(final Bitmap bitmap)
    {
        return check().map(new Function<String, String>()
        {
            @Override
            public String apply(String path) throws Exception
            {
                try
                {
                    api.init(path, LANG);
                    api.setImage(bitmap);
                    return api.getUTF8Text();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    throw Exceptions.propagate(e);
                }
            }
        });
    }

    @UiThread
    public void async(Bitmap bitmap, final AsyncCallback<String> callback)
    {
        observable(bitmap).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>()
                {
                    @Override
                    public void accept(String s) throws Exception
                    {
                        Callbacks.safeCallback(callback,s);
                    }
                });
    }
}
