package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import Exceptions.InvalidPersonInputException;
import Model.Cook;
import Model.DeliveryArea;
import Model.DeliveryPerson;
import Model.Restaurant;
import Utils.Gender;
import Utils.Neighberhood;
import Utils.Vehicle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddDeliveryPersonController extends ControllerWrapper{
	@FXML
	private ComboBox<String> vehicleBox;
	@FXML
	private TextField first_Name;
	@FXML
	private TextField last_Name;
	@FXML
	private ToggleGroup Gender_group;
	@FXML
	private DatePicker date;
	@FXML
	private ComboBox<DeliveryArea> deliveryAreaBox;
	@FXML
	private Text messageToUser;
	

	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		//add vehicle list
		vehicleBox.getItems().clear();
		
		ArrayList<String> vehicleNames = new ArrayList<String>();
		
		for(Vehicle v : Vehicle.values()) {
			vehicleNames.add(v.toString());
		}
		
		vehicleBox.getItems().addAll(FXCollections.observableArrayList(vehicleNames));
		
		//add areas list
		//Set the cell factory to show the delivery areas
			deliveryAreaBox.setCellFactory(param -> new ListCell<DeliveryArea>() {
				    @Override
				    protected void updateItem(DeliveryArea item, boolean empty) {
				        super.updateItem(item, empty);

				        if (empty || item == null) {
				            setText(null);
				        } else {
				            setText(item.getAreaName() + " (id= " + item.getId() + ")");
				        }
				    }
				});
						
			//Add all areas
			deliveryAreaBox.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().getAreas().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
	}

	public void moveToManagerDeliveryPersonScene(ActionEvent e) {
		moveToScene("/View/Manager_DeliveryPerson.fxml", (Stage)first_Name.getScene().getWindow());
	}
	
	//Adds the delivery person to the restaurant
	public void addDeliveryPerson(ActionEvent e) {
		
		try {
			
			String firstName = first_Name.getText();
			if(firstName.isEmpty()) {
				throw new InvalidPersonInputException("Please fill First Name");
			}
			
			String lastName = last_Name.getText();
			if(lastName.isEmpty()) {
				throw new InvalidPersonInputException("Please fill Last Name");
			}
			
			//get DOB
			LocalDate birthDate;
			birthDate = date.getValue();
			if(birthDate == null) {
				throw new InvalidPersonInputException("Please select Date of Birth");
			}
			if(birthDate.isAfter(LocalDate.now())) {
				throw new InvalidPersonInputException("Date of birth can't be in the future");
			}
			
			Gender gender = null;
			//get gender
			try {
				RadioButton selectedRadioButton = (RadioButton) Gender_group.getSelectedToggle();
				String toogleGroupValue = selectedRadioButton.getText();
				for(Gender g : Gender.values()) {
					if(g.name().equals(toogleGroupValue)) {
						gender = g;
					}
				}
			} catch(Exception exc) {
				throw new InvalidPersonInputException("Please fill Gender");
			}
			
			//get Vehicle
			Vehicle vehicle = null;
			for(Vehicle v : Vehicle.values()) {
				if(v.name().equals(vehicleBox.getValue())) {
					vehicle = v;
				}
			}
			if(vehicle == null) {
				throw new InvalidPersonInputException("Please select Vehicle");
			}
	
			DeliveryArea selectedDeliveryArea = deliveryAreaBox.getSelectionModel().getSelectedItem();
			if(selectedDeliveryArea == null) {
				throw new InvalidPersonInputException("Please select Delivery Area");
			}
			
			
			///
			
			DeliveryPerson newDeliveryPerson = new DeliveryPerson(firstName, lastName, birthDate, gender, 
											vehicle, selectedDeliveryArea);
			
			//add deliveryPerson to the restaurant
			if(Restaurant.getInstance().addDeliveryPerson(newDeliveryPerson, selectedDeliveryArea)) {
				messageToUser.setFill(Color.BLUE);
				messageToUser.setText("Cook added successfully");
				first_Name.clear();
				last_Name.clear();
				Gender_group.getSelectedToggle().setSelected(false);
				vehicleBox.getSelectionModel().clearSelection();
				date.setValue(null);
			}else {
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
}
