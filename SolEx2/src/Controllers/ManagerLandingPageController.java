package Controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
	private Button foodItemsBtn;
	@FXML
	private Button deliveryPersonBtn;
	@FXML
	private Button deliveryAreaBtn;
	@FXML
	private Button deliveriesBtn;
	@FXML
	private Button addFoodbtn;
	@FXML
	private Button addEmployeeBtn;
	@FXML
	private Button addOrderAndDeliveryBtn;
	@FXML
	private Button addCustomerBtn;
	@FXML
	private Button statisticsBtn;
	@FXML
	private Button dashboardBtn;
	@FXML
	private AnchorPane toReplacePane;
	@FXML
	private Label messageLbl;
	
	@FXML
	private Button ordersAndDeliveriesBtn;
	@FXML
	private Button logOutBtn;
	@FXML
	private ImageView icon;
	
	private Image image;
	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		dashboardBtn.setAlignment(Pos.BASELINE_LEFT);
		addFoodbtn.setAlignment(Pos.BASELINE_LEFT);
		addEmployeeBtn.setAlignment(Pos.BASELINE_LEFT);
		addOrderAndDeliveryBtn.setAlignment(Pos.BASELINE_LEFT);
		addCustomerBtn.setAlignment(Pos.BASELINE_LEFT);
		statisticsBtn.setAlignment(Pos.BASELINE_LEFT);
		
	}
	
	public void moveToManagerCustomerScene(ActionEvent e) throws Exception {
		messageLbl.setText("All Customers");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Manager_Cooks.fxml"));
		AnchorPane pane = loader.load();
		toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
		toReplacePane.getChildren().add(pane);
	}
	
	public void moveToManagerCooksScene(ActionEvent e) throws Exception {
		messageLbl.setText("All Employees");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Manager_Cooks.fxml"));
		AnchorPane pane = loader.load();
		toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
		toReplacePane.getChildren().add(pane);
	}
	public void moveToManagerComponentsScene(ActionEvent e) {
		moveToScene("/View/Manager_Components.fxml", (Stage)componentsBtn.getScene().getWindow());
	}

	public void moveToManagerDishesScene(ActionEvent e) throws Exception {
		messageLbl.setText("All Food Items");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Manager_Dish.fxml"));
		AnchorPane pane = loader.load();
		toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
		toReplacePane.getChildren().add(pane);
	}
	
	public void moveToManagerDeliveryPersonScene(ActionEvent e) {
		moveToScene("/View/Manager_DeliveryPerson.fxml", (Stage)deliveryPersonBtn.getScene().getWindow());
	}
	
	public void moveToManagerDeliveryAreaScene(ActionEvent e) {
		moveToScene("/View/Manager_DeliveryArea.fxml", (Stage)deliveryAreaBtn.getScene().getWindow());
	}
	
	public void moveToManagerOrdersScene(ActionEvent e) throws Exception {
		messageLbl.setText("All Order & Delivery");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Manager_Order.fxml"));
		AnchorPane pane = loader.load();
		toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
		toReplacePane.getChildren().add(pane);
	}
	public void moveToManagerDeliveriesScene(ActionEvent e) {
		moveToScene("/View/Manager_Delivery.fxml", (Stage)deliveriesBtn.getScene().getWindow());
	}
	
	public void MoveToAddDishScene(ActionEvent e) throws IOException {
		messageLbl.setText("Food Items");
		image = new Image("file:///C:/Users/li493/OneDrive/מסמכים/JavaEat/SolEx2/icons/utensils-solid.png");
		icon.setImage(image);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddDish.fxml"));
		AnchorPane pane = loader.load();
		toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
		toReplacePane.getChildren().add(pane);
	}
	
	public void MoveToAddCookScene(ActionEvent e) throws Exception {
		messageLbl.setText("Employees");
		image = new Image("file:///C:/Users/li493/OneDrive/מסמכים/JavaEat/SolEx2/icons/users-solid.png");
		icon.setImage(image);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddCook.fxml"));
		AnchorPane pane = loader.load();
		toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
		toReplacePane.getChildren().add(pane);
	}
	
	public void MoveToAddOrderScene(ActionEvent e) throws Exception {
		messageLbl.setText("Orders & Deliveries");
		image = new Image("file:///C:/Users/li493/OneDrive/מסמכים/JavaEat/SolEx2/icons/truck-solid.png");
		icon.setImage(image);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddOrder.fxml"));
		AnchorPane pane = loader.load();
		toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
		toReplacePane.getChildren().add(pane);
	}
	
	public void MoveToAddCustomerScene(ActionEvent e) throws Exception {
		messageLbl.setText("Customers");
		image = new Image("file:///C:/Users/li493/OneDrive/מסמכים/JavaEat/SolEx2/icons/users-solid.png");
		icon.setImage(image);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddCustomer.fxml"));
		AnchorPane pane = loader.load();
		toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
		toReplacePane.getChildren().add(pane);
	}
	
	public void MoveToLoginScene(ActionEvent e) {
		moveToScene("/View/Login.fxml", (Stage)logOutBtn.getScene().getWindow());
	}
}
