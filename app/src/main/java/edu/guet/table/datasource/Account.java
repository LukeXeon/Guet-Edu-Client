package edu.guet.table.datasource;

import org.litepal.annotation.Column;
import org.litepal.annotation.Encrypt;
import org.litepal.crud.LitePalSupport;

/**
 * Created by Mr.小世界 on 2018/10/15.
 */

public final class Account extends LitePalSupport
{
    Account() {}

    @Column(unique = true)
    private String username;
    private String password;
    public String getUsername()
    {
        return username;
    }
}
