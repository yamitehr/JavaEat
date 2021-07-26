package Controllers;

import Model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CustMenuController {
	Customer current = MainController.getNowLogin();
	
	@FXML
	private Label welcomeLbl;
	
	@FXML
	public void initialize() {
		welcomeLbl.setText("Hello " + current.getFirstName());
	}

}
