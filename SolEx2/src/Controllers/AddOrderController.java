package Controllers;

import java.util.ArrayList;

import Exceptions.InvalidInputException;
import Model.Component;
import Model.Customer;
import Model.Delivery;
import Model.Dish;
import Model.Order;
import Model.Restaurant;
import Utils.DishType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.paint.Color;
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
	
}
