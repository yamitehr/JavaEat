package Controllers;

import java.util.stream.Collectors;
import Model.Order;
import Model.Restaurant;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ManagerOrdersController extends ControllerWrapper {
	@FXML
	private ListView<Order> allOrders;
	@FXML
	private Button backBtn;
	@FXML
	private Button addOrderBtn;
	@FXML
	private Text customerField;
	@FXML
	private Text dishesField;
	@FXML
	private Text deliveryField;
	
	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
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
	
	public void MoveToAddOrderScene(ActionEvent e) {
		moveToScene("/View/AddOrder.fxml", (Stage)addOrderBtn.getScene().getWindow());
	}
	
	public void moveToManagerLandingPageScene(ActionEvent e) {
		moveToScene("/View/Manager_LandingPage.fxml", (Stage)backBtn.getScene().getWindow());
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
}
