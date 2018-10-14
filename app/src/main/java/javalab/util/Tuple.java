package javalab.util;

import java.util.Iterator;

/**
 * Created by Mr.小世界 on 2018/9/30.
 */

public interface Tuple extends Iterable<Object>
{
    Object get(int index);

    int indexOf(Object o);

    Object[] toArray();

    boolean contains(Object o);

    Iterator<Object> iterator();

    boolean equals(Object o);

    int hashCode();
}
