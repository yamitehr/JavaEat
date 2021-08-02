package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import Exceptions.InvalidInputException;
import Exceptions.InvalidPersonInputException;
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
import Utils.Neighberhood;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

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
	private Button editDeliveryBtn;
	
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
	private ListView<DeliveryArea> allDeliveryAreas;
	@FXML
	private Text areaNameField;
	@FXML
	private Text deliveryTimeField;
	@FXML
	private Text neighborhoodField;
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
	private AnchorPane toReplacePane;
	
	@FXML
    public void initialize() {
		init();
		generateNeighborhoodGrid();
		addDeliveryTimeEventListener();
    }
	
	private void init() {
		newAreaBox.setVisible(false);
		removeLbl.setVisible(false);
		
		
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
		
		//for delivery area edit
		deliveryPersonsList.setItems(deliveryPersons);
		deliveryPersonsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		ObservableList<Delivery> deliveries = FXCollections.observableArrayList(Restaurant.getInstance().getDeliveries().values());
		deliveriesToAddList.setItems(deliveries);
		deliveriesToAddList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
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
			
		private void generateNeighborhoodGrid() {
			neighberhoodList = new ArrayList<Pair<CheckBox, Neighberhood>>();
			GridPane grid = new GridPane();
			grid.setBorder(new Border(new BorderStroke(Color.BLACK, 
			           BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			grid.setHgap(12);
			grid.setVgap(12);
				
				
			for (Neighberhood n : Neighberhood.values()) {
				CheckBox cb = new CheckBox(n.name());
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
			deliveryTime.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				    	if (newValue != "") {
					    	try {
								Integer.parseInt(newValue);
							} catch(NumberFormatException nfe) {
								deliveryTime.setText(oldValue);
							}	
				    	}
				    }
				});
				
				////////All delivery people list view
				//Set the listview cell factory to show the right delivery person name
				allDeliveryAreas.setCellFactory(param -> new ListCell<DeliveryArea>() {
				    @Override
				    protected void updateItem(DeliveryArea item, boolean empty) {
				        super.updateItem(item, empty);

				        if (empty || item == null) {
				            setText(null);
				        } else {
				            setText(item.getAreaName());
				        }
				    }
				});
						
				//Add all delivery people
				allDeliveryAreas.getItems().addAll(FXCollections.observableArrayList(
						Restaurant.getInstance().getAreas().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
				
				//Event listener for listview
				allDeliveryAreas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DeliveryArea>() {
				    @Override
				    public void changed(ObservableValue<? extends DeliveryArea> observable, DeliveryArea oldValue, DeliveryArea newValue) {
				    	updateDeliveryAreaDetailsFields();
				    }
				});
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
		Order selectedOrder = allOrders.getSelectionModel().getSelectedItem();
		if(selectedOrder !=  null) {
			Restaurant.getInstance().removeOrder(selectedOrder);
			//update the list after removal
			allOrders.getItems().clear();
			allOrders.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getOrders().entrySet().stream().map(o -> o.getValue()).collect(Collectors.toList())));
		}
	}
	
	//delivery
	
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
				postageText.setText(String.valueOf(((ExpressDelivery) selectedDelivery).getPostage()));
				
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
	
	public void editDelivery(ActionEvent e) {
		Delivery selectedDelivery = allDeliveries.getSelectionModel().getSelectedItem();
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
			allDeliveries.getItems().clear();
			allDeliveries.getItems().addAll(FXCollections.observableArrayList(
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
	
	
	//delivery area
	public void updateDeliveryAreaDetailsFields() {
		DeliveryArea selectedDeliveryArea = allDeliveryAreas.getSelectionModel().getSelectedItem();
		// fill text fields with values about the selected delivery area on the list
		if(selectedDeliveryArea != null) {
			areaNameField.setText(selectedDeliveryArea.getAreaName());
			deliveryTimeField.setText(String.valueOf(selectedDeliveryArea.getDeliverTime()));
			String neighborhoodsStr = "";
			if (selectedDeliveryArea.getNeighberhoods().size() > 0) {
				for(Neighberhood n : selectedDeliveryArea.getNeighberhoods()) {
					neighborhoodsStr += n.name() + ", ";
				}
				neighborhoodsStr = neighborhoodsStr.substring(0, neighborhoodsStr.length() - 3);
			}
			neighborhoodField.setText(neighborhoodsStr);
			
		//clean the text fields if there is no selection
		} else if(selectedDeliveryArea == null) {
			areaNameField.setText("");
			deliveryTimeField.setText("");
			neighborhoodField.setText("");
		}
	}
	
	public void addDeliveryArea(ActionEvent e) {
		try {
			
			String daName = deliveryAreaName.getText();
			if(daName.isEmpty()) {
				throw new InvalidPersonInputException("Please fill Area Name");
			}
			
			HashSet<Neighberhood> selectedNeighberhoods = new HashSet<Neighberhood>();
			for (Pair<CheckBox, Neighberhood> cbp : neighberhoodList) {
				CheckBox cb = cbp.getKey();
				if (cb.isSelected()) {
					selectedNeighberhoods.add(cbp.getValue());
				}
			}
			
			if(selectedNeighberhoods.isEmpty()) {
				throw new InvalidPersonInputException("Please select Neighberhoods");
			}
			
			int daTime = 0;
			try {
				daTime = Integer.parseInt(deliveryTime.getText()); 
			} catch(Exception exc) {
				throw new InvalidPersonInputException("Please fill Devliery Time");
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
				allDeliveryAreas.getItems().clear();
				allDeliveryAreas.getItems().addAll(FXCollections.observableArrayList(
						Restaurant.getInstance().getAreas().entrySet().stream().map(a -> a.getValue()).collect(Collectors.toList())));
				newAreaBox.getSelectionModel().clearSelection();
			} else {
				messageToUser.setFill(Color.RED);
				messageToUser.setText("an error has accured please try again");
			}
		} catch(InvalidPersonInputException ipe) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText(ipe.getMessage());
		} catch(Exception exc) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText("an error has accured please try again");
		}
		
	}
		
	public void removeDeliveryArea(ActionEvent e) {
		DeliveryArea selectedDeliveryArea = allDeliveryAreas.getSelectionModel().getSelectedItem();
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
				allDeliveryAreas.getItems().clear();
				allDeliveryAreas.getItems().addAll(FXCollections.observableArrayList(
						Restaurant.getInstance().getAreas().entrySet().stream().map(a -> a.getValue()).collect(Collectors.toList())));
				newAreaBox.getSelectionModel().clearSelection();
			}
		}
	}	
	
	public void editDeliveryArea(ActionEvent e) {
		DeliveryArea selectedDeliveryArea = allDeliveryAreas.getSelectionModel().getSelectedItem();
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
		}
	}
}
	
