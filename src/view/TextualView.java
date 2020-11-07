package view;

import java.sql.Time;
import java.util.*;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
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

    public void createRequestList() {

        requestsTable = new TableView();
        requestsTable.setPlaceholder(new Label("No request to display"));
        requestsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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
        durationColumn.setSortable(false);

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
        typeColumn.setSortable(false);

        requestIndexColumn = new TableColumn<>("Request Index");
        requestIndexColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Intersection, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Intersection, Integer> p) {
                Request req = map.getRequestByTourStopId(p.getValue().getId());
                int index = map.getListRequests().indexOf(req) + 1;
                return new ReadOnlyObjectWrapper(index);
            }
        });
        requestIndexColumn.setSortable(false);

        requestsTable.getColumns().add(requestIndexColumn);
        requestsTable.getColumns().add(durationColumn);
        requestsTable.getColumns().add(typeColumn);

        requestsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        requestsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            long id = ((Intersection) newSelection).getId();
            selectRequest(id);
        });

        for (Request item : map.getListRequests()) {
            requestsTable.getItems().add(item.getPickUpPoint());
            requestsTable.getItems().add(item.getDeliveryPoint());
        }

        pane.getChildren().add(requestsTable);
    }

    public void sortRequestsTable() {
        int newTableIndex = 0;
        for (Path path : map.getTour().getListPaths().subList(1, map.getTour().getListPaths().size())) {
            long id = path.getIdDeparture();
            int tableIndex = 0;
            while (((Intersection) requestsTable.getItems().get(tableIndex)).getId() != id) {
                tableIndex++;
            }
            Intersection point = (Intersection) requestsTable.getItems().remove(tableIndex);
            requestsTable.getItems().add(newTableIndex, point);
            newTableIndex++;
        }
    }

    public void selectRequest(long tourStopId) {
        Request req = map.getRequestByTourStopId(tourStopId);
        if (!requestsTable.getSelectionModel().getSelectedItems().contains(req.getPickUpPoint())) {
            int index = requestsTable.getItems().indexOf(req.getPickUpPoint());
            requestsTable.getSelectionModel().select(index);
        }
        if (!requestsTable.getSelectionModel().getSelectedItems().contains(req.getDeliveryPoint())) {
            int index = requestsTable.getItems().indexOf(req.getDeliveryPoint());
            requestsTable.getSelectionModel().select(index);
        }
    }

    public int durationPopup() {
        TextInputDialog popup = new TextInputDialog();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
        popup.setTitle("Duration");
        popup.setHeaderText("");
        popup.setContentText("Please enter the duration:");
        Optional<String> result = popup.showAndWait();
        return Integer.valueOf(result.get());
    }

    @Override
    public void update(observer.Observable observed, Object arg) {
        createRequestList();
        if (!map.getTour().getListPaths().isEmpty()) {
            sortRequestsTable();
        }
    }
}