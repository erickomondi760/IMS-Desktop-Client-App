package com.client.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DbConnector {
    private static Connection connection;

    public DbConnector() {
    }


    public static Connection getConnection(){
        try(Connection connection1 = DriverManager.
                getConnection("jdbc:oracle:thin:@localhost:1521:ORCL","system","Erick12345")){
            connection = connection1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
