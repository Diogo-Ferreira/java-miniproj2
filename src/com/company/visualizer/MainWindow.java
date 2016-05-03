package com.company.visualizer;

import com.company.database.SelectDataBase;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.chart.AreaChart;
/**
 * Created by diogo on 03.05.16.
 */
public class MainWindow extends Application {
    private NumberAxis xAxis = new NumberAxis(1, 7, 0.1);
    private NumberAxis yAxis = new NumberAxis();
    private AreaChart<Number,Number> chart = new AreaChart<Number, Number>(xAxis,yAxis);
    private StatusPane statusPane = new StatusPane();
    private Group root;
    private Scene scene;
    private SelectDataBase dataBase = new SelectDataBase("jdbc:mysql://localhost/meteo_radar");
    private ChoiceBox choiceStation;
    private ChoiceBox choiceQuantity;
    private String currentStation;




    private EventHandler<ActionEvent> onConnection = event -> {
        if(dataBase != null){
            currentStation = choiceStation.getSelectionModel().getSelectedItem().toString();
            dataBase.getDataFromStation(currentStation);
            if(statusPane != null){
                statusPane.setStatus(dataBase.getStatus());
            }
        }
    };

    private EventHandler<ActionEvent> onShowGraph = event -> {
        System.out.println("Show graph");
    };

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        root = new Group();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        buildUi();
        primaryStage.setTitle("Meteo Radar Visualiser");
        primaryStage.show();
        primaryStage.sizeToScene();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void buildUi(){
        VBox mainLayout = new VBox();
        mainLayout.setPadding(new Insets(5,5,5,5));
        mainLayout.setSpacing(10);
        root.getChildren().add(mainLayout);
        mainLayout.setAlignment(Pos.CENTER);

        //Ajout selection stations
        HBox stationsChoixHLayout = new HBox();
        stationsChoixHLayout.setSpacing(10);
        stationsChoixHLayout.getChildren().add(new Label("Station :"));
        choiceStation = new ChoiceBox(FXCollections.observableArrayList(dataBase.getAvaibleStations()));
        stationsChoixHLayout.getChildren().add(choiceStation);
        Button connection = new Button("Connection");
        connection.setOnAction(onConnection);
        stationsChoixHLayout.getChildren().add(connection);
        stationsChoixHLayout.setAlignment(Pos.CENTER_LEFT);
        mainLayout.getChildren().add(stationsChoixHLayout);
        //FIN selection stations

        //Status Pane
        mainLayout.getChildren().add(statusPane);


        //Chart
        mainLayout.getChildren().add(chart);
        //Ajout selection quantity
        HBox qtyChoixHLayout = new HBox();
        qtyChoixHLayout.setSpacing(10);
        qtyChoixHLayout.getChildren().add(new Label("Quantity :"));
        choiceQuantity = new ChoiceBox(FXCollections.observableArrayList("Appolo", "Mars", "Venus"));
        qtyChoixHLayout.getChildren().add(choiceQuantity);
        Button showGraph = new Button("Show graph");
        showGraph.setOnAction(onShowGraph);
        qtyChoixHLayout.getChildren().add(showGraph);
        qtyChoixHLayout.setAlignment(Pos.CENTER_LEFT);
        mainLayout.getChildren().add(qtyChoixHLayout);
        //FIN selection stations

    }
}
