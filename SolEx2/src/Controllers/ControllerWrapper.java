package Controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControllerWrapper {

	protected void replacePaneWithFxml(String fxmlPath, Pane toReplace) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		AnchorPane pane = loader.load();
		toReplace.getChildren().removeAll(toReplace.getChildren());
		toReplace.getChildren().add(pane);
	}
	
	public void moveToScene(String fxmlName, Stage primaryStage, int width, int height) {
		width = width <= 0? Consts.defaultWidth : width;
		height = height <= 0? Consts.defaultHeight : height;
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
	        //primaryStage.setScene(new Scene(root, width, height));
			primaryStage.setScene(new Scene(root, width, height));
	        primaryStage.centerOnScreen();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void moveToScene(String fxmlName, Stage primaryStage) {
		moveToScene(fxmlName, primaryStage, Consts.defaultWidth, Consts.defaultHeight);
	}
}
