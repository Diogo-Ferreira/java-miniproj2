package com.company.server.States;

import com.company.server.StationConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Interface de base pour l'implémentation du patron STATE
 */
public interface IState {
    /**
     * Chaque état (LOGIN,DATA,STATUS,LOGOUT..) Devra être implémenté
     * @param context connectionClient
     * @throws IOException
     */
    void handleState(StationConnection context) throws IOException;
}
