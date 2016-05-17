package com.company.database;
import sun.util.calendar.BaseCalendar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Created by diogo on 29.04.16.
 */
public class InsertDataBase extends DataBaseManager {

    private int stationId;


    public boolean checkLogin(String name,String pass){
        try {
            String query = "SELECT COUNT(*),id FROM stations WHERE name=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,pass);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next() && resultSet.getInt(1) == 1){
                stationId = resultSet.getInt(2);
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertData(String timestamp,String data){
        String query = "INSERT INTO data (qty,number,value,min,max,stationId,timestamp) VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            String[] values = data.split(" ");
            preparedStatement.setString(1,values[0]);
            preparedStatement.setInt(2,Integer.parseInt(values[1]));
            preparedStatement.setDouble(3,Double.parseDouble(values[2]));
            preparedStatement.setDouble(4,Double.parseDouble(values[3]));
            preparedStatement.setDouble(5,Double.parseDouble(values[4]));
            preparedStatement.setInt(6,stationId);
            preparedStatement.setString(7,timestamp);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertStatus(String key,String value){
        String query = "INSERT INTO status (status.key,status.value,stationId) VALUES(?,?,?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,key);
            preparedStatement.setString(2,value);
            preparedStatement.setInt(3,stationId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
