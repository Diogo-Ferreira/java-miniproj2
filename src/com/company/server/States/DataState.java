package com.company.server.States;

import com.company.server.StationConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by diogo on 29.04.16.
 */
public class DataState implements IState {
    @Override
    public void handleState(StationConnection context) throws IOException {
        boolean stop = false;
        context.getPw().println("SENDDATA");
        context.getPw().flush();
        while(!stop){
            String line = context.getIn().readLine();
            if(line.equals("ENDDATA")){
                stop = true;
            }else {
                String[] firstCut = line.split(",");
                String[] timingData = firstCut[0].split(" ");
                String datetime = timingData[1] + " " + timingData[2];
                for(int i = 1; i < firstCut.length;i++){
                    context.getDataBase().insertData(datetime,firstCut[i].replaceFirst(" ",""));
                }
            }
        }
        context.setCurrentState(new StatusState());
    }
}