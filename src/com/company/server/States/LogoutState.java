package com.company.server.States;

import com.company.server.StationConnection;

import java.io.IOException;

/**
 * Implémentation de l'état logout
 */
public class LogoutState implements IState {
    @Override
    public void handleState(StationConnection context) throws IOException {
        if (context.isLogged()) {
            context.setLogged(false);
            context.getWriter().println("LOGOUT");
            context.getWriter().flush();
            context.kill();
        }
    }
}
