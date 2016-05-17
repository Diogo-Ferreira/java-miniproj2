package com.company.database;

import javafx.util.Pair;

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
    private HashMap<Pair<Integer,Integer>,Double> data = new HashMap<>();
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
    public void getStatusFromStation(String name){
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
    public void getDataFromStation(String qty,String name,int nbDays){
        String query = "SELECT data.value,HOUR(data.timestamp) as hour,DATEDIFF(NOW(),data.timestamp) as day FROM" +
                " data JOIN stations on data.stationId=stations.id WHERE qty=? AND stations.name=?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,qty);
            preparedStatement.setString(2,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            data.clear();
            while (resultSet.next()){
                data.put(new Pair<>(resultSet.getInt("day"),resultSet.getInt("hour")),resultSet.getDouble("data.value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String[] getAvaibleQuantities(){
        String query = "SELECT DISTINCT qty FROM data";
        try {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            ArrayList<String> out = new ArrayList<>();
            while(resultSet.next()){
                out.add(resultSet.getString("qty"));
            }
            return out.stream().toArray(String[]::new);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public HashMap<String, String> getStatus() {
        return status;
    }

    public HashMap<Pair<Integer,Integer>, Double> getData() {
        return data;
    }
}
