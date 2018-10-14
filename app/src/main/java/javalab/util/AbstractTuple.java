package javalab.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Mr.小世界 on 2018/9/30.
 */

public abstract class AbstractTuple implements Tuple
{
    public transient final int length;

    protected AbstractTuple(int length)
    {
        this.length = length;
    }

    @Override
    public Iterator<Object> iterator()
    {
        return new TupleIterator();
    }

    @Override
    public int hashCode()
    {
        int result = 1;
        for (Object element : this)
        {
            result = 31 * result + (element == null ? 0 : element.hashCode());
        }
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!getClass().isInstance(obj))
        {
            return false;
        }
        AbstractTuple tuple = ((AbstractTuple) obj);
        for (int i = 0; i < length; i++)
        {
            Object a = get(i);
            Object b = tuple.get(i);
            if (!(a == b || (a != null && a.equals(b))))
            {
                return false;
            }
        }
        return true;
    }

    private class TupleIterator implements Iterator<Object>
    {
        private int index = 0;

        @Override
        public boolean hasNext()
        {
            return index + 1 <= length - 1;
        }

        @Override
        public Object next()
        {
            if (index > length - 1)
            {
                throw new NoSuchElementException();
            }
            return get(index++);
        }
    }

    @Override
    public Object[] toArray()
    {
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
        {
            objects[i] = get(i);
        }
        return objects;
    }

    @Override
    public int indexOf(Object o)
    {
        if (o == null)
        {
            return -1;
        }
        for (int i = 0; i < length; i++)
        {
            Object item = get(i);
            if (item == o || (item != null && item.equals(o)))
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Object o)
    {
        return indexOf(o) != -1;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("Tuple{");
        if (length >= 1)
        {
            builder.append(String.valueOf(get(1)));
            for (int i = 1; i < length; i++)
            {
                builder.append(',');
                builder.append(String.valueOf(get(i)));
            }
        } else
        {
            builder.append(' ');
        }
        builder.append('}');
        return builder.toString();
    }
}
