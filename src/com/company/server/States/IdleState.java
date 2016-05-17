package com.company.server.States;

import com.company.server.StationConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by diogo on 29.04.16.
 */
public class IdleState implements IState {
    @Override
    public void handleState(StationConnection context) throws IOException {
        System.out.println("Noting to do");
        context.setCurrentState(new LoginState());
    }
}
