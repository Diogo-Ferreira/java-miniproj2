package com.company.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe utile pour l'insertion des données météo dans la BDD
 */
public class InsertDataBase extends DataBaseManager {

    private int stationId = -1;

    /**
     * Login de la sation, stationId sera affecté si le
     * login est réussi.
     * @param name nom de la station
     * @param pass mot de passe (en clair, pas de md5 ici)
     * @return true si le login est correcte
     */
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

    /**
     * Insertion de data,le login doit être accomplit avant d'appeler cette méthode
     * @param timestamp date (en string), format mysql datetime
     * @param data donnée météo, ex. TE 39.49 39 69
     */
    public void insertData(String timestamp,String data){
        if (stationId != -1) {
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
    }

    /**
     * Insertion status, le login doit être accomplit avant d'appeler cette méthode
     * @param key clef
     * @param value valeur
     */
    public void insertStatus(String key,String value){

        if (stationId != -1) {
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
}
