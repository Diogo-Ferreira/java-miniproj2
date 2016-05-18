package com.company.server.States;
import com.company.server.StationConnection;

import java.io.IOException;

/**
 * Implémentation de l'état Login
 */
public class LoginState implements IState {
    @Override
    public void handleState(StationConnection context) throws IOException {
        String line = context.getReader().readLine();
        if(line != null){
            //Check login
            String[] args = line.split(" ");
            if(args.length == 3 && context.getDataBase().checkLogin(args[1],args[2])){
                context.setStationName(args[1]);
                context.setLogged(true);
                System.out.println(context.getStationName() + " Logged !");

                //On passe au prochain état
                context.setCurrentState(new DataState());
            }else{
                System.out.println("INCORRECT LOGIN FOR " + args[1]);
                //On pourrait aussi penser à fermer le connexion ici
            }
        }else{
            context.kill();
        }
    }
}
