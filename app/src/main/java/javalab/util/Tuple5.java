package javalab.util;

import java.io.Serializable;

/**
 * Created by Mr.小世界 on 2018/9/30.
 */

public final class Tuple5<T1,T2,T3,T4,T5> extends AbstractTuple implements Serializable
{
    private static final long serialVersionUID = 3191653692641602520L;
    public final T1 item1;
    public final T2 item2;
    public final T3 item3;
    public final T4 item4;
    public final T5 item5;

    public Tuple5(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5)
    {
        super(5);
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
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
            case 4:
            {
                return item5;
            }
        }
        throw new IndexOutOfBoundsException();
    }

}
