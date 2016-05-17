package com.company.server.States;

import com.company.server.StationConnection;

import java.io.IOException;

/**
 * Created by diogo on 29.04.16.
 */
public class LogoutState implements IState {
    @Override
    public void handleState(StationConnection context) throws IOException {
        context.getWriter().println("LOGOUT");
        context.getWriter().flush();
        context.kill();
    }
}
