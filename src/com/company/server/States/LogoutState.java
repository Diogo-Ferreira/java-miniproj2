package com.company.server.States;

import com.company.server.StationConnection;

import java.io.IOException;

/**
 * Created by diogo on 29.04.16.
 */
public class LogoutState implements IState {
    @Override
    public void handleState(StationConnection context) throws IOException {
        context.getPw().println("LOGOUT");
        context.getPw().flush();
        context.kill();
    }
}