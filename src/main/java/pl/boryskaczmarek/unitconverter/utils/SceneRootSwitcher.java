package pl.boryskaczmarek.unitconverter.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneRootSwitcher {
    public static void switchSceneFromEvent(ActionEvent event, String sceneRootName) throws IOException {
        String path = "/pl/boryskaczmarek/unitconverter/" + sceneRootName;
        FXMLLoader fxmlLoader = new FXMLLoader(SceneRootSwitcher.class.getResource(path));

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(fxmlLoader.load());
    }
}
