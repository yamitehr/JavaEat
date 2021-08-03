package Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Model.Order;
import Model.Restaurant;
import Model.State;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomerOrdersHistoryController {
	@FXML
	private TableView allOrdersTable;
	@FXML
	private TableColumn<Order, Integer> orderIdCol;
	@FXML
	private TableColumn<Order, String> dishesCol;
	@FXML
	private TableColumn<Order, Double> priceCol;
	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		
		
		priceCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<Double>(order.getValue().getDishes()
				.stream()
				.map(d -> d.calcDishPrice())
				.reduce((a,b) ->  a + b)
				.get()
				));
		
		dishesCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<String>(
				order.getValue().getDishes()
				.stream()
				.map(d -> d.toString())
				.reduce((a, b) -> a + ", " + b).get()
				));
		
		
		List<Order> orders = new ArrayList<Order>();
		orders = Restaurant.getInstance().getOrders().values().stream()
				.filter(o -> o.getCustomer().equals(State.getCurrentCustomer()))
				.collect(Collectors.toList());
		
		allOrdersTable.getItems().addAll(orders);
	}
}
