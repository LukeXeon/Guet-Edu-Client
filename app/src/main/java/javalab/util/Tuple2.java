package javalab.util;

import java.io.Serializable;

/**
 * Created by Mr.小世界 on 2018/9/30.
 */

public final class Tuple2<T1,T2> extends AbstractTuple implements Serializable
{
    private static final long serialVersionUID = 7688522136358569543L;
    public final T1 item1;
    public final T2 item2;

    public Tuple2(T1 item1, T2 item2)
    {
        super(2);
        this.item1 = item1;
        this.item2 = item2;
    }

    @Override
    public Object get(int index)
    {
        switch (index)
        {
            case 0:
            {
                return item1;
            }
            case 1:
            {
                return item2;
            }
        }
        throw new IndexOutOfBoundsException();
    }

}
