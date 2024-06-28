package com.point.sale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PointSaleApplication extends Application {
	public ConfigurableApplicationContext context;

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		context = SpringApplication.run(PointSaleApplication.class);
		FXMLLoader fmxl = new FXMLLoader(getClass().getResource("/sale/Sale.fxml"));
		fmxl.setControllerFactory(context::getBean);
		Scene scene = new Scene(fmxl.load());
		primaryStage.setTitle("SuperPOS");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
