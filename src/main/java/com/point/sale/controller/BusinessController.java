package com.point.sale.controller;

import com.point.sale.entity.Producto;
import com.point.sale.entity.ResponseQuery;
import com.point.sale.model.ProductView;
import com.point.sale.service.ProductoService;
import com.point.sale.service.VentaService;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class BusinessController implements Initializable {

    private final ProductoService productoService;
    private List<ProductView> productos = new ArrayList<>();
    private final VentaService ventaService;
    private final ConfigurableApplicationContext context;
    @FXML
    private HBox msgUsuario;

    @FXML
    private Label descUsuario;

    @FXML
    private ImageView imgMsgUsuario;


    public BusinessController(ProductoService productoService, VentaService ventaService, ConfigurableApplicationContext contex) {
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.context = contex;
    }

    @FXML
    private TextField code;
    @FXML
    private TableView<ProductView> tableProducts;

    @FXML
    private Label productCurrently;
    @FXML
    private Label priceCurrently;
    @FXML
    private Label total;
    @FXML
    private Label fechaUltModif;
    @FXML
    private Button productosButton;

    @FXML
    private RadioButton busquedaCodigoBarra;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enfocarInputCodeBar();
        detectarEnter();
        code.setOnKeyPressed(this::handleKeyPressed);
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            borrarDatos();
        } else if (event.getCode() == KeyCode.F1) {

            registrarVentaFunct();

        } else if (event.getCode() == KeyCode.F2) {
            productosButton.fire();

        }
    }

    public void registrarVentaFunct() {
        if (productos.isEmpty()) {

            this.descUsuario.setText("Agregue productos");
            File file = new File("src/main/resources/img/icon-error.png");
            Image image = new Image(file.toURI().toString());
            imgMsgUsuario.setImage(image);
            toggleVisibility();

        } else {
            ResponseQuery responseQuery = ventaService.registrarVenta(productos);
            if (responseQuery.getStatus() == 0) {
                borrarDatos();
                this.descUsuario.setText("Facturado correctamente");
                File file = new File("src/main/resources/img/icon-success.png");
                Image image = new Image(file.toURI().toString());
                imgMsgUsuario.setImage(image);
                toggleVisibility();
            } else {
                this.descUsuario.setText("Ocurrio un error: " + responseQuery.getMessage());
                File file = new File("src/main/resources/img/icon-error.png");
                Image image = new Image(file.toURI().toString());
                imgMsgUsuario.setImage(image);
                toggleVisibility();
            }
        }
    }


    @FXML
    public void cambiarProductosStage(ActionEvent event) throws IOException {
        borrarDatos();
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();


        double ancho = stageActual.getScene().getWidth();
        double alto = stageActual.getScene().getHeight();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/panel/Productos.fxml"));
        loader.setControllerFactory(context::getBean);
        Scene scene = new Scene(loader.load(), ancho, alto);

        stageActual.setScene(scene);

    }

    private void enfocarInputCodeBar() {
        Platform.runLater(() -> code.requestFocus());
        code.focusedProperty().addListener((observable, oldValue, newValue) -> {
            code.requestFocus();
        });


    }

    private void detectarEnter() {
        code.setOnAction(this::buscarProductoByCodeBar);
    }

    private void buscarProductoByCodeBar(ActionEvent actionEvent) {

        if (code.getText() == null || code.getText().isEmpty())
            return;
        String codeBar = code.getText();
        code.clear();
        ResponseQuery responseQuery = productoService.searchProductoByCodeBar(codeBar);

        if (responseQuery.getResult() != null) {
            ProductView productoExistente = existProduct((Producto) responseQuery.getResult());
            Producto prod = (Producto) responseQuery.getResult();
            ProductView productView = new ProductView(prod.getProduct(), 1, prod.getPrice(), prod.getPrice());
            productView.setFechaUltModif(prod.getDateLastModification());
            productView.setId(prod.getId());
            if (productoExistente == null) {
                productos.add(productView);
                recargarTabla();
            } else {
                productoExistente.setPreciofinal(productoExistente.getPreciofinal() + productoExistente.getPrecioUnitario());
                productoExistente.setCantidad(productoExistente.getCantidad() + 1);
                recargarTabla();
            }
            rellenarDatos(productView);


        } else {
            this.priceCurrently.setText("$ 00.0");
            this.productCurrently.setText("------------");
            this.fechaUltModif.setText("-------");
            this.descUsuario.setText("Producto no encontrado");
            File file = new File("src/main/resources/img/icon-error.png");
            Image image = new Image(file.toURI().toString());
            imgMsgUsuario.setImage(image);
            toggleVisibility();
        }

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

    private void recargarTabla() {
        ObservableList listaActual = tableProducts.getItems();
        if (!listaActual.isEmpty()) listaActual.clear();
        ObservableList<ProductView> productViews = FXCollections.observableArrayList(productos);
        tableProducts.setItems(productViews);
        calcularTotal();

    }

    private ProductView existProduct(Producto producto) {
        for (ProductView productView : productos) {
            if (productView.getId() == producto.getId())
                return productView;
        }
        return null;
    }


    public void rellenarDatos(ProductView productView) {
        this.priceCurrently.setText("$ " + productView.getPrecioUnitario());
        this.productCurrently.setText(productView.getProducto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.fechaUltModif.setText("Ultima Modificacion: " + sdf.format(productView.getFechaUltModif()));


    }

    public void calcularTotal() {
        double valueTotal = productos.stream().mapToDouble(element -> element.getPrecioUnitario() * element.getCantidad()).sum();
        this.total.setText("TOTAL: $" + valueTotal);
    }

    public void borrarDatos() {
        this.priceCurrently.setText("$ 00.0");
        this.productCurrently.setText("------------");
        this.fechaUltModif.setText("-------");
        this.total.setText("00.0");
        ObservableList<ProductView> listaActual = tableProducts.getItems();
        listaActual.clear();
        productos = new ArrayList<>();
    }


}
