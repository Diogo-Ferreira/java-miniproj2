package com.company.server.States;

import com.company.server.StationConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Etat Idle, lorsque la machine ne doit rien faire, ou démarrer
 * ce qui est le cas ici .
 */
public class IdleState implements IState {
    @Override
    public void handleState(StationConnection context) throws IOException {
        context.setCurrentState(new LoginState());
    }
}
