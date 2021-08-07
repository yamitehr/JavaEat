package Controllers;

import Model.Component;
import Model.Dish;
import Model.Order;
import Model.Restaurant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class ManagerDashboardController extends ControllerWrapper {
	
	@FXML
	private Label totalCustomers;
	@FXML
	private Label totalEmployees;
	@FXML
	private Label totalSell;
	@FXML
	private Label totalRevenue;
	@FXML
	private Label revenueFromExpress;
	@FXML
	private Label profitRelation;
	@FXML
	private Button watchPopularBtn;
	@FXML
	private ListView<Component> popularComponentsList;
	
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
		
		double revenue = 0;
		for(Order o: Restaurant.getInstance().getOrders().values()) {
			revenue += o.calcOrderRevenue();
		}
		totalRevenue.setText(String.valueOf(revenue));
		
		revenueFromExpress.setText(String.valueOf(Restaurant.getInstance().revenueFromExpressDeliveries()));
		
		profitRelation.setText(Restaurant.getInstance().getProfitRelation().stream()
				.map(d -> d.toString())
				.reduce((a, b) -> a + ", " + b).get());
	}
	
	public void watchPopularComponent(ActionEvent e) {
		popularComponentsList.setVisible(true);
		ObservableList<Component> popularComps = FXCollections.observableArrayList(Restaurant.getInstance().getPopularComponents());
		popularComponentsList.setItems(popularComps);
	}

}
