package Controllers;

import Model.Dish;
import Model.Order;
import Model.Restaurant;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ManagerDashboardController extends ControllerWrapper {
	
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
		totalCustomers.setText(String.valueOf(Restaurant.getInstance().getCustomers().size()));
		totalEmployees.setText(String.valueOf(Restaurant.getInstance().getCooks().size() + 
				Restaurant.getInstance().getDeliveryPersons().size()));
		double totalPrice = 0.0;
		for(Order o: Restaurant.getInstance().getOrders().values()) {
			for(Dish d: o.getDishes()) {
				totalPrice += d.calcDishPrice();
			}
		}
		totalSell.setText(String.valueOf(totalPrice));
	}

}
