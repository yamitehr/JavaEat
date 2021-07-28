package Controllers;

import java.time.LocalDate;
import java.util.TreeSet;
import Exceptions.InvalidInputException;
import Model.Delivery;
import Model.DeliveryArea;
import Model.DeliveryPerson;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddRegularDeliveryController extends ControllerWrapper {
	@FXML
	private ComboBox<DeliveryPerson> DeliveryPersonBox;
	@FXML
	private ComboBox<DeliveryArea> deliveryAreaBox;
	@FXML
	private CheckBox yesChoice;
	@FXML
	private DatePicker deliveryDate;
	@FXML
	private ListView<Order> ordersList;
	@FXML
	private Button backBtn;
	@FXML
	private Text messageToUser;
	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		ObservableList<DeliveryPerson> deliveryPersons = FXCollections.observableArrayList(Restaurant.getInstance().getDeliveryPersons().values());
		DeliveryPersonBox.getItems().clear();				
		DeliveryPersonBox.setItems(FXCollections.observableArrayList(deliveryPersons));
		
		ObservableList<DeliveryArea> deliveryAreas = FXCollections.observableArrayList(Restaurant.getInstance().getAreas().values());
		deliveryAreaBox.getItems().clear();				
		deliveryAreaBox.setItems(FXCollections.observableArrayList(deliveryAreas));
		
		
		ObservableList<Order> orders = FXCollections.observableArrayList(Restaurant.getInstance().getOrders().values());
		ordersList.setItems(orders);
		ordersList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	public void moveToManagerOrderScene(ActionEvent e) {
		moveToScene("/View/Manager_Delivery.fxml", (Stage)backBtn.getScene().getWindow());
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
			messageToUser.setFill(Color.BLUE);
			messageToUser.setText("Dish added successfully");
			DeliveryPersonBox.getSelectionModel().clearSelection();
			deliveryAreaBox.getSelectionModel().clearSelection();
			deliveryDate.setValue(null);
			yesChoice.setSelected(false);
		}catch(InvalidInputException inputE) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText(inputE.getMessage());
		}catch(Exception ex) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText("an error has accured please try again");
		}
	}
	
}
