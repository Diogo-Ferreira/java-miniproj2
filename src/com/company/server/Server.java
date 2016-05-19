package com.company.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Serveur de connexions
 */
public class Server {

    private int port;
    /**
     * Nombre de connexions max simultanées
     */
    private int maxConnectionsAllowed;
    /**
     * Le tableaux de connection permet d'avoir
     * un accès sur toutes les connexions actuelles
     */
    private ArrayList<StationConnection> connections;
    private boolean stop = false;

    public Server(int port,int maxConnectionsAllowed) {
        this.port = port;
        this.maxConnectionsAllowed = maxConnectionsAllowed;
        connections = new ArrayList<>();
        try {
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ecoute réseau
     * @throws IOException
     * @throws InterruptedException
     */
    private void listen() throws IOException, InterruptedException {

        //En cas de CTRL + C, on arrête toutes les connexions
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                stopServer();
                synchronized (connections) {
                    for(int i = 0; i < connections.size(); i++){
                        connections.get(i).kill();
                    }
                }
            }
        });

        ServerSocket serverSocket = new ServerSocket(port);

        while(!stop){
            //Si on peut encore accepter des machines
            if(connections.size() < maxConnectionsAllowed){
                Socket instance = serverSocket.accept();
                StationConnection sc = new StationConnection(instance,this);
                connections.add(sc);
                sc.start();
                System.out.println(connections);
            }
        }
    }

    /**
     * Callback lorsque la connexion se termine pour retirer
     * la connexion de la liste principale de connexions
     * @param sc
     */
    synchronized public void onConnectionEnded(StationConnection sc){
        connections.remove(sc);
        System.out.println(sc + " removed");
    }

    /**
     * Arrête la boucle principale du serveur
     */
    private void stopServer(){
        stop = true;
    }
}
