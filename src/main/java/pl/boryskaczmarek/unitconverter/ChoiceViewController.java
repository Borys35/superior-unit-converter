package pl.boryskaczmarek.unitconverter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import pl.boryskaczmarek.unitconverter.models.Conversion;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletionException;

public class ChoiceViewController implements Initializable {
    @FXML
    private ComboBox<String> measureComboBox;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    protected void onNextButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("conversion-view.fxml"));

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Error in loading fxml");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ArrayList<String> measurements = getMeasurements();
            measureComboBox.getItems().addAll(measurements);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<String> readArrayListValue(String body) {
        try {
            return objectMapper.readValue(body, new TypeReference<ArrayList<String>>() {});
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    private ArrayList<String> getMeasurements() throws Exception {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        String apiHost = dotenv.get("API_HOST");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://measurement-unit-converter.p.rapidapi.com/measurements"))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", apiHost)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        ArrayList<String> measurements = HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::readArrayListValue)
                .get();

        return measurements;
    }
}
