package Controllers;

import java.util.stream.Collectors;

import Model.Delivery;
import Model.ExpressDelivery;
import Model.RegularDelivery;
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

public class ManagerDeliveryController extends ControllerWrapper {
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
    public void initialize() {
		init();
    }
	
	private void init() {
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
	
	public void MoveToAddRegularDeliveryScene(ActionEvent e) {
		moveToScene("/View/AddRegularDelivery.fxml", (Stage)addRegularDeliveryBtn.getScene().getWindow());
	}
	public void MoveToAddExpressDeliveryScene(ActionEvent e) {
		moveToScene("/View/AddExpressDelivery.fxml", (Stage)addExpressDeliveryBtn.getScene().getWindow());
	}	
	public void moveToManagerLandingPageScene(ActionEvent e) {
		moveToScene("/View/Manager_LandingPage.fxml", (Stage)backBtn.getScene().getWindow());
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
