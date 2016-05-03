package com.company.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by diogo on 29.04.16.
 */
public class Server {

    private int port;
    private int maxConnectionsAllowed;
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

    private void listen() throws IOException, InterruptedException {
        //Overrides CTRL + C
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
            if(connections.size() < maxConnectionsAllowed){
                Socket instance = serverSocket.accept();
                StationConnection sc = new StationConnection(instance,this);
                connections.add(sc);
                sc.start();
                System.out.println(connections);
            }
        }
    }
    synchronized public void onConnectionEnded(StationConnection sc){
        connections.remove(sc);
        System.out.println(sc + " removed");
    }
    private void stopServer(){
        stop = true;
    }
}
