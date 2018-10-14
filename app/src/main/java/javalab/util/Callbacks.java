package javalab.util;

import java.util.List;

/**
 * Created by Mr.小世界 on 2018/10/13.
 */

public final class Callbacks
{
    private static Callback<Object> emptyCallback
            = new Callback<Object>()
    {
        @Override
        public void onResult(Object o) {}
    };

    private Callbacks()
    {
    }

    public static <T> void safeCallback(Callback<T> callback, T value)
    {
        if (callback != null)
        {
            callback.onResult(value);
        }
    }

    public static <T> void firstCallback(Callback<T> callback, List<T> value)
    {
        safeCallback(callback, value.get(0));
    }

    @SuppressWarnings("unchecked")
    public static <T> Callback<T> empty()
    {
        return (Callback<T>) emptyCallback;
    }
}
