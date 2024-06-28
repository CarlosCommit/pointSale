package com.point.sale.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class PanelProductoController implements Initializable {
    private final ConfigurableApplicationContext context;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button facturacionButton;

    public PanelProductoController(ConfigurableApplicationContext context) {
        this.context = context;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        borderPane.requestFocus();
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
    public void cambiarDesactualizadoPanel(ActionEvent event) throws IOException {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();

        double ancho = stageActual.getScene().getWidth();
        double alto = stageActual.getScene().getHeight();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/panel/Desactualizados.fxml"));
        loader.setControllerFactory(context::getBean);
        Scene scene = new Scene(loader.load(), ancho, alto); // Establecer las dimensiones de la nueva escena

        stageActual.setScene(scene);
    }

    @FXML
    public void cambiarFormulario(ActionEvent event) throws IOException {
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();

        double ancho = stageActual.getScene().getWidth();
        double alto = stageActual.getScene().getHeight();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/panel/Formulario.fxml"));
        loader.setControllerFactory(context::getBean);
        Scene scene = new Scene(loader.load(), ancho, alto); // Establecer las dimensiones de la nueva escena

        stageActual.setScene(scene);
    }

}
