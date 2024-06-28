package com.point.sale.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class DesactualizadoController implements Initializable {
    private final ConfigurableApplicationContext context;

    public DesactualizadoController(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    public void cambiarFacturacion(ActionEvent event) throws IOException {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();

        double ancho = stageActual.getScene().getWidth();
        double alto = stageActual.getScene().getHeight();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sale/Sale.fxml"));
        loader.setControllerFactory(context::getBean);
        Scene scene = new Scene(loader.load(), ancho, alto); // Establecer las dimensiones de la nueva escena

        stageActual.setScene(scene);
    }
    @FXML
    public void cambiarProductos(ActionEvent event) throws IOException {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();

        double ancho = stageActual.getScene().getWidth();
        double alto = stageActual.getScene().getHeight();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/panel/Productos.fxml"));
        loader.setControllerFactory(context::getBean);
        Scene scene = new Scene(loader.load(), ancho, alto); // Establecer las dimensiones de la nueva escena

        stageActual.setScene(scene);
    }
}
