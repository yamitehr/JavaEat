package Controllers;

import java.util.stream.Collectors;
import Model.Delivery;
import Model.ExpressDelivery;
import Model.Order;
import Model.RegularDelivery;
import Model.Restaurant;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ManagerOrdersController_DELETE extends ControllerWrapper {
	//orders
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
	
	//deliveries
	@FXML
	private ListView<Delivery> allDeliveries;
	@FXML
	private Button addRegularDeliveryBtn;
	@FXML
	private Button addExpressDeliveryBtn;
	@FXML
	private Button backBtn;
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
	private Text postageField;
	
	@FXML
	private AnchorPane toReplacePane;
	
	
	@FXML
    public void initialize() {
		initOrder();
		initDelivery();
    }
	
	private void initOrder() {
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
	}
	
	private void initDelivery() {
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
	
	public void MoveToAddOrderScene(ActionEvent e) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddOrder.fxml"));
		AnchorPane pane = loader.load();
		toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
		toReplacePane.getChildren().add(pane);
	}
	public void MoveToAddRegularDeliveryScene(ActionEvent e) {
		moveToScene("/View/AddRegularDelivery.fxml", (Stage)addRegularDeliveryBtn.getScene().getWindow());
	}
	public void MoveToAddExpressDeliveryScene(ActionEvent e) {
		moveToScene("/View/AddExpressDelivery.fxml", (Stage)addExpressDeliveryBtn.getScene().getWindow());
	}	
	
	/*public void moveToManagerLandingPageScene(ActionEvent e) {
		moveToScene("/View/Manager_LandingPage.fxml", (Stage)backBtn.getScene().getWindow());
	}*/
	
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
}
