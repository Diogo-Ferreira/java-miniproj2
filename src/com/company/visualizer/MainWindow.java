package com.company.visualizer;

import com.company.database.SelectDataBase;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * Fenêtre principale visualiseur
 */
public class MainWindow extends Application {
    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();
    private LineChart<String,Number> chart = new LineChart<String, Number>(xAxis,yAxis);
    private StatusPane statusPane = new StatusPane();
    private Group root;
    private Scene scene;
    private SelectDataBase dataBase = new SelectDataBase();
    private ChoiceBox choiceStation;
    private ChoiceBox choiceQuantity;
    private String currentStation;

    /**
     * Gestion onclick button connection
     */
    private EventHandler<ActionEvent> onConnection = event -> {
        if(dataBase != null && !choiceStation.getSelectionModel().isEmpty()){

            //Station actuel
            currentStation = choiceStation.getSelectionModel().getSelectedItem().toString();

            //Fetch status
            dataBase.getStatusFromStation(currentStation);

            if(statusPane != null){
                statusPane.setStatus(dataBase.getStatus());
            }
            if(dataBase.getStatus().isEmpty()){
                statusPane.getStatus().put("No statuses to show","");
                statusPane.refreshStatus();
            }

            //On réactive le choice pour afficher les quantitées
            choiceQuantity.setDisable(false);
            chart.getData().clear();
        }
    };

    /**
     * Gestion onclick button showgraph
     */
    private EventHandler<ActionEvent> onShowGraph = event -> {
        if(!choiceQuantity.getSelectionModel().isEmpty()){

            //On supprime les données présentes
            chart.getData().clear();

            //Quantité à afficher
            String currentQty = choiceQuantity.getSelectionModel().getSelectedItem().toString();

            //Fetch des données dans la BDD
            dataBase.getDataFromStation(currentQty,currentStation);

            //Si la qty est déjà présente dans les séries ont l'enlève
            chart.setData(FXCollections.observableArrayList(chart.getData().filtered(numberNumberSeries -> !numberNumberSeries.getName().equals(currentQty))));

            //Remplissage des données dans le chart
            XYChart.Series<String,Number> seriesMin = new XYChart.Series<>();
            XYChart.Series<String,Number> seriesVal = new XYChart.Series<>();
            XYChart.Series<String,Number> seriesMax = new XYChart.Series<>();

            for(Map.Entry<String,double[]> e : dataBase.getData().entrySet()){
                //MIN
                seriesMin.getData().add(new XYChart.Data<>(e.getKey(),e.getValue()[0]));

                //Val
                seriesVal.getData().add(new XYChart.Data<>(e.getKey(),e.getValue()[1]));

                //MAX
                seriesMax.getData().add(new XYChart.Data<>(e.getKey(),e.getValue()[2]));
            }
            seriesMin.setName(currentQty+ " (MIN) ");
            seriesVal.setName(currentQty);
            seriesMax.setName(currentQty + " (MAX) ");
            chart.getData().addAll(seriesMin,seriesVal,seriesMax);
        }
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new Group();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        buildUi();
        primaryStage.setTitle("Meteo Radar Visualiser");
        primaryStage.show();
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        yAxis.setForceZeroInRange(false);
        chart.setAnimated(false);
    }

    /**
     * Construction de l'interface
     */
    private void buildUi(){
        VBox mainLayout = new VBox();
        mainLayout.setPadding(new Insets(5,5,5,5));

        mainLayout.setSpacing(10);
        root.getChildren().add(mainLayout);

        //Ajout selection stations
        HBox stationsChoixHLayout = new HBox();
        stationsChoixHLayout.setSpacing(10);

        stationsChoixHLayout.getChildren().add(new Label("Station :"));
        choiceStation = new ChoiceBox(FXCollections.observableArrayList(dataBase.getAvaibleStations()));

        stationsChoixHLayout.getChildren().add(choiceStation);
        Button connection = new Button("Connection");

        connection.setOnAction(onConnection);
        stationsChoixHLayout.getChildren().add(connection);

        mainLayout.getChildren().add(stationsChoixHLayout);
        stationsChoixHLayout.setStyle("-fx-alignment: center");
        //FIN selection stations


        //Status Pane
        mainLayout.getChildren().add(statusPane);


        //Chart
        mainLayout.getChildren().add(chart);
        chart.setStyle("-fx-alignment: center");


        //Ajout selection quantity
        HBox qtyChoixHLayout = new HBox();

        qtyChoixHLayout.setSpacing(10);
        qtyChoixHLayout.getChildren().add(new Label("Quantity :"));

        choiceQuantity = new ChoiceBox(FXCollections.observableArrayList(dataBase.getAvaibleQuantities()));
        choiceQuantity.setDisable(true);

        qtyChoixHLayout.getChildren().add(choiceQuantity);
        Button showGraph = new Button("Show graph");

        showGraph.setOnAction(onShowGraph);
        qtyChoixHLayout.getChildren().addAll(showGraph);

        mainLayout.getChildren().add(qtyChoixHLayout);
        qtyChoixHLayout.setStyle("-fx-alignment: center");
        //FIN selection quantity
    }
}
