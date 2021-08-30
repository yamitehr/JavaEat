package Controllers;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ManagerLandingPageController extends ControllerWrapper {
	@FXML
	private Button customersBtn;
	@FXML
	private Button allEmployeesBtn;
	
	@FXML
	private Button componentsBtn;
	
	@FXML
	private AnchorPane toReplacePane;
	@FXML
	private Label messageLbl;
	@FXML
	private Button logOutBtn;
	@FXML
	private ImageView icon;
	@FXML
	private Label totalCustomers;
	@FXML
	private Label totalEmployees;
	@FXML
	private Label totalSell;
	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		
		replacePane(toReplacePane, "/View/Manager_Dashboard.fxml"); 
				
	}

	public void MoveToAddDishScene(ActionEvent e) {
		soundOfButton("button.mp3");
		replacePane(toReplacePane, "/View/AddDish.fxml");
	}
	
	public void MoveToAddCookScene(ActionEvent e) {
		soundOfButton("button.mp3");
		replacePane(toReplacePane, "/View/AddCook.fxml");
	}
	
	public void MoveToAddOrderScene(ActionEvent e) {
		soundOfButton("button.mp3");
		replacePane(toReplacePane, "/View/AddOrder.fxml");
	}
	
	public void MoveToAddCustomerScene(ActionEvent e) {
		soundOfButton("button.mp3");
		replacePane(toReplacePane, "/View/AddCustomer.fxml");
	}
	
	public void moveToStatisticsScene(ActionEvent e) {
		soundOfButton("button.mp3");
		replacePane(toReplacePane, "/View/Manager_Statistics.fxml");
	}
	
	public void moveToDashboardScene(ActionEvent e) {
		soundOfButton("button.mp3");
		replacePane(toReplacePane, "/View/Manager_Dashboard.fxml");
	}
	
	public void MoveToLoginScene(ActionEvent e) {
		soundOfButton("button.mp3");
		moveToScene("/View/Login.fxml", (Stage)logOutBtn.getScene().getWindow(), 700, 550);
	}
	
	public void moveToAIMachineScene(ActionEvent e) {
		soundOfButton("button.mp3");
		replacePane(toReplacePane, "/View/Manager_AIMachine.fxml");
	}
}
