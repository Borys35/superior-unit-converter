package pl.boryskaczmarek.unitconverter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pl.boryskaczmarek.unitconverter.models.Conversion;

import java.io.IOException;
import java.net.http.*;
import java.net.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CompletionException;

import io.github.cdimascio.dotenv.Dotenv;
import pl.boryskaczmarek.unitconverter.utils.SceneRootSwitcher;

// When button click is triggered, API call is sent.
// Here is a playground to try out the API.
// https://rapidapi.com/foxello-foxello-default/api/measurement-unit-converter/playground/endpoint_1f9d51b9-3230-4468-85df-2c736f5c48f3
public class ConversionViewController implements Initializable {
    @FXML
    private ComboBox<String> fromComboBox;
    @FXML
    private TextField fromTextField;
    @FXML
    private ComboBox<String> toComboBox;
    @FXML
    private Text toText;
    @FXML
    private Button convertButton;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static String measureUnitGroup = "length";

    private String fromUnitName;
    private String toUnitName;
    private String fromValue;

    public static void setMeasureUnitGroup(String unitGroup) {
        measureUnitGroup = unitGroup;
    }

    @FXML
    protected void onConvertButtonClick() {
        try {
            new Thread(() -> {
                Platform.runLater(() -> convertButton.setDisable(true));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> convertButton.setDisable(false));
            }).start();

            Conversion conversion = getConversionAsync(measureUnitGroup, fromValue, fromUnitName, toUnitName);
            toText.setText(conversion.getResult());
        } catch (Exception e) {
            toText.setText("ERR!");
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) throws IOException {
        SceneRootSwitcher.switchSceneFromEvent(event, "choice-view.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            fromComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                fromUnitName = newValue;
            });

            toComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                toUnitName = newValue;
            });

            ArrayList<String> unitNames = getUnitNames();
            fromComboBox.getItems().addAll(unitNames);
            fromComboBox.getSelectionModel().select(0);
            toComboBox.getItems().addAll(unitNames);
            toComboBox.getSelectionModel().select(1);

            fromTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                String v = newValue.trim();
                if (!newValue.matches("\\d*")) {
                    v = v.replaceAll("\\D", "");
                    fromTextField.setText(v);
                }
                fromValue = v;
            });
            fromTextField.setText("100");
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    private ArrayList<String> readArrayListValue(String body) {
        try {
            return objectMapper.readValue(body, new TypeReference<ArrayList<String>>() {});
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    private ArrayList<String> getUnitNames() throws Exception {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        String apiHost = dotenv.get("API_HOST");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://measurement-unit-converter.p.rapidapi.com/" + measureUnitGroup + "/units"))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", apiHost)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        ArrayList<String> unitNames = HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::readArrayListValue)
                .get();

        return unitNames;
    }

    private Conversion readConversionValue(String body) {
        try {
            return objectMapper.readValue(body, new TypeReference<Conversion>() {});
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    private Conversion getConversionAsync(String unitGroup, String value, String from, String to) throws Exception {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        String apiHost = dotenv.get("API_HOST");

        String urlBlueprint = "https://measurement-unit-converter.p.rapidapi.com/%s?value=%s&from=%s&to=%s";
        String url = String.format(urlBlueprint, unitGroup, value, from, to);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", apiHost)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        var conversion = HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::readConversionValue)
                .get();

        return conversion;
    }
}