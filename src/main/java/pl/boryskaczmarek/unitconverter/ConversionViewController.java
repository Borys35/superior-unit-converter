package pl.boryskaczmarek.unitconverter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pl.boryskaczmarek.unitconverter.models.Conversion;

import java.io.IOException;
import java.net.http.*;
import java.net.*;
import java.util.concurrent.CompletionException;

import io.github.cdimascio.dotenv.Dotenv;
import pl.boryskaczmarek.unitconverter.utils.SceneRootSwitcher;

// When button click is triggered, API call is sent.
// Here is a playground to try out the API.
// https://rapidapi.com/foxello-foxello-default/api/measurement-unit-converter/playground/endpoint_1f9d51b9-3230-4468-85df-2c736f5c48f3
public class ConversionViewController {
    @FXML
    private Label welcomeText;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    protected void onHelloButtonClick() {
        try {
            Conversion conversion = getConversionAsync();

            System.out.println("CONVERSION RESULT: " + conversion.getResult());
            welcomeText.setText("Result is " + conversion.getResult());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            welcomeText.setText("ERROR!");
        }
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) throws IOException {
        SceneRootSwitcher.switchSceneFromEvent(event, "choice-view.fxml");
    }

    private Conversion readConversionValue(String body) {
        try {
            return objectMapper.readValue(body, new TypeReference<Conversion>() {});
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    private Conversion getConversionAsync() throws Exception {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        String apiHost = dotenv.get("API_HOST");

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://measurement-unit-converter.p.rapidapi.com/length?value=1200&from=m&to=km"))
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