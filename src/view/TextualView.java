package view;

import java.util.*;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.*;
import model.Map;

import static java.lang.String.valueOf;


public class TextualView implements observer.Observer {

    Map map;
    Pane pane;
    TextArea TextArea;
    Label TourInfos;
    TableView requestsTable;
    TableColumn<Intersection, Long> intersectionColumn;
    TableColumn<Intersection, String> durationColumn;
    TableColumn<Intersection, String> typeColumn;
    TableColumn<Intersection, Integer> requestIndexColumn;
    TableColumn<Intersection, String> arrivalTimeColumn;

    public TextualView(Map map, Pane pane, TextArea textArea,Label tourInfos) {
        this.map = map;
        this.pane = pane;
        this.TextArea = textArea;
        this.TourInfos = tourInfos;
        createRequestList();
    }

    public void createRequestList() {

        requestsTable = new TableView();
        requestsTable.setPlaceholder(new Label("No request to display"));
        requestsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //requestsTable.prefHeightProperty().bind(pane.heightProperty());
        requestsTable.prefWidthProperty().bind(pane.widthProperty());

        requestsTable.setRowFactory( tableView -> {
            final TableRow<Intersection> row = new TableRow<>();
            row.setOnMousePressed(event -> {
                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY) {
                    Intersection intersection = row.getItem();
                    Request request = null;
                    if (intersection instanceof DeliveryPoint) {
                        request = ((DeliveryPoint) intersection).getRequest();
                    } else if (intersection instanceof PickUpPoint) {
                        request = ((PickUpPoint) intersection).getRequest();
                    }
                    selectRequest(request);
                }
            });
            return row;
        });

        intersectionColumn = new TableColumn<>("Intersection Id");
        intersectionColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        intersectionColumn.setSortable(false);

        durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Intersection, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Intersection, String> p) {
                String res = "";
                int minutes = 0;
                if (p.getValue() instanceof DeliveryPoint) {
                    minutes += ((DeliveryPoint) p.getValue()).getDeliveryDuration()/60;
                } else if (p.getValue() instanceof PickUpPoint) {
                    minutes += ((PickUpPoint) p.getValue()).getPickUpDuration() / 60;
                }
                if (minutes > 1) {
                    res = minutes + " minutes";
                } else {
                    res = minutes + " minute";
                }
                return new ReadOnlyObjectWrapper(res);
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
                Request req = map.getRequestByIntersectionId(p.getValue().getId());
                int index = map.getListRequests().indexOf(req) + 1;
                return new ReadOnlyObjectWrapper(index);
            }
        });
        requestIndexColumn.setSortable(false);

        arrivalTimeColumn = new TableColumn<>("Arrival Time");
        arrivalTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Intersection, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Intersection, String> p) {
                if (map.getTour().getListPaths().isEmpty()) {
                    return new ReadOnlyObjectWrapper("Not computed yet");
                } else {
                    int index = 0;
                    for (Path path: map.getTour().getListPaths()) {
                        if (path.getIdArrival() == p.getValue().getId()) {
                            break;
                        } else {
                            index++;
                        }
                    }
                    if (index >= map.getTour().getListTimes().size()) {
                        return new ReadOnlyObjectWrapper("an error occurred");
                    }
                    int time = map.getTour().getListTimes().get(index)[1];
                    return new ReadOnlyObjectWrapper(map.getTour().timeToString(time));
                }
            }
        });
        arrivalTimeColumn.setVisible(false);
        arrivalTimeColumn.setSortable(false);

        requestsTable.getColumns().add(requestIndexColumn);
        requestsTable.getColumns().add(durationColumn);
        requestsTable.getColumns().add(typeColumn);
        requestsTable.getColumns().add(arrivalTimeColumn);

        requestsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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

    public void selectRequest(Request req) {
        requestsTable.getSelectionModel().clearSelection();
        int index = requestsTable.getItems().indexOf(req.getPickUpPoint());
        requestsTable.getSelectionModel().select(index);
        int index2 = requestsTable.getItems().indexOf(req.getDeliveryPoint());
        requestsTable.getSelectionModel().select(index2);
    }

    public int durationPopup() {
        TextInputDialog popup = new TextInputDialog();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
        popup.setTitle("Duration");
        popup.setHeaderText("");
        popup.setContentText("Please enter the duration:");
        Button okButton = (Button) popup.getDialogPane().lookupButton(ButtonType.OK);
        TextField input = popup.getEditor();
        BooleanBinding isInvalid = Bindings.createBooleanBinding(() -> durationIsInvalid(input.getText()), input.textProperty());
        okButton.disableProperty().bind(isInvalid);
        Optional<String> resultat = popup.showAndWait();
        int res = Integer.parseInt(resultat.get());
        return res;
    }

    public boolean durationIsInvalid(String str) {
        try {
            int res = Integer.parseInt(str);
            if (res <= 0) {
                return true;
            }
        } catch (NumberFormatException nfe) {
            return true;
        }
        return false;
    }

    public void setMessage(String message){
        TextArea.setText(message);
    }

    public void setTourInfo(String info){TourInfos.setText(info);}

    @Override
    public void update(observer.Observable observed, Object arg) {
        createRequestList();
        if (!map.getTour().getListPaths().isEmpty()) {
            sortRequestsTable();
            arrivalTimeColumn.setVisible(true);
        }
    }
}