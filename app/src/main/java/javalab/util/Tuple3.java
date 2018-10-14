package javalab.util;

import java.io.Serializable;

/**
 * Created by Mr.小世界 on 2018/9/30.
 */

public final class Tuple3<T1,T2,T3> extends AbstractTuple implements Serializable
{
    private static final long serialVersionUID = 4965826868741671750L;
    public final T1 item1;
    public final T2 item2;
    public final T3 item3;

    public Tuple3(T1 item1, T2 item2, T3 item3)
    {
        super(3);
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
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
        }
        throw new IndexOutOfBoundsException();
    }

}
