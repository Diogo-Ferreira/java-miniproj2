package com.company.database;


import java.sql.*;

/**
 * Classe abstraite pour la gestion BDD
 */
public abstract class DataBaseManager {
    // JDBC driver name and database URL
    protected final String DB_URL = "jdbc:mysql://localhost/meteo_radar";
    //  Database credentials
    private final String USER = "root";
    private final String PASS = "";
    protected Connection conn = null;
    
    public DataBaseManager() {

        //Connection à la BDD
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
