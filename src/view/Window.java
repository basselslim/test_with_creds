package view;

import controler.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Map;

import java.io.IOException;

/**
 * Window.
 *
 * @author T-REXANOME
 */
public class Window extends Application {

    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    Controller controller = new Controller();

    /**
     * @param MainFrame
     * @throws Exception
     */
    @Override
    public void start(Stage MainFrame) throws Exception {
        initUI(MainFrame);
    }

    /**
     * @param stage
     * @throws IOException
     */
    private void initUI(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        var scene = new Scene(root, screenBounds.getMaxX()-150, screenBounds.getMaxY()-150, Color.WHITE);

        stage.setTitle("DeliveryTool");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
