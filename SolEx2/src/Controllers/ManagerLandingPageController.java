package Controllers;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
	@FXML
	private Label totalSell;
	
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
		
//		totalCustomers.setText(String.valueOf(Restaurant.getInstance().getCustomers().size()));
//		totalEmployees.setText(String.valueOf(Restaurant.getInstance().getCooks().size() + 
//				Restaurant.getInstance().getDeliveryPersons().size()));
//		double totalPrice = 0.0;
//		for(Order o: Restaurant.getInstance().getOrders().values()) {
//			for(Dish d: o.getDishes()) {
//				totalPrice += d.calcDishPrice();
//			}
//		}
//		totalSell.setText(String.valueOf(totalPrice));
		
		replacePane(toReplacePane, "/View/Manager_Dashboard.fxml"); 
				
	}

	public void MoveToAddDishScene(ActionEvent e) {
		soundOfButton("button2.mp3");
		replacePane(toReplacePane, "/View/AddDish.fxml");
	}
	
	public void MoveToAddCookScene(ActionEvent e) {
		soundOfButton("button2.mp3");
		replacePane(toReplacePane, "/View/AddCook.fxml");
	}
	
	public void MoveToAddOrderScene(ActionEvent e) {
		soundOfButton("button2.mp3");
		replacePane(toReplacePane, "/View/AddOrder.fxml");
	}
	
	public void MoveToAddCustomerScene(ActionEvent e) {
		soundOfButton("button2.mp3");
		replacePane(toReplacePane, "/View/AddCustomer.fxml");
	}
	
	public void moveToStatisticsScene(ActionEvent e) {
		soundOfButton("button2.mp3");
		replacePane(toReplacePane, "/View/Manager_Statistics.fxml");
	}
	
	public void moveToDashboardScene(ActionEvent e) {
		soundOfButton("button2.mp3");
		replacePane(toReplacePane, "/View/Manager_Dashboard.fxml");
	}
	
	public void MoveToLoginScene(ActionEvent e) {
		soundOfButton("button2.mp3");
		moveToScene("/View/Login.fxml", (Stage)logOutBtn.getScene().getWindow(), 700, 550);
	}
	
	public void moveToAIMachineScene(ActionEvent e) {
		soundOfButton("button2.mp3");
		replacePane(toReplacePane, "/View/Manager_AIMachine.fxml");
	}
}
