package edu.guet.table.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Mr.小世界 on 2018/10/13.
 */
public final class CookieCache implements CookieJar
{
    private final Map<String, List<Cookie>> cache = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        cache.put(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url)
    {
        List<Cookie> result = cache.get(url.host());
        return result == null ? new ArrayList<Cookie>() : result;
    }
}
