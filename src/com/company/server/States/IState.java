package com.company.server.States;

import com.company.server.StationConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by diogo on 29.04.16.
 */
public interface IState {
    /**
     *
     * @param context
     * @throws IOException
     */
    void handleState(StationConnection context) throws IOException;
}
