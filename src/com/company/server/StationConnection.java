package com.company.server;

import com.company.database.InsertDataBase;
import com.company.database.SelectDataBase;
import com.company.server.States.*;

import java.io.*;
import java.net.Socket;

/**
 * Created by diogo on 29.04.16.
 */
public class StationConnection extends Thread {

    private Socket connection;
    private String stationName;
    private Server server;
    private boolean stop = false;
    private boolean logged = false;
    private IState currentState = new IdleState();
    private PrintWriter pw = null;
    private BufferedReader in = null;
    private InsertDataBase dataBase;

    public StationConnection(Socket connection,Server server) {
        this.connection = connection;
        this.server = server;
        dataBase = new InsertDataBase("jdbc:mysql://localhost/meteo_radar");
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            pw = new PrintWriter(connection.getOutputStream());
            pw.println("HELLO");
            pw.flush();
            while(!stop){
                currentState.handleState(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Signals the server that we're dead, so that it cans update connections list
        server.onConnectionEnded(this);
    }


    public void kill(){
        System.out.println("good bye");
        stop = true;
    }

    public IState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(IState currentState) {
        this.currentState = currentState;
    }
    public boolean isLogged() {
        return logged;
    }
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public PrintWriter getPw() {
        return pw;
    }

    public BufferedReader getIn() {
        return in;
    }

    public InsertDataBase getDataBase() {
        return dataBase;
    }
}
