package com.example.assh;
import java.sql.*;

public class dbconnection {
    Connection connection;
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String dbName="account";
        String username="root";
        String password="";
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection= DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,username,password);
        return connection;
    }

}
