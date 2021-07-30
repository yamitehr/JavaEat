package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeSet;

import Exceptions.InvalidInputException;
import Model.Component;
import Model.Customer;
import Model.Delivery;
import Model.DeliveryArea;
import Model.DeliveryPerson;
import Model.Dish;
import Model.ExpressDelivery;
import Model.Order;
import Model.RegularDelivery;
import Model.Restaurant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddOrderController extends ControllerWrapper {
	//order
	@FXML
	private ComboBox<Customer> customerBox;
	@FXML
	private ComboBox<Delivery> deliveryBox;
	@FXML
	private ListView<Dish> dishesList;
	@FXML
	private Button backBtn;
	@FXML
	private Text messageToUserOrder;
	
	//delivery
	@FXML
	private ComboBox<DeliveryPerson> DeliveryPersonBox;
	@FXML
	private ComboBox<DeliveryArea> deliveryAreaBox;
	@FXML
	private CheckBox yesChoice;
	@FXML
	private DatePicker deliveryDate;
	@FXML
	private ComboBox<Order> orderBox;
	@FXML
	private TextField postageField;
	@FXML
	private ListView<Order> ordersList;
	@FXML
	private Text messageToUserRegular;
	@FXML
	private Text messageToUserExpress;
	
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
		
		ObservableList<DeliveryPerson> deliveryPersons = FXCollections.observableArrayList(Restaurant.getInstance().getDeliveryPersons().values());
		DeliveryPersonBox.getItems().clear();				
		DeliveryPersonBox.setItems(FXCollections.observableArrayList(deliveryPersons));
		
		ObservableList<DeliveryArea> deliveryAreas = FXCollections.observableArrayList(Restaurant.getInstance().getAreas().values());
		deliveryAreaBox.getItems().clear();				
		deliveryAreaBox.setItems(FXCollections.observableArrayList(deliveryAreas));
		
		ObservableList<Order> orders = FXCollections.observableArrayList(Restaurant.getInstance().getOrders().values());
		orderBox.getItems().clear();				
		orderBox.setItems(FXCollections.observableArrayList(orders));
		
		ObservableList<Order> order = FXCollections.observableArrayList(Restaurant.getInstance().getOrders().values());
		ordersList.setItems(order);
		ordersList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
			messageToUserOrder.setFill(Color.BLUE);
			messageToUserOrder.setText("Order added successfully");
			customerBox.getSelectionModel().clearSelection();
			deliveryBox.getSelectionModel().clearSelection();
		}catch(InvalidInputException inputE) {
			messageToUserOrder.setFill(Color.RED);
			messageToUserOrder.setText(inputE.getMessage());
		}catch(Exception ex) {
			messageToUserOrder.setFill(Color.RED);
			messageToUserOrder.setText("an error has accured please try again");
		}
	}
	
	
	public void addExpressDelivery(ActionEvent e) {
		try {
			DeliveryPerson dp = DeliveryPersonBox.getValue();
			if(dp == null) {
				throw new InvalidInputException("Please select Delivery Person");
			}			
			DeliveryArea da = deliveryAreaBox.getValue();
			if(da == null) {
				throw new InvalidInputException("Please select Delivery Area");
			}		
			
			Order order = orderBox.getValue();	
			if(order == null)
				throw new InvalidInputException("Please sekect order");
			
			LocalDate dateOfDelivery;
			dateOfDelivery = deliveryDate.getValue();
			if(dateOfDelivery == null) {
				throw new InvalidInputException("Please select Date of Delivery");
			}
			double postage;
			if(postageField.getText().isEmpty()) {
				throw new InvalidInputException("Please fill postage");
			}
			else
				postage = Double.parseDouble(postageField.getText());
			boolean isDelivered;
			isDelivered = yesChoice.isSelected() ? true : false;
			
			Delivery newExpressDelivery = new ExpressDelivery(dp, da, isDelivered, order, postage, dateOfDelivery);
			///		
	
			Restaurant.getInstance().addDelivery(newExpressDelivery); 
			messageToUserExpress.setFill(Color.BLUE);
			messageToUserExpress.setText("Express Delivery added successfully");
			DeliveryPersonBox.getSelectionModel().clearSelection();
			deliveryAreaBox.getSelectionModel().clearSelection();
			orderBox.getSelectionModel().clearSelection();
			deliveryDate.setValue(null);
			yesChoice.setSelected(false);
		}catch(InvalidInputException inputE) {
			messageToUserExpress.setFill(Color.RED);
			messageToUserExpress.setText(inputE.getMessage());
		}catch(NumberFormatException ne) {
			messageToUserExpress.setFill(Color.RED);
			messageToUserExpress.setText("Wrong Input!");
		}catch(Exception ex) {
			messageToUserExpress.setFill(Color.RED);
			messageToUserExpress.setText("an error has accured please try again");
		}
	}
	
	
	public void addRegularDelivery(ActionEvent e) {
		try {
			DeliveryPerson dp = DeliveryPersonBox.getValue();
			if(dp == null) {
				throw new InvalidInputException("Please select Delivery Person");
			}			
			DeliveryArea da = deliveryAreaBox.getValue();
			if(da == null) {
				throw new InvalidInputException("Please select Delivery Area");
			}		
			
			TreeSet<Order> deliveryOrders = new TreeSet<Order>();
			deliveryOrders.addAll(ordersList.getSelectionModel().getSelectedItems());	
			if(deliveryOrders.isEmpty())
				throw new InvalidInputException("Please choose at least one order!");
			
			LocalDate dateOfDelivery;
			dateOfDelivery = deliveryDate.getValue();
			if(dateOfDelivery == null) {
				throw new InvalidInputException("Please select Date of Delivery");
			}
			boolean isDelivered;
			isDelivered = yesChoice.isSelected() ? true : false;
			
			Delivery newRegularDelivery = new RegularDelivery(deliveryOrders, dp, da, isDelivered, dateOfDelivery);
			///		
	
			Restaurant.getInstance().addDelivery(newRegularDelivery); 
			messageToUserRegular.setFill(Color.BLUE);
			messageToUserRegular.setText("Delivery added successfully");
			DeliveryPersonBox.getSelectionModel().clearSelection();
			deliveryAreaBox.getSelectionModel().clearSelection();
			deliveryDate.setValue(null);
			yesChoice.setSelected(false);
		}catch(InvalidInputException inputE) {
			messageToUserRegular.setFill(Color.RED);
			messageToUserRegular.setText(inputE.getMessage());
		}catch(Exception ex) {
			messageToUserRegular.setFill(Color.RED);
			messageToUserRegular.setText("an error has accured please try again");
		}
	}
	
	
}
	
