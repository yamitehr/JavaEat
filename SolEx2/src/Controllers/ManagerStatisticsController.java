package Controllers;

import java.util.stream.Collectors;

import Model.Cook;
import Model.Customer;
import Model.Delivery;
import Model.DeliveryPerson;
import Model.Dish;
import Model.Restaurant;
import Utils.Expertise;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ManagerStatisticsController extends ControllerWrapper {
	@FXML
	private ComboBox<DeliveryPerson> deliveryPersonBox;

	@FXML
	private ComboBox<Integer> monthBox;
	
	@FXML
	private ListView<Delivery> deliveriesByPerson;
	
	@FXML
	private Label regularDeliveries;

	@FXML
	private Label expressDeliveries;
	
	@FXML
	private ComboBox<Expertise> expertiseBox;
	
	@FXML
	private ListView<Cook> cooksByExpertise;
	
	@FXML
	private ComboBox<Customer> customerBox;
	
	@FXML
	private ListView<Dish> relevantDishList;
	
	@FXML
    public void initialize() {
		init();
	}

	private void init() {
		ObservableList<DeliveryPerson> deliveryPersons = FXCollections.observableArrayList(Restaurant.getInstance().getDeliveryPersons().values());
		deliveryPersonBox.getItems().clear();				
		deliveryPersonBox.setItems(FXCollections.observableArrayList(deliveryPersons));
		
		ObservableList<Integer> monthsNumber = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12);
		monthBox.getItems().clear();				
		monthBox.setItems(FXCollections.observableArrayList(monthsNumber));
		
		regularDeliveries.setText(Restaurant.getInstance().getNumberOfDeliveriesPerType().get("Regular delivery").toString());
		expressDeliveries.setText(Restaurant.getInstance().getNumberOfDeliveriesPerType().get("Express delivery").toString());
		
		ObservableList<Expertise> expertises = FXCollections.observableArrayList(Expertise.values());
		expertiseBox.getItems().clear();				
		expertiseBox.setItems(FXCollections.observableArrayList(expertises));
		
		ObservableList<Customer> customers = FXCollections.observableArrayList(Restaurant.getInstance().getCustomers().values());
		customerBox.getItems().clear();				
		customerBox.setItems(FXCollections.observableArrayList(customers));
		
		
		
		
		
	}
	
	public void getDeliveries(ActionEvent e) {
		DeliveryPerson selectedDP = deliveryPersonBox.getValue();
		Integer selectedMonth = monthBox.getValue();
		if(selectedDP != null && selectedMonth != null) {
			deliveriesByPerson.setVisible(true);
			deliveriesByPerson.getItems().clear();
			deliveriesByPerson.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().getDeliveriesByPerson(selectedDP, selectedMonth).stream().collect(Collectors.toList())));
		}
	}
	
	public void getCooks(ActionEvent e) {
		Expertise expert = expertiseBox.getValue();
		if(expert != null) {
			cooksByExpertise.setVisible(true);
			cooksByExpertise.getItems().clear();
			cooksByExpertise.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().GetCooksByExpertise(expert).stream().collect(Collectors.toList())));
		}
	}
	
	public void getDishes(ActionEvent e) {
		Customer cust = customerBox.getValue();
		if(cust != null) {
			relevantDishList.setVisible(true);
			relevantDishList.getItems().clear();
			relevantDishList.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().getReleventDishList(cust).stream().collect(Collectors.toList())));
		}
	}

}
