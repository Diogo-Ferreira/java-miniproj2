package com.company.server.States;
import com.company.server.StationConnection;
import java.io.IOException;

/**
 * Implémentation de l'état status
 */
public class StatusState implements IState {
    @Override
    public void handleState(StationConnection context) throws IOException {
        //On s'assure que la connexion à bien été authentifié
        if(context.isLogged()){
            boolean stop = false;
            //On demande de recevoir les status
            context.getWriter().println("SENDSTATUS");
            context.getWriter().flush();

            while(!stop){
                String line = context.getReader().readLine();
                if(line.equals("ENDSTATUS")){
                    stop = true;
                }else {
                    //Découpage de la valeur, clef
                    String[] status = line.split(" ")[1].split("=");
                    context.getDataBase().insertStatus(status[0],status[1]);
                }
            }
            context.setCurrentState(new LogoutState());
        }
    }
}
