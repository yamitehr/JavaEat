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
	
	@FXML
	private Button componentsBtn;
	
	@FXML
	private Button dishesBtn;
	@FXML
	private Button deliveryPersonBtn;
	@FXML
	private Button deliveryAreaBtn;
	
	public void moveToManagerCustomerScene(ActionEvent e) {
		moveToScene("/View/Manager_Customer.fxml", (Stage)customersBtn.getScene().getWindow());
	}
	
	public void moveToManagerCooksScene(ActionEvent e) {
		moveToScene("/View/Manager_Cooks.fxml", (Stage)cooksBtn.getScene().getWindow());
	}
	public void moveToManagerComponentsScene(ActionEvent e) {
		moveToScene("/View/Manager_Components.fxml", (Stage)componentsBtn.getScene().getWindow());
	}
	
	public void moveToManagerDishesScene(ActionEvent e) {
		moveToScene("/View/Manager_Dish.fxml", (Stage)dishesBtn.getScene().getWindow());
	}

	public void moveToManagerDeliveryPersonScene(ActionEvent e) {
		moveToScene("/View/Manager_DeliveryPerson.fxml", (Stage)deliveryPersonBtn.getScene().getWindow());
	}
	
	public void moveToManagerDeliveryAreaScene(ActionEvent e) {
		moveToScene("/View/AddDeliveryArea.fxml", (Stage)deliveryAreaBtn.getScene().getWindow());
	}
}
