package javalab.util;

import java.util.NoSuchElementException;

/**
 * Created by Mr.小世界 on 2018/10/14.
 */

public final class Optional<T>
{
    private final T value;

    public Optional(T value)
    {
        this.value = value;
    }

    public boolean isEmpty()
    {
        return value == null;
    }

    public T get()
    {
        if (value != null)
        {
            return value;
        } else
        {
            throw new NoSuchElementException();
        }
    }

    public static <V> Optional<V> create(V value)
    {
        return new Optional<>(value);
    }

    @Override
    public int hashCode()
    {
        return value == null ? 0 : value.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj) || (value == null && obj == null) || (value != null && value.equals(obj));
    }
}
