package Controllers;

import java.util.stream.Collectors;

import Exceptions.InvalidInputException;
import Exceptions.InvalidPersonInputException;
import Model.Customer;
import Model.Restaurant;
import Model.State;
import Utils.Gender;
import Utils.Neighberhood;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class CustomerUpdatePersonalDetailsController {
	private ObservableList<Neighberhood> neighberhood = FXCollections.observableArrayList(Neighberhood.values());
	@FXML
	private ChoiceBox<Neighberhood> NeighberhoodBox;	
	@FXML
	private TextField userName;
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private DatePicker birthDate;
	@FXML
	private RadioButton male;
	@FXML
	private RadioButton female;
	@FXML
	private RadioButton unknown;
	@FXML
	private ToggleGroup gender;
	@FXML
	private CheckBox gluten;
	@FXML
	private CheckBox lactose;
	@FXML
	private PasswordField password;
	@FXML
	private Text messageToUser;
	@FXML
	private Button editBtn;
	@FXML
	private Button saveBtn;
	
	@FXML
	private void initialize() {
		initCustomerDetails();
	}
	
	private void initCustomerDetails() {
		Customer currentCustomer = State.getCurrentCustomer();
		if(currentCustomer !=  null) {
			saveBtn.setDisable(true);
			editBtn.setDisable(false);
			firstName.setText(currentCustomer.getFirstName());
			lastName.setText(currentCustomer.getLastName());
			birthDate.setValue(currentCustomer.getBirthDay());
			
			Gender customerGender = currentCustomer.getGender();
			//get gender
			try {
				for(Toggle g : gender.getToggles()) {
					RadioButton rb = (RadioButton) g;
					if(rb.getText().equals(customerGender.name())) {
						g.setSelected(true);
					}
				}
			} catch(Exception exc) {
				exc.printStackTrace();
			}
			
			NeighberhoodBox.setValue(currentCustomer.getNeighberhood());
			gluten.setSelected(currentCustomer.isSensitiveToGluten());	
			lactose.setSelected(currentCustomer.isSensitiveToLactose());
			
			firstName.setDisable(true);
			lastName.setDisable(true);
			birthDate.setDisable(true);
			female.setDisable(true);
			male.setDisable(true);
			unknown.setDisable(true);
			NeighberhoodBox.setDisable(true);
			gluten.setDisable(true);
			lactose.setDisable(true);
			
			editBtn.setOnAction(e -> {
				firstName.setDisable(false);
				lastName.setDisable(false);
				female.setDisable(false);
				male.setDisable(false);
				unknown.setDisable(false);
				NeighberhoodBox.setDisable(false);
				gluten.setDisable(false);
				lactose.setDisable(false);
				saveBtn.setDisable(false);
				editBtn.setDisable(true);
				messageToUser.setText("");
			});
			
			saveBtn.setOnAction(e ->{
				try {
					if(!currentCustomer.getFirstName().equals(firstName.getText())) {
						if(firstName.getText().isEmpty())
							throw new InvalidInputException("First Name cannot be empty");
						currentCustomer.setFirstName(firstName.getText());
					}
					if(!currentCustomer.getLastName().equals(lastName.getText())) {
						if(lastName.getText().isEmpty())
							throw new InvalidInputException("Last Name cannot be empty");
						currentCustomer.setLastName(lastName.getText());
					}
					Gender selectedGender = null;
					//get gender
					try {
						RadioButton selectedRadioButton = (RadioButton) gender.getSelectedToggle();
						String toogleGroupValue = selectedRadioButton.getText();
						for(Gender g : Gender.values()) {
							if(g.name().equals(toogleGroupValue)) {
								selectedGender = g;
							}
						}
						currentCustomer.setGender(selectedGender);
					} catch(Exception exc) {
						throw new InvalidInputException("Please fill Gender");
					}
						
					if(!currentCustomer.getNeighberhood().equals(NeighberhoodBox.getValue())) {
						if(NeighberhoodBox.getValue() == null)
							throw new InvalidInputException("you must choose Neighberhood");
						currentCustomer.setNeighberhood(NeighberhoodBox.getValue());
					}
					if(!currentCustomer.isSensitiveToGluten() && gluten.isSelected())
						currentCustomer.setSensitiveToGluten(true);
					if(currentCustomer.isSensitiveToGluten() && !gluten.isSelected())
						currentCustomer.setSensitiveToGluten(false);
					if(!currentCustomer.isSensitiveToLactose() && lactose.isSelected())
						currentCustomer.setSensitiveToLactose(true);
					if(currentCustomer.isSensitiveToLactose() && !lactose.isSelected())
						currentCustomer.setSensitiveToLactose(false);
					
					messageToUser.setText("details are edited successfully!");
					saveBtn.setDisable(true);
					editBtn.setDisable(false);
					
					firstName.setDisable(true);
					lastName.setDisable(true);
					birthDate.setDisable(true);
					female.setDisable(true);
					male.setDisable(true);
					unknown.setDisable(true);
					NeighberhoodBox.setDisable(true);
					gluten.setDisable(true);
					lactose.setDisable(true);
					
					}catch(InvalidInputException inputE) {
						messageToUser.setFill(Color.RED);
						messageToUser.setText(inputE.getMessage());
					}
				});
			}
		}
}
