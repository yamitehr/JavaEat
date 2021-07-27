package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCompleted;

import Exceptions.InvalidPersonInputException;
import Model.Cook;
import Model.Customer;
import Model.Restaurant;
import Utils.Expertise;
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

public class AddCookController extends ControllerWrapper{
	@FXML
	private ComboBox<String> expertiseBox;
	@FXML
	private TextField first_Name;
	@FXML
	private TextField last_Name;
	@FXML
	private ToggleGroup Gender_group;
	@FXML
	private DatePicker date;
	@FXML
	private CheckBox isChef;
	@FXML
	private Text messageToUser;
	

	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		expertiseBox.getItems().clear();
		
		ArrayList<String> expertiseNames = new ArrayList<String>();
		
		for(Expertise expert : Expertise.values()) {
			expertiseNames.add(expert.toString());
		}
		
		expertiseBox.getItems().addAll(FXCollections.observableArrayList(expertiseNames));
	}
	
	public void moveToManagerCooksScene(ActionEvent e) {
		moveToScene("/View/Manager_Cooks.fxml", (Stage)first_Name.getScene().getWindow());
	}
	
	//Adds the cook to the restaurant
	public void addCook(ActionEvent e) {
		
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
			Expertise expertise = null;
			for(Expertise expert : Expertise.values()) {
				if(expert.name().equals(expertiseBox.getValue())) {
					expertise = expert;
				}
			}
			if(expertise == null) {
				throw new InvalidPersonInputException("Please select Neighborhood");
			}
	
			boolean isChefChoice = isChef.isSelected();
			///
			
			Cook newCook = new Cook(firstName, lastName, birthDate, gender, 
											expertise, isChefChoice);
			
			//add customer to the restaurant
			if(Restaurant.getInstance().addCook(newCook)) {
				messageToUser.setFill(Color.BLUE);
				messageToUser.setText("Cook added successfully");
				first_Name.clear();
				last_Name.clear();
				Gender_group.getSelectedToggle().setSelected(false);
				expertiseBox.getSelectionModel().clearSelection();
				date.setValue(null);
				isChef.setSelected(false);
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
