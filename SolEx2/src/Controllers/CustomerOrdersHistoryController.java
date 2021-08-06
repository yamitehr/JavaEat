package Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Model.DeliveryArea;
import Model.Order;
import Model.Restaurant;
import Model.State;
import Utils.DeliveryManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
	private TableColumn<Order, String> deliveryPersonCol;
	@FXML
	private TableColumn<Order, String> deliveryStatusCol;
	@FXML
	private TableColumn<Order, Integer> etaCol;
	@FXML
	private Button refreshBtn;
	
	@FXML
    public void initialize() {
		init();
		refreshBtn.setOnAction(e -> initData());
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
		
		deliveryPersonCol.setCellValueFactory(orderProperty ->{
			if (orderProperty.getValue().getDelivery() != null) {
				return new ReadOnlyObjectWrapper<String>(orderProperty.getValue()
						.getDelivery().getDeliveryPerson().getFirstName() 
						+ " " + orderProperty.getValue()
						.getDelivery().getDeliveryPerson().getLastName());
			} else {
				return new ReadOnlyObjectWrapper<String>("order in progress");
				}
		});
		
		deliveryStatusCol.setCellValueFactory(orderProperty -> {
			if (orderProperty.getValue().getDelivery() != null) {
	            boolean isDelivered = orderProperty.getValue().getDelivery().isDelivered();
	            String isDeliveredAsString = "";
	            if(isDelivered == true)
	            {
	            	isDeliveredAsString = "Delivered";
	            } else
	            {
	            	isDeliveredAsString += "On the way to you";
	            }
	
	            return new ReadOnlyStringWrapper(isDeliveredAsString);
	        
			} else {
				return new ReadOnlyObjectWrapper<String>("N/A");
			}
		});
		
		etaCol.setCellValueFactory(orderProperty ->
		{
			if (orderProperty.getValue().getDelivery() != null) {
				return new ReadOnlyObjectWrapper<Integer>(orderProperty.getValue()
						.orderWaitingTime(orderProperty.getValue().getDelivery().getArea()));
			} else {
				DeliveryArea da = DeliveryManager.getInstance().getDeliveryAreaForOrder(orderProperty.getValue());
				if (da != null) {
					return new ReadOnlyObjectWrapper<Integer>(orderProperty.getValue().orderWaitingTime(da));
				} else {
					return new ReadOnlyObjectWrapper<Integer>(0);					
				}
			}
		});
		
		initData();
		
	}
	
	private void initData() {
		List<Order> orders = new ArrayList<Order>();
		orders = Restaurant.getInstance().getOrders().values().stream()
				.filter(o -> o.getCustomer().equals(State.getCurrentCustomer()))
				.collect(Collectors.toList());
		
		allOrdersTable.getItems().clear();
		allOrdersTable.getItems().addAll(orders);
	}
}
