package com.company.server.States;

import com.company.server.StationConnection;

import java.io.IOException;

/**
 * Implémentation de l'état DATA
 */
public class DataState implements IState {
    @Override
    public void handleState(StationConnection context) throws IOException {
        //On s'assure que la connexion est bien authentifié
        if (context.isLogged()) {
            boolean stop = false;
            //On demande de nous envoyer les datas
            context.getWriter().println("SENDDATA");
            context.getWriter().flush();
            while(!stop){
                String line = context.getReader().readLine();
                if(line.equals("ENDDATA")){
                    stop = true;
                }else {

                    //Découpage
                    String[] firstCut = line.split(",");
                    String[] timingData = firstCut[0].split(" ");
                    String datetime = timingData[1] + " " + timingData[2];

                    //Entrée des différents mesures dans la BDD
                    for(int i = 1; i < firstCut.length;i++){
                        context.getDataBase().insertData(datetime,firstCut[i].replaceFirst(" ",""));
                    }
                }
            }
            //Quand on as finit on passe à l'état prochain qui est le status
            context.setCurrentState(new StatusState());
        }
    }
}
