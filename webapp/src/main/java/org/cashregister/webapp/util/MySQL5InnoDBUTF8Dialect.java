package org.cashregister.webapp.util;

import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.BooleanType;

import java.sql.Types;

/**
 * This class forces UTF8 dialect for MySQL.
 */
public class MySQL5InnoDBUTF8Dialect extends MySQL5InnoDBDialect {

    public MySQL5InnoDBUTF8Dialect() {
        super();
        registerColumnType(Types.BOOLEAN, "bit(1)");
        registerFunction("useindex", new StandardSQLFunction("useindex", BooleanType.INSTANCE));
    }

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }

}
