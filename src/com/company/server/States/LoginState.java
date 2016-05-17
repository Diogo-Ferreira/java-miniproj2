package com.company.server.States;
import com.company.server.StationConnection;

import java.io.IOException;

/**
 * Created by diogo on 29.04.16.
 */
public class LoginState implements IState {
    @Override
    public void handleState(StationConnection context) throws IOException {
        String line = context.getReader().readLine();
        if(line != null){
            String[] args = line.split(" ");
            if(args.length == 3 && context.getDataBase().checkLogin(args[1],args[2])){
                context.setName(args[1]);
                context.setLogged(true);
                System.out.println("Logged !");
                context.setCurrentState(new DataState());
            }else{
                System.out.println("ERROR");
            }
        }else{
            context.kill();
        }
    }
}
