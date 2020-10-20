package view;

import java.sql.Time;
import java.util.*;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import model.*;
import model.Map;


public class TextualView implements Observer {

    TableView requestsTable;
    TableColumn<Intersection, Long> intersectionColumn;
    TableColumn<Intersection, Time> durationColumn;


    public TextualView() {
        requestsTable = new TableView();
        intersectionColumn = new TableColumn<>("Where");
        intersectionColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationColumn.setCellValueFactory(cellule -> {
            if (cellule.getValue() instanceof DeliveryPoint) {
                return (ObservableValue<Time>) ((DeliveryPoint) cellule.getValue()).getDeliveryDuration();
            } else if (cellule.getValue() instanceof PickUpPoint){
                return (ObservableValue<Time>) ((PickUpPoint) cellule.getValue()).getPickUpDuration();
            } else {
                return null;
            }
        });
        requestsTable.getColumns().add(intersectionColumn);
        requestsTable.getColumns().add(durationColumn);
        StackPane root = new StackPane();
        root.getChildren().add(requestsTable);
    }

    public void updateRequestList(Map map) {
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}