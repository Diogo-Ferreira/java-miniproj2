package com.company.server.States;
import com.company.server.StationConnection;
import java.io.IOException;

/**
 * Created by diogo on 29.04.16.
 */
public class StatusState implements IState {
    @Override
    public void handleState(StationConnection context) throws IOException {
        boolean stop = false;
        context.getPw().println("SENDSTATUS");
        context.getPw().flush();
        while(!stop){
            String line = context.getIn().readLine();
            if(line.equals("ENDSTATUS")){
                stop = true;
            }else {
                String[] status = line.split(" ")[1].split("=");
                context.getDataBase().insertStatus(status[0],status[1]);
            }
        }
        context.setCurrentState(new LogoutState());
    }
}
