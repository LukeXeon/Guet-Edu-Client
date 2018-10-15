package javalab.util;

import java.util.List;

/**
 * Created by Mr.小世界 on 2018/10/13.
 */

public final class Callbacks
{
    private Callbacks()
    {
    }

    public static <T> void safeCallback(AsyncCallback<T> callback, T value)
    {
        if (callback != null)
        {
            callback.onResult(value);
        }
    }

}
