package Controllers;

import java.util.ArrayList;
import Exceptions.InvalidInputException;
import Model.Component;
import Model.Customer;
import Model.Delivery;
import Model.Dish;
import Model.Order;
import Model.Restaurant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddOrderController extends ControllerWrapper {
	@FXML
	private ComboBox<Customer> customerBox;
	@FXML
	private ComboBox<Delivery> deliveryBox;
	@FXML
	private ListView<Dish> dishesList;
	@FXML
	private Button backBtn;
	@FXML
	private Text messageToUser;
	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		ObservableList<Customer> customers = FXCollections.observableArrayList(Restaurant.getInstance().getCustomers().values());
		customerBox.getItems().clear();				
		customerBox.setItems(FXCollections.observableArrayList(customers));
		
		ObservableList<Dish> dishes = FXCollections.observableArrayList(Restaurant.getInstance().getDishes().values());
		dishesList.setItems(dishes);
		dishesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	public void moveToManagerOrderScene(ActionEvent e) {
		moveToScene("/View/Manager_Order.fxml", (Stage)backBtn.getScene().getWindow());
	}
	
	public void addOrder(ActionEvent e) {
		try {
			Customer customer = customerBox.getValue();
			if(customer == null) {
				throw new InvalidInputException("Please select Customer");
			}
			if(Restaurant.getInstance().getBlackList().contains(customer)) {
				throw new InvalidInputException("This customer is in the black list!!");
			}
			
			Delivery delivery = deliveryBox.getValue();
		
			ArrayList<Dish> orderDishes = new ArrayList<Dish>();
			orderDishes.addAll(dishesList.getSelectionModel().getSelectedItems());	
			if(orderDishes.isEmpty())
				throw new InvalidInputException("Please choose at least one dish!");
			for(Dish d: orderDishes) {
				for(Component c: d.getComponenets()) {
					if(customer.isSensitiveToGluten() && c.isHasGluten()) {
						throw new InvalidInputException(customer + " is sensitive to one of the components");
					}
					else if(customer.isSensitiveToLactose() && c.isHasLactose()) {
						throw new InvalidInputException(customer + " is sensitive to one of the components");
					}
				}
			}
			
			Order newOrder = new Order(customer, orderDishes, delivery);
			///		
	
			Restaurant.getInstance().addOrder(newOrder); 
			messageToUser.setFill(Color.BLUE);
			messageToUser.setText("Order added successfully");
			customerBox.getSelectionModel().clearSelection();
			deliveryBox.getSelectionModel().clearSelection();
		}catch(InvalidInputException inputE) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText(inputE.getMessage());
		}catch(Exception ex) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText("an error has accured please try again");
		}
	}
	
}
	
