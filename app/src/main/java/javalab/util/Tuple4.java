package javalab.util;

import java.io.Serializable;

/**
 * Created by Mr.小世界 on 2018/9/30.
 */

public final class Tuple4<T1,T2,T3,T4> extends AbstractTuple implements Serializable
{
    private static final long serialVersionUID = 415664677807701514L;
    public final T1 item1;
    public final T2 item2;
    public final T3 item3;
    public final T4 item4;

    public Tuple4(T1 item1, T2 item2, T3 item3, T4 item4)
    {
        super(4);
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
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
            case 2:
            {
                return item3;
            }
            case 3:
            {
                return item4;
            }
        }
        throw new IndexOutOfBoundsException();
    }

}
