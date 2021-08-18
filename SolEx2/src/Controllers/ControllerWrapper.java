package Controllers;

import java.io.File;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
			primaryStage.setScene(new Scene(root, width, height));
	        primaryStage.centerOnScreen();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void replacePane(AnchorPane toReplacePane, String path) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			AnchorPane pane = loader.load();
			toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
			toReplacePane.getChildren().add(pane);
		}catch (IOException e1) {
				e1.printStackTrace();
			}
	}
	
	public void moveToScene(String fxmlName, Stage primaryStage) {
		moveToScene(fxmlName, primaryStage, Consts.defaultWidth, Consts.defaultHeight);
	}
	
	public void soundOfButton(String path) {
		String musicFile = path;  
		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}
}
