package view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import controler.Controller;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.PopupWindow;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.*;
import model.Map;

import static java.lang.String.valueOf;


public class TextualView implements observer.Observer {

    Map map;
    Controller controller;
    Pane pane;
    TextArea TextArea;
    Label TourInfos;
    TableView requestsTable;
    TableColumn<Step, String> durationColumn;
    TableColumn<Step, String> typeColumn;
    TableColumn<Step, Integer> requestIndexColumn;
    TableColumn<Step, String> arrivalTimeColumn;
    TableColumn<Step, String> crossroadColumn;

    public TextualView(Map map, Pane pane, TextArea textArea,Label tourInfos, Controller controller) {
        this.controller = controller;
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
        requestsTable.prefHeightProperty().bind(pane.heightProperty());
        requestsTable.prefWidthProperty().bind(pane.widthProperty());

        requestsTable.setRowFactory( tableView -> {
            final TableRow<Step> row = new TableRow<>();
            row.setOnMousePressed(event -> {
                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY) {
                    Step step = row.getItem();
                    Request request = step.getRequest();
                    selectRequest(request,true);
                }
            });
            return row;
        });

        durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Step, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Step, String> p) {
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
        //durationColumn.setResizable(false);
        //durationColumn.setMaxWidth(100);

        typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Step, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Step, String> p) {
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
        //typeColumn.setResizable(false);
        //typeColumn.setMaxWidth(100);

        requestIndexColumn = new TableColumn<>("");
        requestIndexColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Step, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Step, Integer> p) {
                Request req = p.getValue().getRequest();
                int index = map.getListRequests().indexOf(req) + 1;
                return new ReadOnlyObjectWrapper(index);
            }
        });
        requestIndexColumn.setCellFactory(new Callback<TableColumn<Step, Integer>,
                TableCell<Step, Integer>>()
        {
            @Override
            public TableCell<Step, Integer> call(
                    TableColumn<Step, Integer> param) {
                return new TableCell<Step, Integer>() {
                    @Override
                    protected void updateItem(Integer i, boolean empty) {
                        if (!empty) {
                            setText(Integer.toString(i));
                            Step step = (Step)requestsTable.getItems().get(indexProperty().getValue());
                            Random generator = new Random(step.getRequest().getPickUpPoint().getId());
                            int rand = generator.nextInt(150);
                            int rand2 = generator.nextInt(150);
                            int rand3 = generator.nextInt(150);
                            Color color = Color.rgb(rand + 100,(rand2 + 100),(rand3 + 100));
                            setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                };
            }
        });
        requestIndexColumn.setSortable(false);
        //requestIndexColumn.setResizable(false);
        //requestIndexColumn.setMaxWidth(20);

        arrivalTimeColumn = new TableColumn<>("Arrival Time");
        arrivalTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Step, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Step, String> p) {
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
        //arrivalTimeColumn.setResizable(false);

        crossroadColumn = new TableColumn<>("Crossroad");
        crossroadColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Step, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Step, String> p) {
                long id = p.getValue().getId();
                List<Segment> segments = map.getListIntersections().get(id).getListSegments();
                String res = "";
                int i = 0;
                while (i < segments.size()) {
                    Segment segment = segments.get(i);
                    if (i == 0) {
                        if (segment.getStreetName() != "" && segment.getStreetName() != null) {
                            res += segment.getStreetName();
                            res += " - ";
                        }
                    } else {
                        if (segment.getStreetName() != "" && segment.getStreetName() != null && !segment.getStreetName().equals(segments.get(i-1).getStreetName())) {
                            res += segment.getStreetName();
                            res += " - ";
                        }
                    }
                    i++;
                }
                res = res.substring(0, res.length()-3);
                return new ReadOnlyObjectWrapper(res);
            }
        });
        crossroadColumn.setCellFactory(tc -> {
            TableCell<Step, String> cell = new TableCell<>();
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            cell.textProperty().bind(cell.itemProperty());
            cell.setOnMouseEntered(event -> {
                if (cell.getText() != null && cell.getText() != "") {
                    Tooltip tooltip = new Tooltip(cell.getText());
                    cell.setTooltip(tooltip);
                }
            });
            return cell ;
        });
        crossroadColumn.setSortable(false);
        //crossroadColumn.setResizable(true);
        //crossroadColumn.setPrefWidth(260);

        requestsTable.getColumns().add(requestIndexColumn);
        requestsTable.getColumns().add(durationColumn);
        requestsTable.getColumns().add(typeColumn);
        requestsTable.getColumns().add(arrivalTimeColumn);
        requestsTable.getColumns().add(crossroadColumn);

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

    public void selectRequest(Request req, Boolean local) {
        requestsTable.getSelectionModel().clearSelection();
        int index = requestsTable.getItems().indexOf(req.getPickUpPoint());
        requestsTable.getSelectionModel().select(index);
        int index2 = requestsTable.getItems().indexOf(req.getDeliveryPoint());
        requestsTable.getSelectionModel().select(index2);
        if(local) {
            controller.leftClick(req.getDeliveryPoint().getId());
        }
    }

    public int durationPopup() {
        TextInputDialog popup = new TextInputDialog("0");
        popup.initStyle(StageStyle.UNDECORATED);
        popup.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
        popup.setTitle("Duration");
        popup.setHeaderText("");
        popup.setContentText("Please enter the duration (in seconds):");
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
            if (res < 0) {
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