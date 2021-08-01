package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
	@FXML
	private ListView<Order> allOrders;
	@FXML
	private Button addOrderBtn;
	@FXML
	private Text customerField;
	@FXML
	private Text dishesField;
	@FXML
	private Text deliveryField;
	@FXML
	private Button editOrderBtn;
	
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
	private Label postageLbl;
	
	@FXML
	private AnchorPane regularPane;
	@FXML
	private Button addExpressBtn;
	@FXML
	private Button addRegularBtn;
	
	@FXML
	private ListView<Delivery> allDeliveries;
	@FXML
	private Button addRegularDeliveryBtn;
	@FXML
	private Button addExpressDeliveryBtn;
	@FXML
	private Text deliveryPersonField;
	@FXML
	private Text deliveryAreaField;
	@FXML
	private Text isDeliveredField;
	@FXML
	private Text dateField;
	@FXML
	private Text ordersField;
	@FXML 
	private Text orderTitle;
	@FXML
	private Text postageTitle;
	@FXML
	private Text postageText;
	
	@FXML
	private AnchorPane toReplacePane;
	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		editOrderBtn.setDisable(true);
		
		orderBox.setVisible(false);
		ordersList.setVisible(false);
		postageField.setVisible(false);
		postageLbl.setVisible(false);
		regularPane.setVisible(false);
		addRegularBtn.setVisible(false);
		addExpressBtn.setVisible(false);
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
		
////////All cooks list view
		//Set the listview cell factory to show the right cook name
		allOrders.setCellFactory(param -> new ListCell<Order>() {
		    @Override
		    protected void updateItem(Order item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null) {
		            setText(null);
		        } else {
		            setText(item.getId() + " " + item.getCustomer());
		        }
		    }
		});
		
		//Add all orders
			allOrders.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().getOrders().entrySet().stream().map(o -> o.getValue()).collect(Collectors.toList())));
				
			//Event listener for listview
			allOrders.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {
				   @Override
				   public void changed(ObservableValue<? extends Order> observable, Order oldValue, Order newValue) {
				    updateOrderDetailsFields();
				   }
			});
			
			
	////////All cooks list view
			//Set the listview cell factory to show the right cook name
			allDeliveries.setCellFactory(param -> new ListCell<Delivery>() {
			    @Override
			    protected void updateItem(Delivery item, boolean empty) {
			        super.updateItem(item, empty);

			        if (empty || item == null) {
			            setText(null);
			        } else {
			            setText(item.getClass().getSimpleName() + " " + item.getId());
			        }
			    }
			});
			
			//Add all orders
			allDeliveries.getItems().addAll(FXCollections.observableArrayList(
						Restaurant.getInstance().getDeliveries().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
					
				//Event listener for listview
			allDeliveries.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Delivery>() {
					   @Override
					   public void changed(ObservableValue<? extends Delivery> observable, Delivery oldValue, Delivery newValue) {
					    updateDeliveryDetailsFields();
					   }
				});
	}
	
	public void moveToManagerOrderScene(ActionEvent e) {
		moveToScene("/View/Manager_Order.fxml", (Stage)backBtn.getScene().getWindow());
	}
	
	public void updateOrderDetailsFields() {
		Order selectedOrder = allOrders.getSelectionModel().getSelectedItem();
		// fill text fields with values about the selected order on the list
		if(selectedOrder != null) {
			customerField.setText(selectedOrder.getCustomer().toString());
			dishesField.setText(selectedOrder.getDishes().toString());
			if(selectedOrder.getDelivery() != null)
				deliveryField.setText(selectedOrder.getDelivery().toString());
			
		//clean the text fields if there is no selection
		} else if(selectedOrder == null) {
			customerField.setText("");
			dishesField.setText("");
			deliveryField.setText("");
		}
	}
	
	
	public void editOrder(ActionEvent e) {
		Order selectedOrder = allOrders.getSelectionModel().getSelectedItem();
		if(selectedOrder !=  null) {
			addOrderBtn.setDisable(true);
			editOrderBtn.setDisable(false);
			customerBox.setValue(selectedOrder.getCustomer());
			customerBox.setEditable(false);
			if(selectedOrder.getDelivery() != null)
				deliveryBox.setValue(selectedOrder.getDelivery());
			for(int i = 0; i < selectedOrder.getDishes().size(); i++) {
				dishesList.getSelectionModel().select(selectedOrder.getDishes().get(i));
			}
			customerBox.setDisable(true);
		}
	}
	
	public void setEditOrder(ActionEvent e) {
		Order selectedOrder = allOrders.getSelectionModel().getSelectedItem();
		try {
			if(!selectedOrder.getCustomer().equals(customerBox.getValue())) {
				if(customerBox.getValue() == null)
					throw new InvalidInputException("You must choose Customer");
				selectedOrder.setCustomer(customerBox.getValue());
		}
			if(selectedOrder.getDelivery() != null) {
				if(!selectedOrder.getDelivery().equals(deliveryBox.getValue())) {
					if(deliveryBox.getValue() == null)
						throw new InvalidInputException("you must choose Delivery");
					selectedOrder.setDelivery(deliveryBox.getValue());
				}
		}
		ArrayList<Dish> changedDishes = new ArrayList<Dish>();
		changedDishes.addAll(dishesList.getSelectionModel().getSelectedItems());
		if(changedDishes.isEmpty()) 
			throw new InvalidInputException("You must choose at least one dish");
		for(Dish d: changedDishes) {
			for(Component c: d.getComponenets()) {
				if(selectedOrder.getCustomer().isSensitiveToGluten() && c.isHasGluten()) {
					throw new InvalidInputException(selectedOrder.getCustomer() + " is sensitive to " + c);
				}
				else if(selectedOrder.getCustomer().isSensitiveToLactose() && c.isHasLactose()) {
					throw new InvalidInputException(selectedOrder.getCustomer() + " is sensitive to " + c);
				}
			}
		}
		for(int i = 0; i < selectedOrder.getDishes().size(); i++) {
			if(!changedDishes.contains(selectedOrder.getDishes().get(i)))
				selectedOrder.removeDish(selectedOrder.getDishes().get(i));			
		}
		for(Dish d: changedDishes) {
			if(!selectedOrder.getDishes().contains(d))
				selectedOrder.addDish(d);
		}
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Order edited successfully!");
		alert.showAndWait();
		messageToUserOrder.setText("");
		customerBox.getSelectionModel().clearSelection();
		deliveryBox.getSelectionModel().clearSelection();
		dishesList.getSelectionModel().clearSelection();
		allOrders.getItems().clear();
		allOrders.getItems().addAll(FXCollections.observableArrayList(
		Restaurant.getInstance().getOrders().entrySet().stream().map(o -> o.getValue()).collect(Collectors.toList())));
		editOrderBtn.setDisable(true);
		addOrderBtn.setDisable(false);
		}catch(InvalidInputException inputE) {
			messageToUserOrder.setFill(Color.RED);
			messageToUserOrder.setText(inputE.getMessage());
		}
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
						throw new InvalidInputException(customer + " is sensitive to " + c);
					}
					else if(customer.isSensitiveToLactose() && c.isHasLactose()) {
						throw new InvalidInputException(customer + " is sensitive to " + c);
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
			//update the list
			allOrders.getItems().clear();
			allOrders.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getOrders().entrySet().stream().map(o -> o.getValue()).collect(Collectors.toList())));
		}catch(InvalidInputException inputE) {
			messageToUserOrder.setFill(Color.RED);
			messageToUserOrder.setText(inputE.getMessage());
		}catch(Exception ex) {
			messageToUserOrder.setFill(Color.RED);
			messageToUserOrder.setText("an error has accured please try again");
		}
	}
	
	public void removeOrder(ActionEvent e) {
		Order selectedOrder = allOrders.getSelectionModel().getSelectedItem();
		if(selectedOrder !=  null) {
			Restaurant.getInstance().removeOrder(selectedOrder);
			//update the list after removal
			allOrders.getItems().clear();
			allOrders.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getOrders().entrySet().stream().map(o -> o.getValue()).collect(Collectors.toList())));
		}
	}
	
	public void updateDeliveryDetailsFields() {
		Delivery selectedDelivery = allDeliveries.getSelectionModel().getSelectedItem();
		// fill text fields with values about the selected order on the list
		if(selectedDelivery != null) {
			deliveryPersonField.setText(selectedDelivery.getDeliveryPerson().toString());
			deliveryAreaField.setText(selectedDelivery.getArea().toString());
			String isDelivered;
			if(selectedDelivery.isDelivered())
				isDelivered = "No";
			else
				isDelivered = "Yes";
			isDeliveredField.setText(isDelivered);
			dateField.setText(selectedDelivery.getDeliveredDate().toString());
			if(selectedDelivery instanceof RegularDelivery) {
				orderTitle.setText("Orders:");
				ordersField.setText(((RegularDelivery) selectedDelivery).getOrders().toString());
			}
			else { //Express Delivery
				orderTitle.setText("Order:");
				ordersField.setText(((ExpressDelivery) selectedDelivery).getOrder().toString());
				postageTitle.setText("Postage:");
				postageField.setText(String.valueOf(((ExpressDelivery) selectedDelivery).getPostage()));
				
			}
			
		//clean the text fields if there is no selection
		} else if(selectedDelivery == null) {
			deliveryPersonField.setText("");
			deliveryAreaField.setText("");
			isDeliveredField.setText("");
			dateField.setText("");
			ordersField.setText("");
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
				throw new InvalidInputException("Please select order");
			
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
			//update the list
			allDeliveries.getItems().clear();
			allDeliveries.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getDeliveries().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
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
			//update the list
			allDeliveries.getItems().clear();
			allDeliveries.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getDeliveries().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
		}catch(InvalidInputException inputE) {
			messageToUserRegular.setFill(Color.RED);
			messageToUserRegular.setText(inputE.getMessage());
		}catch(Exception ex) {
			messageToUserRegular.setFill(Color.RED);
			messageToUserRegular.setText("an error has accured please try again");
		}
	}
	
	public void removeDelivery(ActionEvent e) {
		Delivery selectedDelivery = allDeliveries.getSelectionModel().getSelectedItem();
		if(selectedDelivery !=  null) {
			Restaurant.getInstance().removeDelivery(selectedDelivery);
			//update the list after removal
			allDeliveries.getItems().clear();
			allDeliveries.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getDeliveries().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
		}
	}
	
	public void regularDeliveryChoice(ActionEvent e) {
		orderBox.setVisible(false);
		postageLbl.setVisible(false);
		postageField.setVisible(false);
		addExpressBtn.setVisible(false);
		addRegularBtn.setVisible(true);
		regularPane.setVisible(true);
		ordersList.setVisible(true);
	}
	public void expressDeliveryChoice(ActionEvent e) {
		addRegularBtn.setVisible(false);
		ordersList.setVisible(false);
		addExpressBtn.setVisible(true);
		regularPane.setVisible(true);
		orderBox.setVisible(true);
		postageLbl.setVisible(true);
		postageField.setVisible(true);
	}
}
	
