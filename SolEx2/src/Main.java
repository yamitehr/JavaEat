import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Controllers.Consts;
import Model.Restaurant;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	private final String restSerPath = "Rest.ser";
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		readRestaurant();
		
		Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
		Scene scene = new Scene(root, Consts.defaultWidth, Consts.defaultHeight);
		scene.getStylesheets().add(getClass().getResource("/View/decoration.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaEat");
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}
	
	@Override
	public void stop() {
		saveRestaurant();
	}
	
	private void readRestaurant() {
		try {
	         FileInputStream fileIn = new FileInputStream(restSerPath);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         Restaurant rest = (Restaurant)in.readObject();
	         in.close();
	         fileIn.close();
	      } catch (IOException i) {
	         i.printStackTrace();
	         return;
	      } catch (ClassNotFoundException c) {
	         System.out.println("Restaurant class not found");
	         c.printStackTrace();
	         return;
	      }
	}
	
	private void saveRestaurant() {

		try {
			//Create rest file if it doesn't exist
			File restFile = new File(restSerPath);
			restFile.createNewFile(); 
			
			//Serialize restaurant to file
			FileOutputStream fileOut = new FileOutputStream(restFile, false);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Restaurant.getInstance());
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + restSerPath);
		 } catch (IOException i) {
			i.printStackTrace();
		}	
	}
}
