package gameoflife.application;

import gameoflife.controllers.MainWindowController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        // Load fxml file
        FXMLLoader fxml = new FXMLLoader();
        Parent root = fxml.load(getClass().getResource("../../GameOfLife.fxml").openStream());
        MainWindowController mwc = (MainWindowController)fxml.getController();
        Scene scene = new Scene(root, 600, 600);

        // Setting keyboard listener
        scene.setOnKeyPressed(mwc);
        stage.widthProperty().addListener(new ChangeListener<Number>() {

                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    mwc.setCanvasWidth((double) newValue);
                }

            });
        stage.heightProperty().addListener(new ChangeListener<Number>() {

                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    mwc.setCanvasHeight((double)newValue);
                }

            });

        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
