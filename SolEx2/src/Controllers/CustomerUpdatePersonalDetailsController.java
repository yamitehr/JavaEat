package Controllers;

import java.time.LocalDate;

import Exceptions.InvalidInputException;
import Model.Customer;
import Model.Restaurant;
import Model.State;
import Utils.Gender;
import Utils.Neighberhood;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CustomerUpdatePersonalDetailsController extends ControllerWrapper{
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
	private TextField showPass;
	@FXML
	private TextArea resultConcole;
	@FXML
	private Button editBtn;
	@FXML
	private Button saveBtn;
	@FXML
	private CheckBox showPassCheckBox;
	
	@FXML
	private void initialize() {
		initCustomerDetails();
		initValidations();
	}
	
	private void initCustomerDetails() {
		Customer currentCustomer = State.getCurrentCustomer();
		if(currentCustomer !=  null) {
			saveBtn.setDisable(true);
			editBtn.setDisable(false);
			firstName.setText(currentCustomer.getFirstName());
			lastName.setText(currentCustomer.getLastName());
			birthDate.setValue(currentCustomer.getBirthDay());
			userName.setText(currentCustomer.getUserName());
			password.setText(currentCustomer.getPassword());
			
			password.setVisible(true);
			showPass.setVisible(false);
			
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
			userName.setDisable(true);
			password.setDisable(true);
			showPass.setDisable(true);
			
			editBtn.setOnAction(e -> {
				firstName.setDisable(false);
				lastName.setDisable(false);
				female.setDisable(false);
				male.setDisable(false);
				unknown.setDisable(false);
				NeighberhoodBox.setDisable(false);
				gluten.setDisable(false);
				lactose.setDisable(false);
				userName.setDisable(false);
				password.setDisable(false);
				showPass.setDisable(false);
				saveBtn.setDisable(false);
				editBtn.setDisable(true);
				resultConcole.setText("");
			});
			
			showPassCheckBox.setOnAction( e -> {
				if(showPassCheckBox.isSelected()) {
					String pass = password.getText();
					showPass.setText(pass);
					password.setVisible(false);
					showPass.setVisible(true);
				} else {
					showPass.setVisible(false);
					password.setVisible(true);
					password.setText(showPass.getText());
				}
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
					
					if(!currentCustomer.getUserName().equals(userName.getText())) {
						if(userName.getText().isEmpty())
							throw new InvalidInputException("User Name cannot be empty");
						for(Customer c : Restaurant.getInstance().getCustomers().values()) {
							if(c.getUserName().equals(userName.getText())) {
							throw new InvalidInputException("User Name already exists");
							}
						}
						currentCustomer.setUserName(userName.getText());
					}
					
					if(password.isVisible()) {
						if(!currentCustomer.getPassword().equals(password.getText())) {
							if(password.getText().isEmpty())
								throw new InvalidInputException("Password cannot be empty");
							currentCustomer.setPassword(password.getText());
						}
					} else if(showPass.isVisible()) {
						if(!currentCustomer.getPassword().equals(showPass.getText())) {
							if(password.getText().isEmpty())
								throw new InvalidInputException("Password cannot be empty");
							currentCustomer.setPassword(showPass.getText());
						}
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
					
					resultConcole.setText("details are edited successfully!");
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
					userName.setDisable(true);
					password.setDisable(true);
					showPass.setDisable(true);
					
					}catch(InvalidInputException inputE) {
						soundOfButton("error.mp3");
						resultConcole.setText(inputE.getMessage());
					} catch(Exception exc) {
						soundOfButton("error.mp3");
						resultConcole.setText("an error has accured please try again");
					}
				});
			}
		}
	
	public void initValidations() {
		firstName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				    	if (newValue != "") {
				    		resultConcole.setText("");
					    	if(!newValue.matches("[a-zA-Z\s]+")) {
					    		firstName.setText(newValue.substring(0, newValue.length()-1));
					    		resultConcole.setText("Letters Only!");
					    	}
				    	}
				    }
				});
		
		 lastName.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					    	if (newValue != "") {
					    		resultConcole.setText("");
						    	if(!newValue.matches("[a-zA-Z\s]+")) {
						    		lastName.setText(newValue.substring(0, newValue.length()-1));
						    		resultConcole.setText("Letters Only!");
						    	}
					    	}
					    }
					});
	}
}
