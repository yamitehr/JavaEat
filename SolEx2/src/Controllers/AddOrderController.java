package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import Exceptions.InvalidInputException;
import Exceptions.InvalidInputException;
import Model.Component;
import Model.Customer;
import Model.Delivery;
import Model.DeliveryArea;
import Model.DeliveryPerson;
import Model.Dish;
import Model.ExpressDelivery;
import Model.Order;
import Model.OrderStatus;
import Model.RegularDelivery;
import Model.Restaurant;
import Utils.Neighberhood;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class AddOrderController extends ControllerWrapper {
	//order
	private ObservableList<Dish> finalDishes = FXCollections.observableArrayList();
	@FXML
	private ComboBox<Customer> customerBox;
	@FXML
	private ComboBox<Delivery> deliveryBox;
	@FXML
	private ListView<Dish> dishesList;
	@FXML
	private TextField quantityField;
	@FXML
	private ListView<Dish> finalDishesList;
	@FXML
	private Text messageToUserOrder;
	@FXML
	private TableView<Order> allOrdersTable;
	@FXML
	private TableColumn<Order, Integer> orderIdCol;
	@FXML
	private TableColumn<Order, String> customerCol;
	@FXML
	private TableColumn<Order, String> dishesCol;
	@FXML
	private TableColumn<Order, OrderStatus> statusCol;
	@FXML
	private Button addOrderBtn;
	@FXML
	private Button editOrderBtn;
	@FXML
	private TextField searchOrderField;
	
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
	private Label postageLbl;
	@FXML
	private ListView<Order> ordersList;
	@FXML
	private Text messageToUserRegular;
	@FXML
	private Text messageToUserExpress;	
	@FXML
	private AnchorPane regularPane;
	@FXML
	private Button addExpressBtn;
	@FXML
	private Button addRegularBtn;
	
	@FXML
	private TableView<Delivery> allDeliveriesTable;
	@FXML
	private TableColumn<Delivery, Integer> deliveryIdCol;
	@FXML
	private TableColumn<Delivery, String> deliveryTypeCol;
	@FXML
	private TableColumn<Delivery, String> deliveryPersonCol;
	@FXML
	private TableColumn<Delivery, String> areaCol;
	@FXML
	private TableColumn<Delivery, String> deliveryDateCol;
	@FXML
	private TableColumn<Delivery, String> isDeliveredCol;
	@FXML
	private TableColumn<Delivery, String> ordersCol;
	@FXML
	private TableColumn<Delivery, Double> postageCol;
	@FXML
	private Button addRegularDeliveryBtn;
	@FXML
	private Button addExpressDeliveryBtn;
	@FXML
	private Button editDeliveryBtn;
	@FXML
	private TextField searchDeliveryField;
	@FXML
	private Text postageMessage;
	
	//delivery area
	@FXML
	private Button editDeliveryAreaBtn;
	@FXML
	private Pane neighberhood_pane;
	@FXML
	private TextField deliveryAreaName;
	@FXML
	private TextField deliveryTime;	
	
	private ArrayList<Pair<CheckBox, Neighberhood>> neighberhoodList;
	
	@FXML
	private Text messageToUser;
	@FXML
	private Button addDeliveryAreaBtn;
	@FXML
	private Button removeDeliveryAreaBtn;
	@FXML
	private TableView<DeliveryArea> allAreasTable;
	@FXML
	private TableColumn<DeliveryArea, Integer> areaIdCol;
	@FXML
	private TableColumn<DeliveryArea, String> areaNameCol;
	@FXML
	private TableColumn<DeliveryArea, String> neighborhoodsCol;
	@FXML
	private TableColumn<DeliveryArea, Integer> deliveryTimeCol;
	@FXML
	private ComboBox<DeliveryArea> newAreaBox;
	@FXML
	private Label removeLbl;
	@FXML
	private Label editDelPersonsLbl;
	@FXML
	private Label editDeliveriesLbl;
	@FXML
	private ListView<DeliveryPerson> deliveryPersonsList;
	@FXML
	private ListView<Delivery> deliveriesToAddList;
	@FXML
	private TextField searchAreaField;	
	@FXML
	private Text timeMessage;
	
	@FXML
    public void initialize() {
		initOrderTab();
		initDeliveryTab();
		initAreaTab();
		generateNeighborhoodGrid();
		addDeliveryTimeEventListener();
    }

	private void initOrderTab() {
		editOrderBtn.setDisable(true);
		ObservableList<Customer> customers = FXCollections.observableArrayList(Restaurant.getInstance().getCustomers().values());
		customerBox.getItems().clear();				
		customerBox.setItems(FXCollections.observableArrayList(customers));
		
		ObservableList<Dish> dishes = FXCollections.observableArrayList(Restaurant.getInstance().getDishes().values());
		dishesList.setItems(dishes);
		dishesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		//Add all orders
		orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
	
		customerCol.setCellValueFactory(o -> new ReadOnlyObjectWrapper<String>(o.getValue().getCustomer().toString()));
			
		dishesCol.setCellValueFactory(o -> new ReadOnlyObjectWrapper<String>(
				o.getValue().getDishes()
				.stream()
				.map(d -> d.toString())
				.reduce((a, b) -> a + ", " + b).get()
				));
	
		statusCol.setCellValueFactory(o -> new ReadOnlyObjectWrapper<OrderStatus>(o.getValue().getStatus()));
			
			
		List<Order> allOrders = new ArrayList<Order>();
		allOrders = Restaurant.getInstance().getOrders().values().stream()
				.collect(Collectors.toList());
			
		allOrdersTable.getItems().addAll(allOrders);
	
		searchOrderField.textProperty().addListener((observable, oldValue, newValue) -> {
			searchOrderByID();
		});
		
	}
	
	private void initDeliveryTab() {
		orderBox.setVisible(false);
		ordersList.setVisible(false);
		postageField.setVisible(false);
		postageLbl.setVisible(false);
		regularPane.setVisible(false);
		addRegularBtn.setVisible(false);
		addExpressBtn.setVisible(false);
		
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
		
			
		//Add all deliveries
		List<Delivery> allDeliveries = new ArrayList<Delivery>();
		allDeliveries = Restaurant.getInstance().getDeliveries().values().stream()
				.collect(Collectors.toList());

		deliveryIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		
		deliveryTypeCol.setCellValueFactory(delivery -> new ReadOnlyObjectWrapper<String>(delivery.getValue().getClass().getSimpleName()));
		
		deliveryPersonCol.setCellValueFactory(delivery -> new ReadOnlyObjectWrapper<String>(delivery.getValue().getDeliveryPerson().toString()));
		
		areaCol.setCellValueFactory(area -> new ReadOnlyObjectWrapper<String>(area.getValue().getArea().toString()));
		
		deliveryDateCol.setCellValueFactory(delivery -> new ReadOnlyObjectWrapper<String>(delivery.getValue().getDeliveredDate().toString()));
		
		isDeliveredCol.setCellValueFactory(delivery -> {
            boolean isDelivered = delivery.getValue().isDelivered();
            String isDeliveredAsString;
            if(isDelivered == true)
            {
            	isDeliveredAsString = "Yes";
            }
            else
            {
            	isDeliveredAsString = "No";
            }

         return new ReadOnlyStringWrapper(isDeliveredAsString);
        });
		
		for(Delivery del: allDeliveries) {
			if(del instanceof RegularDelivery) {
				postageCol.setCellValueFactory(delivery -> new ReadOnlyObjectWrapper<Double>(0.0));
				ordersCol.setCellValueFactory(delivery -> new ReadOnlyObjectWrapper<String>(
					((RegularDelivery) delivery.getValue()).getOrders()
					.stream()
					.map(d -> d.toString())
					.reduce((a, b) -> a + ", " + b).get()
				));
			}
			if(del instanceof ExpressDelivery) {
				postageCol.setCellValueFactory(delivery -> new ReadOnlyObjectWrapper<Double>(((ExpressDelivery) delivery.getValue()).getPostage()));
				ordersCol.setCellValueFactory(delivery -> new ReadOnlyObjectWrapper<String>(((ExpressDelivery) delivery.getValue()).getOrder().toString()));
			}
		}
				
		allDeliveriesTable.getItems().addAll(allDeliveries);
		
		searchDeliveryField.textProperty().addListener((observable, oldValue, newValue) -> {
			 searchDeliveryByID();
		});
		
		postageMessage.setFill(Color.RED);
		postageField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				    	if (newValue != "") {
					    	try {
					    		postageMessage.setText("");
								Double.parseDouble(newValue);
							} catch(NumberFormatException nfe) {
								postageField.setText(oldValue);
								postageMessage.setText("Numbers only!");
							}	
				    	}
				    }
				});
		
	}

	private void initAreaTab() {
		newAreaBox.setVisible(false);
		removeLbl.setVisible(false);
		
		//for delivery area edit
		ObservableList<DeliveryPerson> deliveryPersons = FXCollections.observableArrayList(Restaurant.getInstance().getDeliveryPersons().values());
		deliveryPersonsList.setItems(deliveryPersons);
		deliveryPersonsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		ObservableList<Delivery> deliveries = FXCollections.observableArrayList(Restaurant.getInstance().getDeliveries().values());
		deliveriesToAddList.setItems(deliveries);
		deliveriesToAddList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

		private void generateNeighborhoodGrid() {
			neighberhoodList = new ArrayList<Pair<CheckBox, Neighberhood>>();
			GridPane grid = new GridPane();
			grid.setBorder(new Border(new BorderStroke(Color.BLACK, 
			           BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			grid.setHgap(12);
			grid.setVgap(12);
				
				
			for (Neighberhood n : Neighberhood.values()) {
				CheckBox cb = new CheckBox(n.name());
				cb.getStylesheets().add(getClass().getResource("/View/checkBox.css").toExternalForm());
				neighberhoodList.add(new Pair<CheckBox, Neighberhood>(cb, n));
			}
				
			int i = 0;
			int j = 0;
			for (Pair<CheckBox, Neighberhood> cb : neighberhoodList) {
				grid.add(cb.getKey(), j, i, 1, 1);
				if (j == 3) {
					j = 0;
					i ++;
				} else {
					j ++;
				}
				
			}
			
				
			neighberhood_pane.getChildren().add(grid);
		}
			
		public void addDeliveryTimeEventListener() {
			timeMessage.setFill(Color.RED);
			deliveryTime.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				    	if (newValue != "") {
					    	try {
					    		timeMessage.setText("");
								Integer.parseInt(newValue);
							} catch(NumberFormatException nfe) {
								deliveryTime.setText(oldValue);
								timeMessage.setText("Numbers only!");
							}	
				    	}
				    }
				});
				
			//Add all delivery areas
			areaIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
			
			areaNameCol.setCellValueFactory(area -> new ReadOnlyObjectWrapper<String>(area.getValue().getAreaName()));
					
			neighborhoodsCol.setCellValueFactory(area -> new ReadOnlyObjectWrapper<String>(
					area.getValue().getNeighberhoods()
					.stream()
					.map(d -> d.toString())
					.reduce((a, b) -> a + ", " + b).get()
					));
			
			deliveryTimeCol.setCellValueFactory(area -> new ReadOnlyObjectWrapper<Integer>(area.getValue().getDeliverTime()));
					
					
			List<DeliveryArea> allAres = new ArrayList<DeliveryArea>();
			allAres = Restaurant.getInstance().getAreas().values().stream()
					.collect(Collectors.toList());
					
			allAreasTable.getItems().addAll(allAres);
			
			searchAreaField.textProperty().addListener((observable, oldValue, newValue) -> {
				 searchAreaByID();
				});
						
			}
	
	private void searchOrderByID() {
		String keyword = searchOrderField.getText();
		ObservableList<Order> filteredData = FXCollections.observableArrayList();
		  if (keyword.isEmpty()) {
			  filteredData.addAll(Restaurant.getInstance().getOrders().values());
			  allOrdersTable.setItems(filteredData);
		  }
		  else {
			  Order order = Restaurant.getInstance().getRealOrder(Integer.parseInt(searchOrderField.getText()));
			  if(order != null)
				  filteredData.add(order);
		     allOrdersTable.setItems(filteredData);
		  }
			
	}
				
	private void searchDeliveryByID() {
		String keyword = searchDeliveryField.getText();
		ObservableList<Delivery> filteredData = FXCollections.observableArrayList();
		  if (keyword.isEmpty()) {
			  filteredData.addAll(Restaurant.getInstance().getDeliveries().values());
			  allDeliveriesTable.setItems(filteredData);
		  }
		  else {
			  Delivery delivery = Restaurant.getInstance().getRealDelivery(Integer.parseInt(searchDeliveryField.getText()));
			  if(delivery != null)
				  filteredData.add(delivery);
		     allDeliveriesTable.setItems(filteredData);
		  }
			
	}
	private void searchAreaByID() {
		String keyword = searchAreaField.getText();
		ObservableList<DeliveryArea> filteredData = FXCollections.observableArrayList();
		  if (keyword.isEmpty()) {
			  filteredData.addAll(Restaurant.getInstance().getAreas().values());
			  allAreasTable.setItems(filteredData);
		  }
		  else {
			  DeliveryArea da = Restaurant.getInstance().getRealDeliveryArea(Integer.parseInt(searchAreaField.getText()));
			  if(da != null)
				  filteredData.add(da);
		     allAreasTable.setItems(filteredData);
		  }
			
		}

	public void editOrder(ActionEvent e) {
		Order selectedOrder = allOrdersTable.getSelectionModel().getSelectedItem();
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
		Order selectedOrder = allOrdersTable.getSelectionModel().getSelectedItem();
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
		allOrdersTable.getItems().clear();
		allOrdersTable.getItems().addAll(FXCollections.observableArrayList(
		Restaurant.getInstance().getOrders().entrySet().stream().map(o -> o.getValue()).collect(Collectors.toList())));
		editOrderBtn.setDisable(true);
		addOrderBtn.setDisable(false);
		}catch(InvalidInputException inputE) {
			messageToUserOrder.setFill(Color.RED);
			messageToUserOrder.setText(inputE.getMessage());
		}
	}
	
	public void addDishesToOrder(ActionEvent event) {
		int quantity = Integer.parseInt(quantityField.getText());
		for(int i = 0; i < quantity; i++) {
			finalDishes.add(dishesList.getSelectionModel().getSelectedItem());
		}
		finalDishesList.setItems(finalDishes);
		
		ObservableList<Dish> dishes = FXCollections.observableArrayList(Restaurant.getInstance().getDishes().values());
		dishes.remove(dishesList.getSelectionModel().getSelectedItem());
		dishesList.setItems(dishes);
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
			orderDishes.addAll(finalDishesList.getItems());	
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
			allOrdersTable.getItems().clear();
			allOrdersTable.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getOrders().entrySet().stream().map(o -> o.getValue()).collect(Collectors.toList())));
			ordersList.getItems().clear();
			ordersList.getItems().addAll(FXCollections.observableArrayList(
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
		Order selectedOrder = allOrdersTable.getSelectionModel().getSelectedItem();
		if(selectedOrder !=  null) {
			Restaurant.getInstance().removeOrder(selectedOrder);
			//update the list after removal
			allOrdersTable.getItems().clear();
			allOrdersTable.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getOrders().entrySet().stream().map(o -> o.getValue()).collect(Collectors.toList())));
		}
	}
	
	//delivery
	
	
	public void editDelivery(ActionEvent e) {
		Delivery selectedDelivery = allDeliveriesTable.getSelectionModel().getSelectedItem();
		if(selectedDelivery !=  null) {
			addExpressBtn.setDisable(true);
			addRegularBtn.setDisable(true);
			editDeliveryBtn.setDisable(false);
			DeliveryPersonBox.setValue(selectedDelivery.getDeliveryPerson());
			deliveryAreaBox.setValue(selectedDelivery.getArea());
			yesChoice.setSelected(selectedDelivery.isDelivered());
			deliveryDate.setValue(selectedDelivery.getDeliveredDate());
			deliveryDate.setDisable(true);
			if(selectedDelivery instanceof RegularDelivery) {
				orderBox.setVisible(false);
				postageLbl.setVisible(false);
				postageField.setVisible(false);
				regularPane.setVisible(true);
				ordersList.setVisible(true);
				for(Order o: ((RegularDelivery) selectedDelivery).getOrders()) {
					ordersList.getSelectionModel().select(o);
				}
			}
			else { //express
				ordersList.setVisible(false);
				regularPane.setVisible(true);
				orderBox.setVisible(true);
				postageLbl.setVisible(true);
				postageField.setVisible(true);
				orderBox.setValue(((ExpressDelivery) selectedDelivery).getOrder());
				postageField.setText(String.valueOf(((ExpressDelivery) selectedDelivery).getPostage()));
			}
		}
	}
	public void setEditDelivery(ActionEvent e) {
		Delivery selectedDelivery = allDeliveriesTable.getSelectionModel().getSelectedItem();
		try {
			if(!selectedDelivery.getDeliveryPerson().equals(DeliveryPersonBox.getValue())) {
				if(DeliveryPersonBox.getValue() == null)
					throw new InvalidInputException("You must choose Delivery Person");
				selectedDelivery.setDeliveryPerson(DeliveryPersonBox.getValue());
			}
			if(!selectedDelivery.getArea().equals(deliveryAreaBox.getValue())) {
				if(deliveryAreaBox.getValue() == null)
					throw new InvalidInputException("You must choose Area");
				selectedDelivery.setArea(deliveryAreaBox.getValue());
			}
			if(!selectedDelivery.isDelivered() && yesChoice.isSelected())
				selectedDelivery.setDelivered(true);

			
			if(selectedDelivery instanceof RegularDelivery) {
				
				ArrayList<Order> changedOrders = new ArrayList<Order>();
				changedOrders.addAll(ordersList.getSelectionModel().getSelectedItems());
				if(changedOrders.isEmpty()) 
					throw new InvalidInputException("You must choose at least one order");
				for(Order o: ((RegularDelivery) selectedDelivery).getOrders()) {
					if(!changedOrders.contains(o))
						((RegularDelivery)selectedDelivery).removeOrder(o);			
				}
				for(Order o: changedOrders) {
					if(!((RegularDelivery) selectedDelivery).getOrders().contains(o))
						((RegularDelivery)selectedDelivery).addOrder(o);
				}
			}			
			else { //express
				if(!((ExpressDelivery) selectedDelivery).getOrder().equals(orderBox.getValue())) {
					if(orderBox.getValue() == null)
						throw new InvalidInputException("You must choose Order");
					((ExpressDelivery) selectedDelivery).setOrder(orderBox.getValue());
				}
					
					if(((ExpressDelivery) selectedDelivery).getPostage() != Double.parseDouble(postageField.getText()))
						((ExpressDelivery) selectedDelivery).setPostage(Double.parseDouble(postageField.getText()));
			}
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Delivery edited successfully!");
		alert.showAndWait();
		messageToUserOrder.setText("");
		DeliveryPersonBox.getSelectionModel().clearSelection();
		deliveryAreaBox.getSelectionModel().clearSelection();
		yesChoice.setSelected(false);
		orderBox.getSelectionModel().clearSelection();
		postageField.clear();
		allDeliveriesTable.getItems().clear();
		allDeliveriesTable.getItems().addAll(FXCollections.observableArrayList(
		Restaurant.getInstance().getDeliveries().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
		editDeliveryBtn.setDisable(true);
		addRegularBtn.setDisable(false);
		addExpressBtn.setDisable(false);
		}catch(InvalidInputException inputE) {
			messageToUserRegular.setFill(Color.RED);
			messageToUserRegular.setText(inputE.getMessage());
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
			if(order.getDelivery() != null)
				throw new InvalidInputException("This order is already linked to delivery");
			
			LocalDate dateOfDelivery;
			dateOfDelivery = deliveryDate.getValue();
			if(dateOfDelivery == null) {
				throw new InvalidInputException("Please select Date of Delivery");
			}
			double postage;
			if(postageField.getText().isEmpty()) 
				throw new InvalidInputException("Please fill postage");
			postage = Double.parseDouble(postageField.getText());
			boolean isDelivered;
			isDelivered = yesChoice.isSelected() ? true : false;
			
			Delivery newExpressDelivery = new ExpressDelivery(dp, da, isDelivered, order, postage, dateOfDelivery);
			///		
	
			Restaurant.getInstance().addDelivery(newExpressDelivery); 
			messageToUserRegular.setFill(Color.BLUE);
			messageToUserRegular.setText("Express Delivery added successfully");
			DeliveryPersonBox.getSelectionModel().clearSelection();
			deliveryAreaBox.getSelectionModel().clearSelection();
			orderBox.getSelectionModel().clearSelection();
			deliveryDate.setValue(null);
			yesChoice.setSelected(false);
			postageField.setText("");
			//update the list
			allDeliveriesTable.getItems().clear();
			allDeliveriesTable.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getDeliveries().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
		}catch(InvalidInputException inputE) {
			messageToUserRegular.setFill(Color.RED);
			messageToUserRegular.setText(inputE.getMessage());
		}catch(NumberFormatException ne) {
			messageToUserRegular.setFill(Color.RED);
			messageToUserRegular.setText("Wrong Input!");
		}catch(Exception ex) {
			messageToUserRegular.setFill(Color.RED);
			messageToUserRegular.setText("an error has accured please try again");
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
				throw new InvalidInputException("Please choose at least 2 orders!");
			if(deliveryOrders.size() == 1)
				throw new InvalidInputException("In Regular Delivery need to be 2 orders or more");
			for(Order o: deliveryOrders) {
				if(o.getDelivery() != null) {
					throw new InvalidInputException(o + " already linked to another delivery");
				}
			}
			
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
			allDeliveriesTable.getItems().clear();
			allDeliveriesTable.getItems().addAll(FXCollections.observableArrayList(
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
		Delivery selectedDelivery = allDeliveriesTable.getSelectionModel().getSelectedItem();
		if(selectedDelivery !=  null) {
			Restaurant.getInstance().removeDelivery(selectedDelivery);
			//update the list after removal
			allDeliveriesTable.getItems().clear();
			allDeliveriesTable.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getDeliveries().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
		}
	}
	
	public void signAsDelivered(ActionEvent e) {
		Delivery selectedDelivery = allDeliveriesTable.getSelectionModel().getSelectedItem();
		if(selectedDelivery != null) {
			if(!selectedDelivery.isDelivered()) {
				Restaurant.getInstance().deliver(selectedDelivery);
				allDeliveriesTable.getItems().clear();
				allDeliveriesTable.getItems().addAll(FXCollections.observableArrayList(
						Restaurant.getInstance().getDeliveries().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
			}
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
	
	//delivery area
	
	public void addDeliveryArea(ActionEvent e) {
		try {
			
			String daName = deliveryAreaName.getText();
			if(daName.isEmpty()) {
				throw new InvalidInputException("Please fill Area Name");
			}
			
			HashSet<Neighberhood> selectedNeighberhoods = new HashSet<Neighberhood>();
			for (Pair<CheckBox, Neighberhood> cbp : neighberhoodList) {
				CheckBox cb = cbp.getKey();
				if (cb.isSelected()) {
					selectedNeighberhoods.add(cbp.getValue());
				}
			}
			for(DeliveryArea area: Restaurant.getInstance().getAreas().values()) {
				for(Neighberhood n: area.getNeighberhoods()) {
					if(selectedNeighberhoods.contains(n))
						throw new InvalidInputException(n.name() + " is already in another delivery area.");
				}
			}
			
			if(selectedNeighberhoods.isEmpty()) {
				throw new InvalidInputException("Please select Neighberhoods");
			}
			
			int daTime = 0;
			try {
				daTime = Integer.parseInt(deliveryTime.getText()); 
			} catch(Exception exc) {
				throw new InvalidInputException("Please fill Devliery Time");
			}
			
			DeliveryArea da = new DeliveryArea(daName, selectedNeighberhoods, daTime);
			//Add delivery area to the restaurant and clear fields
			if(Restaurant.getInstance().addDeliveryArea(da)) {
				messageToUser.setFill(Color.BLUE);
				messageToUser.setText("Cook added successfully");
				deliveryAreaName.clear();
				deliveryTime.clear();
				for (Pair<CheckBox, Neighberhood> cbp : neighberhoodList) {
					CheckBox cb = cbp.getKey();
					cb.setSelected(false);
				}
				//update the list after addition
				allAreasTable.getItems().clear();
				allAreasTable.getItems().addAll(FXCollections.observableArrayList(
						Restaurant.getInstance().getAreas().entrySet().stream().map(a -> a.getValue()).collect(Collectors.toList())));
				newAreaBox.getSelectionModel().clearSelection();
			} else {
				messageToUser.setFill(Color.RED);
				messageToUser.setText("an error has accured please try again");
			}
		} catch(InvalidInputException ipe) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText(ipe.getMessage());
		} catch(Exception exc) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText("an error has accured please try again");
		}
		
	}
		
	public void removeDeliveryArea(ActionEvent e) {
		DeliveryArea selectedDeliveryArea = allAreasTable.getSelectionModel().getSelectedItem();
		if(selectedDeliveryArea !=  null) {
			removeLbl.setVisible(true);
			newAreaBox.setVisible(true);
			ObservableList<DeliveryArea> deliveryAreas = FXCollections.observableArrayList(Restaurant.getInstance().getAreas().values());
			newAreaBox.setItems(FXCollections.observableArrayList(deliveryAreas));	
			if(newAreaBox.getSelectionModel().getSelectedItem() != null) {
				removeLbl.setVisible(false);
				newAreaBox.setVisible(false);
				Restaurant.getInstance().removeDeliveryArea(selectedDeliveryArea, newAreaBox.getSelectionModel().getSelectedItem());
				//update the list after removal
				allAreasTable.getItems().clear();
				allAreasTable.getItems().addAll(FXCollections.observableArrayList(
						Restaurant.getInstance().getAreas().entrySet().stream().map(a -> a.getValue()).collect(Collectors.toList())));
				newAreaBox.getSelectionModel().clearSelection();
			}
		}
	}	
	
	public void editDeliveryArea(ActionEvent e) {
		DeliveryArea selectedDeliveryArea = allAreasTable.getSelectionModel().getSelectedItem();
		if(selectedDeliveryArea !=  null) {
			addDeliveryAreaBtn.setDisable(true);
			editDeliveryAreaBtn.setDisable(false);
			deliveryTime.setText(String.valueOf(selectedDeliveryArea.getDeliverTime()));
			deliveryTime.setDisable(true);
			deliveryAreaName.setText(selectedDeliveryArea.getAreaName());
			editDelPersonsLbl.setVisible(true);
			deliveryPersonsList.setVisible(true);
			editDeliveriesLbl.setVisible(true);
			deliveriesToAddList.setVisible(true);
			for(Delivery d: selectedDeliveryArea.getDelivers()) {
				deliveriesToAddList.getSelectionModel().select(d);
			}			
		}
	}
	
	public void setEditDeliveryArea(ActionEvent e) {
		DeliveryArea selectedArea = allAreasTable.getSelectionModel().getSelectedItem();
		try {
			
			if(!selectedArea.getAreaName().equals(deliveryAreaName.getText())) {
				if(deliveryAreaName.getText().isEmpty())
					throw new InvalidInputException("You must enter area name");
				selectedArea.setAreaName(deliveryAreaName.getText());
			}
			//neighberhoods
			HashSet<Neighberhood> selectedNeighberhoods = new HashSet<Neighberhood>();
			for (Pair<CheckBox, Neighberhood> cbp : neighberhoodList) {
				CheckBox cb = cbp.getKey();
				if (cb.isSelected()) {
					selectedNeighberhoods.add(cbp.getValue());
				}
			}
			
			HashSet<Neighberhood> toRemove = new HashSet<Neighberhood>();			
			if(selectedNeighberhoods.isEmpty()) 
				throw new InvalidInputException("You must choose at least one neighberhood");
			
				for(Neighberhood n:  selectedArea.getNeighberhoods()) {
					if(!selectedNeighberhoods.contains(n))
						toRemove.add(n);	
				}
				for(Neighberhood n: toRemove) {
					selectedArea.removeNeighberhood(n);
				}
				
				for(Neighberhood n: selectedNeighberhoods) {
					if(!selectedArea.getNeighberhoods().contains(n))
						selectedArea.addNeighberhood(n);
				}	
				
				//deliveries
				ArrayList<DeliveryPerson> changedDP = new ArrayList<DeliveryPerson>();
				changedDP.addAll(deliveryPersonsList.getSelectionModel().getSelectedItems());
				for(DeliveryPerson dp: selectedArea.getDelPersons()) {
					if(!changedDP.contains(dp))
						selectedArea.removeDelPerson(dp);			
				}
				for(DeliveryPerson dp: changedDP) {
					if(!selectedArea.getDelPersons().contains(dp))
						selectedArea.addDelPerson(dp);
				}
				
				//delivery person
				ArrayList<Delivery> changedDeliveries = new ArrayList<Delivery>();
				changedDeliveries.addAll(deliveriesToAddList.getSelectionModel().getSelectedItems());
				for(Delivery d: selectedArea.getDelivers()) {
					if(!changedDeliveries.contains(d))
						selectedArea.removeDelivery(d);			
				}
				for(Delivery d: changedDeliveries) {
					if(!selectedArea.getDelivers().contains(d))
						selectedArea.addDelivery(d);
				}
					
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Area edited successfully!");
		alert.showAndWait();
		messageToUser.setText("");
		deliveryAreaName.clear();
		deliveryTime.clear();
		deliveryPersonsList.getItems().clear();
		deliveriesToAddList.getItems().clear();
		for (Pair<CheckBox, Neighberhood> cbp : neighberhoodList) {
			CheckBox cb = cbp.getKey();
			cb.setSelected(false);
		}
		deliveriesToAddList.setVisible(false);
		deliveryPersonsList.setVisible(false);
		allAreasTable.getItems().clear();
		allAreasTable.getItems().addAll(FXCollections.observableArrayList(
		Restaurant.getInstance().getAreas().entrySet().stream().map(a -> a.getValue()).collect(Collectors.toList())));
		editDeliveryAreaBtn.setDisable(true);
		addDeliveryAreaBtn.setDisable(false);
		}catch(InvalidInputException inputE) {
			messageToUserRegular.setFill(Color.RED);
			messageToUserRegular.setText(inputE.getMessage());
		}
	}
	
}
	
