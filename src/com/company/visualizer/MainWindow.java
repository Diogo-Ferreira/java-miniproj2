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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.chart.AreaChart;
import javafx.util.Pair;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * Created by diogo on 03.05.16.
 */
public class MainWindow extends Application {
    private NumberAxis xAxis = new NumberAxis("Nombre de jours passé depuis aujord'hui",0,30,1);
    private NumberAxis yAxis = new NumberAxis();
    private LineChart<Number,Number> chart = new LineChart<Number, Number>(xAxis,yAxis);
    private StatusPane statusPane = new StatusPane();
    private Group root;
    private Scene scene;
    private SelectDataBase dataBase = new SelectDataBase();
    private ChoiceBox choiceStation;
    private ChoiceBox choiceQuantity;
    private String currentStation;

    private EventHandler<ActionEvent> onConnection = event -> {
        if(dataBase != null && !choiceStation.getSelectionModel().isEmpty()){
            currentStation = choiceStation.getSelectionModel().getSelectedItem().toString();
            dataBase.getStatusFromStation(currentStation);
            if(statusPane != null){
                statusPane.setStatus(dataBase.getStatus());
            }
            if(dataBase.getStatus().isEmpty()){
                statusPane.getStatus().put("No statuses to show","");
                statusPane.refreshStatus();
            }
            choiceQuantity.setDisable(false);
            chart.getData().clear();
        }
    };

    private EventHandler<ActionEvent> onShowGraph = event -> {
        if(!choiceQuantity.getSelectionModel().isEmpty()){
            String currentQty = choiceQuantity.getSelectionModel().getSelectedItem().toString();
            dataBase.getDataFromStation(currentQty,currentStation,(int)xAxis.getUpperBound());
            //Si la qty est déjà présente dans les séries ont l'enlève
            chart.setData(FXCollections.observableArrayList(chart.getData().filtered(numberNumberSeries -> !numberNumberSeries.getName().equals(currentQty))));
            XYChart.Series<Number,Number> series = new XYChart.Series<>();
            for(Map.Entry<Pair<Integer,Integer>,Double> e : dataBase.getData().entrySet()){
                Double day = new Double(e.getKey().getKey());
                Double hour = new Double((double)e.getKey().getValue() / 24.0);
                series.getData().add(new XYChart.Data<>( hour + day,e.getValue()));
            }
            series.setName(currentQty);
            chart.getData().add(series);
            System.out.println(dataBase.getData());
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
        choiceQuantity = new ChoiceBox(FXCollections.observableArrayList(dataBase.getAvaibleQuantities()));
        choiceQuantity.setDisable(true);
        qtyChoixHLayout.getChildren().add(choiceQuantity);
        Button showGraph = new Button("Show graph");
        Button clearAllData = new Button("Effacer tout");
        clearAllData.setOnAction(event -> {
            chart.getData().clear();
        });
        TextField daysToShow = new TextField("30");
        TextField startDay = new TextField("0");
        daysToShow.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")){
                daysToShow.setText(oldValue);
            }else if(!newValue.isEmpty() && Integer.parseInt(newValue) > 100){
                daysToShow.setText(oldValue);
            }
            if(!daysToShow.getText().isEmpty()){
                xAxis.setUpperBound(Integer.parseInt(daysToShow.getText()));
            }
        });
        startDay.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")){
                startDay.setText(oldValue);
            }else if(!newValue.isEmpty() && (Integer.parseInt(newValue) > 100 || Integer.parseInt(newValue) > Integer.parseInt(daysToShow.getText()))){
                startDay.setText(oldValue);
            }
            if(!startDay.getText().isEmpty()){
                xAxis.setLowerBound(Integer.parseInt(startDay.getText()));
            }
        });
        showGraph.setOnAction(onShowGraph);
        qtyChoixHLayout.getChildren().addAll(showGraph,clearAllData,new Label("jour départ"),startDay,new Label("jour fin"),daysToShow);
        qtyChoixHLayout.setAlignment(Pos.CENTER_LEFT);
        mainLayout.getChildren().add(qtyChoixHLayout);
        //FIN selection stations
    }
}
