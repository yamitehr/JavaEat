package Controllers;

import java.io.File;

import Model.Customer;
import Model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class CustomerLandingPageController extends ControllerWrapper{
	Customer current = State.getCurrentCustomer();
	
	@FXML
	private Label messageLbl;
	
	@FXML
	private Button dashboardBtn;
	
	@FXML
	private Button ordersBtn;
	
	@FXML
	private Button shoppingCartBtn;
	
	@FXML
	private Button menuBtn;
	
	@FXML
	private Button addOrderBtn;
	
	@FXML
	private Button personalDetailsBtn;
	
	@FXML
	private AnchorPane toReplacePane;
	@FXML
	private MediaView restaurantVideo;
	
	private File file;
	private MediaPlayer mediaPlayer;
	private Media media;
	
	
	
	@FXML
	public void initialize() {
		messageLbl.setText("Hello " + current.getFirstName());
		
		dashboardBtn.setAlignment(Pos.BASELINE_LEFT);
		ordersBtn.setAlignment(Pos.BASELINE_LEFT);
		shoppingCartBtn.setAlignment(Pos.BASELINE_LEFT);
		menuBtn.setAlignment(Pos.BASELINE_LEFT);
		addOrderBtn.setAlignment(Pos.BASELINE_LEFT);
		personalDetailsBtn.setAlignment(Pos.BASELINE_LEFT);
		
		file = new File("restaurantVideoHD.mp4");
		media = new Media(file.toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		restaurantVideo.setMediaPlayer(mediaPlayer);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		
	}

	public void moveToDashboardScene(ActionEvent e) {
		
	}
	public void moveToOrdersScene(ActionEvent e) {
		
	}
	public void moveToShoppingCartScene(ActionEvent e) {
		
	}
	public void moveToMenuScene(ActionEvent e) {
		messageLbl.setText("Menu");
		mediaPlayer.pause();
		try {
			replacePaneWithFxml("/View/Customer_Menu.fxml", toReplacePane);
		}catch(Exception er) 
		{
			
		}
	}
	public void moveToAddOrderScene(ActionEvent e) {
		
	}
	public void moveToPersonalDetailsScene(ActionEvent e) {
		
	}
}
