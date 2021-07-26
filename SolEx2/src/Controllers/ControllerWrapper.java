package Controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerWrapper {

	public void moveToScene(String fxmlName, Stage primaryStage, int width, int height) {
		width = width <= 0? Consts.defaultWidth : width;
		height = height <= 0? Consts.defaultHeight : height;
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
	        primaryStage.setScene(new Scene(root, width, height));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void moveToScene(String fxmlName, Stage primaryStage) {
		moveToScene(fxmlName, primaryStage, Consts.defaultWidth, Consts.defaultHeight);
	}
}
