package com.company.visualizer;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diogo on 03.05.16.
 */
public class StatusPane extends ScrollPane {
    private HashMap<String,String> status = new HashMap<>();
    private VBox mainLayout = new VBox();

    public StatusPane() {
        status.put("test","testeu");
        status.put("test2","testeu");
        status.put("test3","testeu");
        status.put("test4","testeu");
        status.put("test5","testeu");
        status.put("test6","testeu");
        status.put("test7","testeu");
        refreshStatus();
        setContent(mainLayout);
        mainLayout.setPadding(new Insets(5,5,5,5));
        setPrefHeight(100);
        setBackground(Background.EMPTY);
    }

    public void refreshStatus(){
        mainLayout.getChildren().clear();
        for(Map.Entry<String, String> entry : status.entrySet()){
            Label lbl = new Label(entry.getKey());
            TextField txt = new TextField(entry.getValue());
            txt.setEditable(false);
            lbl.setTextAlignment(TextAlignment.RIGHT);
            HBox subLayout = new HBox();
            subLayout.getChildren().addAll(lbl,txt);
            subLayout.setFillHeight(true);
            subLayout.setSpacing(10);
            mainLayout.getChildren().add(subLayout);
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
