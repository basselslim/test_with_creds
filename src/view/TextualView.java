package view;

import java.sql.Time;
import java.util.*;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import model.*;
import model.Map;

import static java.lang.String.valueOf;


public class TextualView implements observer.Observer {

    Map map;
    Pane pane;
    TableView requestsTable;
    TableColumn<Intersection, Long> intersectionColumn;
    TableColumn<Intersection, Integer> durationColumn;
    TableColumn<Intersection, String> typeColumn;
    TableColumn<Intersection, Integer> requestIndexColumn;

    public TextualView(Map map, Pane pane) {
        this.map = map;
        this.pane = pane;
        createRequestList();
    }

    public void refreshTable() {
        if (!map.getTour().getListPaths().isEmpty()) {
            sortRequestsTable();
        }
        System.out.println(map.getTour().getListPaths());
    }

    public void createRequestList() {

        requestsTable = new TableView();
        requestsTable.setPlaceholder(new Label("No request to display"));
        requestsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        /*requestsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            System.out.println("-------------");
            System.out.println(obs);
            System.out.println(oldSelection);
            System.out.println(newSelection);
        });*/

        intersectionColumn = new TableColumn<>("Intersection Id");
        intersectionColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        intersectionColumn.setSortable(false);

        durationColumn = new TableColumn<>("Duration (s)");
        durationColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Intersection, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Intersection, Integer> p) {
                if (p.getValue() instanceof DeliveryPoint) {
                    return new ReadOnlyObjectWrapper(((DeliveryPoint) p.getValue()).getDeliveryDuration());
                } else if (p.getValue() instanceof PickUpPoint) {
                    return new ReadOnlyObjectWrapper(((PickUpPoint) p.getValue()).getPickUpDuration());
                } else {
                    return null;
                }
            }
        });

        typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Intersection, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Intersection, String> p) {
                if (p.getValue() instanceof DeliveryPoint) {
                    return new ReadOnlyObjectWrapper("Delivery");
                } else if (p.getValue() instanceof PickUpPoint) {
                    return new ReadOnlyObjectWrapper("Pick up");
                } else {
                    return null;
                }
            }
        });

        /*requestIndexColumn = new TableColumn<>("Request Index");
        requestIndexColumn.setVisible(false);
        durationColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Intersection, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Intersection, Integer> p) {
                return new ReadOnlyObjectWrapper(((DeliveryPoint) p.getValue()).getDeliveryDuration());
            }
        });*/

        requestsTable.getColumns().add(intersectionColumn);
        requestsTable.getColumns().add(durationColumn);
        requestsTable.getColumns().add(typeColumn);

        for (Request item:map.getListRequests()) {
            requestsTable.getItems().add(item.getPickUpPoint());
            requestsTable.getItems().add(item.getDeliveryPoint());
            //requestsTable.getItems().indexOf()
        }

        pane.getChildren().add(requestsTable);
    }

    public void sortRequestsTable() {
        int newTableIndex = 0;
        for (Path path: map.getTour().getListPaths().subList(1, map.getTour().getListPaths().size())) {
            long id = path.getIdDeparture();
            int tableIndex = 0;
            while (((Intersection)requestsTable.getItems().get(tableIndex)).getId() != id) {
                tableIndex++;
            }
            Intersection point = (Intersection)requestsTable.getItems().remove(tableIndex);
            requestsTable.getItems().add(newTableIndex, point);
            newTableIndex++;
        }
    }

    @Override
    public void update(observer.Observable observed, Object arg) {
        createRequestList();
    }
}