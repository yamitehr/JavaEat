package Controllers;

import Model.Customer;
import Model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

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
	public void initialize() {
		messageLbl.setText("Hello " + current.getFirstName());
	}

	public void moveToDashboardScene(ActionEvent e) {
		
	}
	public void moveToOrdersScene(ActionEvent e) {
		
	}
	public void moveToShoppingCartScene(ActionEvent e) {
		
	}
	public void moveToMenuScene(ActionEvent e) {
		messageLbl.setText("Menu");
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
