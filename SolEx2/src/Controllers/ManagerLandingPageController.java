package Controllers;

import java.io.IOException;

import Model.Restaurant;
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
	@FXML
	private Label totalCustomers;
	@FXML
	private Label totalEmployees;
	
	private Image image;
	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
//		dashboardBtn.setAlignment(Pos.BASELINE_LEFT);
//		addFoodbtn.setAlignment(Pos.BASELINE_LEFT);
//		addEmployeeBtn.setAlignment(Pos.BASELINE_LEFT);
//		addOrderAndDeliveryBtn.setAlignment(Pos.BASELINE_LEFT);
//		addCustomerBtn.setAlignment(Pos.BASELINE_LEFT);
//		statisticsBtn.setAlignment(Pos.BASELINE_LEFT);
		
		totalCustomers.setText(String.valueOf(Restaurant.getInstance().getCustomers().size()));
		totalEmployees.setText(String.valueOf(Restaurant.getInstance().getCooks().size() + 
				Restaurant.getInstance().getDeliveryPersons().size()));
		
		
		
	}

	public void MoveToAddDishScene(ActionEvent e) throws IOException {
		messageLbl.setText("Food Items");
		image = new Image("file:///C:/Users/li493/OneDrive/מסמכים/JavaEat/SolEx2/icons/utensils-solid.png");
		icon.setImage(image);
		replacePane(toReplacePane, "/View/AddDish.fxml");
	}
	
	public void MoveToAddCookScene(ActionEvent e) throws Exception {
		messageLbl.setText("Employees");
		image = new Image("file:///C:/Users/li493/OneDrive/מסמכים/JavaEat/SolEx2/icons/users-solid.png");
		icon.setImage(image);
		replacePane(toReplacePane, "/View/AddCook.fxml");
	}
	
	public void MoveToAddOrderScene(ActionEvent e) throws Exception {
		messageLbl.setText("Orders & Deliveries");
		image = new Image("file:///C:/Users/li493/OneDrive/מסמכים/JavaEat/SolEx2/icons/truck-solid.png");
		icon.setImage(image);
		replacePane(toReplacePane, "/View/AddOrder.fxml");
	}
	
	public void MoveToAddCustomerScene(ActionEvent e) throws Exception {
		messageLbl.setText("Customers");
		image = new Image("file:///C:/Users/li493/OneDrive/מסמכים/JavaEat/SolEx2/icons/users-solid.png");
		icon.setImage(image);
		replacePane(toReplacePane, "/View/AddCustomer.fxml");
	}
	
	public void moveToStatisticsScene(ActionEvent e) throws Exception {
		messageLbl.setText("Statistics");
		image = new Image("file:///C:/Users/li493/OneDrive/מסמכים/JavaEat/SolEx2/icons/chart-line-solid.png");
		icon.setImage(image);
		replacePane(toReplacePane, "/View/Manager_Statistics.fxml");
	}
	
	public void MoveToLoginScene(ActionEvent e) {
		moveToScene("/View/Login.fxml", (Stage)logOutBtn.getScene().getWindow());
	}
}
