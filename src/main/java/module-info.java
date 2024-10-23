module pl.boryskaczmarek.unitconverter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires io.github.cdimascio.dotenv.java;


    opens pl.boryskaczmarek.unitconverter.models to com.fasterxml.jackson.databind;
    opens pl.boryskaczmarek.unitconverter to javafx.fxml;
    exports pl.boryskaczmarek.unitconverter;
}