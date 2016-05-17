package com.company.visualizer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by diogo on 03.05.16.
 */
public class StatusPane extends ScrollPane {
    private HashMap<String,String> status = new HashMap<>();
    private VBox mainLayout = new VBox();

    public StatusPane() {
        refreshStatus();
        setContent(mainLayout);
        //mainLayout.setPadding(new Insets(5,5,5,5));
        setPrefHeight(100);
        setStyle("-fx-background: white;-fx-border-color: white;");
        mainLayout.setStyle("-fx-background-color: white");
    }
    public void refreshStatus(){
        mainLayout.getChildren().clear();
        for(Map.Entry<String, String> entry : status.entrySet()){
            Label lbl = new Label(entry.getKey());
            TextField txt = new TextField(entry.getValue());
            txt.setEditable(false);
            txt.setAlignment(Pos.CENTER_LEFT);
            lbl.setTextAlignment(TextAlignment.RIGHT);
            HBox subLayout = new HBox();
            subLayout.getChildren().addAll(lbl,txt);
            lbl.setMinWidth(100);
            mainLayout.getChildren().add(subLayout);
        }

        if(status.isEmpty()){
            mainLayout.getChildren().add(new Label("No Status, please connect"));
        }
    }
    public void setStatus(HashMap<String, String> status) {
        this.status = status;
        this.refreshStatus();
    }
    public HashMap<String, String> getStatus() {
        return status;
    }
}
