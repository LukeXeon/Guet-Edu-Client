package javalab.util;


import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by Mr.小世界 on 2018/9/30.
 */

public final class Tuples
{
    private Tuples()
    {
    }

    public static <T1, T2, T3, T4, T5, T6, T7, TRest extends AbstractTuple>
    TupleR<T1, T2, T3, T4, T5, T6, T7, TRest>
    join(Tuple7<T1, T2, T3, T4, T5, T6, T7> tuple, TRest rest)
    {
        return new TupleR<>(tuple.item1, tuple.item2, tuple.item3, tuple.item4, tuple.item5, tuple.item6, tuple.item7, rest);
    }

    public static <T1, T2, T3, T4, T5, T6, T7>
    Tuple7<T1, T2, T3, T4, T5, T6, T7>
    join(Tuple6<T1, T2, T3, T4, T5, T6> tuple, T7 item7)
    {
        return new Tuple7<>(tuple.item1, tuple.item2, tuple.item3, tuple.item4, tuple.item5, tuple.item6, item7);
    }

    public static <T1, T2, T3, T4, T5, T6>
    Tuple6<T1, T2, T3, T4, T5, T6>
    join(Tuple5<T1, T2, T3, T4, T5> tuple, T6 item6)
    {
        return new Tuple6<>(tuple.item1, tuple.item2, tuple.item3, tuple.item4, tuple.item5, item6);
    }

    public static <T1, T2, T3, T4, T5>
    Tuple5<T1, T2, T3, T4, T5>
    join(Tuple4<T1, T2, T3, T4> tuple, T5 item5)
    {
        return new Tuple5<>(tuple.item1, tuple.item2, tuple.item3, tuple.item4,item5);
    }

    public static <T1, T2, T3, T4>
    Tuple4<T1, T2, T3, T4>
    join(Tuple3<T1, T2, T3> tuple, T4 item4)
    {
        return new Tuple4<>(tuple.item1, tuple.item2, tuple.item3,item4);
    }

    public static <T1, T2, T3>
    Tuple3<T1, T2, T3>
    join(Tuple2<T1, T2> tuple, T3 item3)
    {
        return new Tuple3<>(tuple.item1, tuple.item2, item3);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, TRest extends AbstractTuple>
    TupleR<T1, T2, T3, T4, T5, T6, T7, TRest>
    create(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6, T7 item7, TRest rest)
    {
        return new TupleR<>(item1, item2, item3, item4, item5, item6, item7, rest);
    }

    public static <T1, T2, T3, T4, T5, T6, T7>
    Tuple7<T1, T2, T3, T4, T5, T6, T7>
    create(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6, T7 item7)
    {
        return new Tuple7<>(item1, item2, item3, item4, item5, item6, item7);
    }

    public static <T1, T2, T3, T4, T5, T6>
    Tuple6<T1, T2, T3, T4, T5, T6>
    create(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6)
    {
        return new Tuple6<>(item1, item2, item3, item4, item5, item6);
    }

    public static <T1, T2, T3, T4, T5>
    Tuple5<T1, T2, T3, T4, T5>
    create(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5)
    {
        return new Tuple5<>(item1, item2, item3, item4, item5);
    }

    public static <T1, T2, T3, T4>
    Tuple4<T1, T2, T3, T4>
    create(T1 item1, T2 item2, T3 item3, T4 item4)
    {
        return new Tuple4<>(item1, item2, item3, item4);
    }

    public static <T1, T2, T3>
    Tuple3<T1, T2, T3>
    create(T1 item1, T2 item2, T3 item3)
    {
        return new Tuple3<>(item1, item2, item3);
    }

    public static <T1, T2>
    Tuple2<T1, T2>
    create(T1 item1, T2 item2)
    {
        return new Tuple2<>(item1, item2);
    }

    private static class EmptyTuple extends AbstractTuple implements Serializable
    {
        private static final long serialVersionUID = -3735687138701002587L;

        private EmptyTuple()
        {
            super(0);
        }

        @Override
        public Object get(int index)
        {
            throw new IndexOutOfBoundsException("Index: " + index);
        }


        @Override
        public boolean contains(Object o)
        {
            return false;
        }

        @Override
        public Iterator<Object> iterator()
        {
            return EmptyTupleIterator.EMPTY_TUPLE_ITERATOR;
        }

        private static class EmptyTupleIterator implements Iterator<Object>
        {
            static final EmptyTupleIterator EMPTY_TUPLE_ITERATOR
                    = new EmptyTupleIterator();

            public boolean hasNext()
            {
                return false;
            }

            public Object next()
            {
                throw new NoSuchElementException();
            }
        }

    }

    public static final Tuple EMPTY_TUPLE = new EmptyTuple();

}
