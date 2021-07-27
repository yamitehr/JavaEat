package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCompleted;

import Exceptions.InvalidPersonInputException;
import Model.Customer;
import Model.Restaurant;
import Utils.Gender;
import Utils.Neighberhood;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddCustomerController extends ControllerWrapper{
	@FXML
	private ComboBox<String> neighberhoodsBox;

	@FXML
	private TextField first_Name;
	@FXML
	private TextField last_Name;
	@FXML
	private ToggleGroup Gender_group;
	@FXML
	private DatePicker date;
	@FXML
	private CheckBox isLactose;
	@FXML
	private CheckBox isGluten;
	@FXML
	private Text messageToUser;
	

	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		neighberhoodsBox.getItems().clear();
		
		ArrayList<String> neighberhoodNames = new ArrayList<String>();
		
		for(Neighberhood n : Neighberhood.values()) {
			neighberhoodNames.add(n.toString());
		}
		
		neighberhoodsBox.getItems().addAll(FXCollections.observableArrayList(neighberhoodNames));
	}
	
	public void moveToManagerCustomerScene(ActionEvent e) {
		moveToScene("/View/Manager_Customer.fxml", (Stage)first_Name.getScene().getWindow());
	}
	
	//Adds the customer to the restaurant
	public void addCustomer(ActionEvent e) {
		
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
			
			//get Neighborhood
			Neighberhood neighberhood = null;
	
			for(Neighberhood n : Neighberhood.values()) {
				if(n.name().equals(neighberhoodsBox.getValue())) {
					neighberhood = n;
				}
			}
			if(neighberhood == null) {
				throw new InvalidPersonInputException("Please select Neighborhood");
			}
	
			boolean isSensitiveToLactose = isLactose.isSelected();
			boolean isSensitiveToGluten = isGluten.isSelected();
			///
			
			Customer newCustomer = new Customer(firstName, lastName, birthDate, gender, 
											neighberhood, isSensitiveToLactose, isSensitiveToGluten, "", "");
			
			//add customer to the restaurant
			if(Restaurant.getInstance().addCustomer(newCustomer)) {
				messageToUser.setFill(Color.BLUE);
				messageToUser.setText("Customer added successfully");
				first_Name.clear();
				last_Name.clear();
				Gender_group.getSelectedToggle().setSelected(false);
				neighberhoodsBox.getSelectionModel().clearSelection();
				date.setValue(null);
				isLactose.setSelected(false);
				isGluten.setSelected(false);
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
