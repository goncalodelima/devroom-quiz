package pt.gongas.twinscore.utils.database;

import java.sql.Connection;

public abstract class DatabaseConnector {

    protected static Connection connection;

    public abstract void connect();
    public abstract void close();
    public abstract void createTables();
    public static Connection getConnection() { return connection; }

}
