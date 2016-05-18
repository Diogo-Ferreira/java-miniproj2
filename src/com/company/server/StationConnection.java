package com.company.server;

import com.company.database.InsertDataBase;
import com.company.server.States.*;

import java.io.*;
import java.net.Socket;

/**
 * Gère une connexion à la station,
 * les différents état LOGIN,DATA... sont implémenter avec le pattern STATE
 */
public class StationConnection extends Thread {

    private Socket connection;
    private String stationName;
    private Server server;
    private boolean stop = false;
    private boolean logged = false;
    private IState currentState = new IdleState();
    private PrintWriter writer = null;
    private BufferedReader reader = null;
    private InsertDataBase dataBase;

    public StationConnection(Socket connection,Server server) {
        this.connection = connection;
        this.server = server;
        dataBase = new InsertDataBase();
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            writer = new PrintWriter(connection.getOutputStream());
            //Message de départ
            writer.println("HELLO");
            writer.flush();
            //On gère l'état actuel
            while(!stop){
                currentState.handleState(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //On signale le serveur qu'on as finit, comme ça il peut mettre à jour sa liste
        server.onConnectionEnded(this);
    }

    /**
     * Termine la connexion
     */
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

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public InsertDataBase getDataBase() {
        return dataBase;
    }
}
