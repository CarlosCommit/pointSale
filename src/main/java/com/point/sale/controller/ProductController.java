package com.point.sale.controller;

import com.point.sale.entity.Producto;
import com.point.sale.entity.ResponseQuery;
import com.point.sale.service.ProductoService;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

@Component
public class ProductController implements Initializable {

    private final ProductoService productoService;
    private final ConfigurableApplicationContext context;


    public ProductController(ProductoService productoService, ConfigurableApplicationContext context) {
        this.productoService = productoService;
        this.context = context;
    }

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    @FXML
    private TextField nombre;
    @FXML
    private TextField precio;
    @FXML
    private TextField codebar;
    @FXML
    private TextField precioMayorista;

    @FXML
    private HBox msgUsuario;

    @FXML
    private Label descUsuario;


    @FXML
    private Button guardar;
    private boolean ocultoCampos;

    @FXML
    private ImageView imgMsgUsuario;
    private Producto producto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> codebar.requestFocus());
        ocultoCampos = false;
        producto=null;
        nombre.setOnKeyPressed(this::handleKeyPressed);
        precio.setOnKeyPressed(this::handleKeyPressed);
        codebar.setOnKeyPressed(this::handleKeyPressed);
        precioMayorista.setOnKeyPressed(this::handleKeyPressed);
        toggleCampos();
    }

    private void toggleVisibility() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1800), msgUsuario);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        msgUsuario.setVisible(true);
        fadeTransition.setOnFinished(event -> {
            FadeTransition fadeInTransition = new FadeTransition(Duration.millis(3000), msgUsuario);
            fadeInTransition.setFromValue(1.0);
            fadeInTransition.setToValue(0.0);
            fadeInTransition.play();
        });
        fadeTransition.play();

    }
    public void handleFirsEnterCodeBar() {
        if (codebar.getText() != null || !codebar.getText().isEmpty() && !ocultoCampos) {

            ResponseQuery responseQuery = productoService.searchProductoByCodeBar(codebar.getText());
            this.guardar.setText("GUARDAR");
            if (responseQuery.getResult() != null) {

                this.producto = (Producto) responseQuery.getResult();
                cargarDatos();
                guardar.setText("EDITAR");

            }

            this.ocultoCampos = true;
            toggleCampos();
            precio.requestFocus();
        }

    }


    public void toggleCampos() {
        this.nombre.setVisible(ocultoCampos);
        this.precio.setVisible(ocultoCampos);
        this.precioMayorista.setVisible(ocultoCampos);
    }

    private void handleKeyPressed(KeyEvent event) {
        TextField currentTextField = (TextField) event.getSource();
        switch (event.getCode()) {
            case UP:
                moveFocusToPreviousTextField(currentTextField);
                event.consume();
                break;
            case DOWN, ENTER:
                moveFocusToNextTextField(currentTextField);
                event.consume();
                break;
            default:
                break;
        }
    }

    // Método para mover el foco al TextField anterior
    private void moveFocusToPreviousTextField(TextField currentTextField) {
        switch (currentTextField.getId()) {
            case "precio":
                nombre.requestFocus();
                break;
            case "nombre":
                codebar.requestFocus();
                break;
            case "precioMayorista":
                precio.requestFocus();
                break;
            default:
                break;
        }
    }

    private void moveFocusToNextTextField(TextField currentTextField) {
        if(!this.ocultoCampos)
        {
            if (currentTextField.getId().equals("codebar")) {
                this.guardar.requestFocus();
            }
            return;
        }

        switch (currentTextField.getId()) {
            case "nombre":
                precio.requestFocus();
                break;
            case "precio":
                precioMayorista.requestFocus();
                break;
            case "codebar":
                nombre.requestFocus();
                break;
            case "precioMayorista":
                guardar.requestFocus();
                break;
            default:
                break;
        }
    }



    public void cargarDatos() {
        this.nombre.setText(producto.getProduct());
        this.codebar.setText(producto.getBar());
        this.precio.setText(String.valueOf(producto.getPrice()));
        this.precioMayorista.setText(String.valueOf(producto.getPriceMayorista()));
    }

    public void guardarProducto() {
        if (isValid()) {
            try {

                Producto productoEntity = new Producto();
                long id = (producto!=null) ? producto.getId() : 0;
                boolean esnuevo = producto == null;
                productoEntity.setId(id);
                productoEntity.setProduct(nombre.getText());
                productoEntity.setBar(codebar.getText());
                productoEntity.setPrice(Double.parseDouble(precio.getText()));
                productoEntity.setPriceMayorista(Double.parseDouble(precioMayorista.getText()));
                productoEntity.setDateLastModification(new Date());
                ResponseQuery responseQuery = productoService.saveProducto(productoEntity,esnuevo);

                File file;

                if (responseQuery.getStatus() == 0) {

                    log.info("Guardado Correctamente");
                    this.descUsuario.setText("El producto se guardo correctamente");
                    file = new File("src/main/resources/img/icon-success.png");
                    this.ocultoCampos = false;
                    this.toggleCampos();
                    reiniciar();

                }else {
                    this.descUsuario.setText("Ocurrio un problema, verifica los valores y no cambies el codigo de barra una vez ingresado");
                    file = new File("src/main/resources/img/icon-error.png");
                }

               Image image = new Image(file.toURI().toString());

                toggleVisibility();
                imgMsgUsuario.setImage(image);

            } catch (Exception e) {
                this.descUsuario.setText("Revisa los valores, no se realizo el guardado");
                File file = new File("src/main/resources/img/icon-error.png");
                Image image = new Image(file.toURI().toString());
                imgMsgUsuario.setImage(image);
                toggleVisibility();

            }
        }else {
            this.descUsuario.setText("Completa todos los campos");
            File file = new File("src/main/resources/img/icon-error.png");
            Image image = new Image(file.toURI().toString());
            imgMsgUsuario.setImage(image);
            toggleVisibility();
        }
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
    private void reiniciar(){
        codebar.requestFocus();
        this.producto=null;
        this.codebar.clear();
        this.precio.clear();
        this.precioMayorista.clear();
        this.nombre.clear();
    }
    private boolean isValid() {

        return nombre != null && !nombre.getText().isEmpty()
                && precio != null && !precio.getText().isEmpty()
                && precioMayorista != null && !precioMayorista.getText().isEmpty()
                && codebar != null && !codebar.getText().isEmpty();

    }

}
