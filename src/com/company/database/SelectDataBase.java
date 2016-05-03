package com.company.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by diogo on 29.04.16.
 */
public class SelectDataBase extends DataBaseManager {

    private HashMap<String,String> status = new HashMap<>();

    public SelectDataBase(String DB_URL) {
        super(DB_URL);
    }

    public String[] getAvaibleStations(){
        String query = "SELECT name FROM stations";
        try {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            ArrayList<String> out = new ArrayList<>();
            while(resultSet.next()){
                out.add(resultSet.getString("name"));
            }
            return out.stream().toArray(String[]::new);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void getDataFromStation(String name){
        String query = "SELECT status.key,status.value FROM stations JOIN status ON status.stationId = stations.id WHERE stations.name=?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            status.clear();
            while (resultSet.next()){
                status.put(resultSet.getString("status.key"),resultSet.getString("status.value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public HashMap<String, String> getStatus() {
        return status;
    }
}
