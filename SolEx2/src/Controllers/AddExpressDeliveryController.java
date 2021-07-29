package Controllers;

import java.time.LocalDate;
import Exceptions.InvalidInputException;
import Model.Delivery;
import Model.DeliveryArea;
import Model.DeliveryPerson;
import Model.ExpressDelivery;
import Model.Order;
import Model.Restaurant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddExpressDeliveryController extends ControllerWrapper {
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
		orderBox.getItems().clear();				
		orderBox.setItems(FXCollections.observableArrayList(orders));
	}
	
	public void moveToManagerOrderScene(ActionEvent e) {
		moveToScene("/View/Manager_Delivery.fxml", (Stage)backBtn.getScene().getWindow());
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
			messageToUser.setFill(Color.BLUE);
			messageToUser.setText("Express Delivery added successfully");
			DeliveryPersonBox.getSelectionModel().clearSelection();
			deliveryAreaBox.getSelectionModel().clearSelection();
			orderBox.getSelectionModel().clearSelection();
			deliveryDate.setValue(null);
			yesChoice.setSelected(false);
		}catch(InvalidInputException inputE) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText(inputE.getMessage());
		}catch(NumberFormatException ne) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText("Wrong Input!");
		}catch(Exception ex) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText("an error has accured please try again");
		}
	}

}
