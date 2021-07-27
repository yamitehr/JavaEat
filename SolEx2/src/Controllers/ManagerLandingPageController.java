package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ManagerLandingPageController extends ControllerWrapper {
	@FXML
	private Button customersBtn;
	@FXML
	private Button cooksBtn;
	
	public void moveToManagerCustomerScene(ActionEvent e) {
		moveToScene("/View/Manager_Customer.fxml", (Stage)customersBtn.getScene().getWindow());
	}
	
	public void moveToManagerCooksScene(ActionEvent e) {
		moveToScene("/View/Manager_Cooks.fxml", (Stage)cooksBtn.getScene().getWindow());
	}
}
