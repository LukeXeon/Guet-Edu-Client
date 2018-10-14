package javalab.util;

/**
 * Created by Mr.小世界 on 2018/10/14.
 */

public interface Callback2<Result> extends Callback<Result>
{
    void onError(Throwable throwable);
}
