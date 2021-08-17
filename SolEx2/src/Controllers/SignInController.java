package Controllers;

import java.time.LocalDate;

import Exceptions.InvalidInputException;
import Exceptions.InvalidPersonInputException;
import Model.Customer;
import Model.Restaurant;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SignInController extends ControllerWrapper {

	private ObservableList<Neighberhood> neighberhood = FXCollections.observableArrayList(Neighberhood.values());
	@FXML
	private ChoiceBox<Neighberhood> NeighberhoodBox;	
	@FXML
	private void initialize() {
		NeighberhoodBox.setItems(neighberhood);
		returnBtn.setOnAction(e -> {
			moveToScene("/View/Login.fxml", (Stage)signInBtn.getScene().getWindow(), 700, 550);
		});
	}
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
	private CheckBox gluten;
	@FXML
	private CheckBox lactose;
	@FXML
	private PasswordField password;
	@FXML
	private TextArea resultConcole;
	@FXML
	private Button signInBtn;
	@FXML
	private ToggleGroup gender;
	@FXML
	private Button returnBtn;
	
	
	public void register(ActionEvent e) throws Exception {
		try {
			String user_Name = userName.getText();
			if(user_Name.isEmpty()) {
				throw new InvalidPersonInputException("Please fill User Name");
			}
			for(Customer c : Restaurant.getInstance().getCustomers().values()) {
				if(c.getUserName().equals(user_Name)) {
				throw new InvalidInputException("User Name already exists");
				}
			}
			String first_Name = firstName.getText();
			if(first_Name.isEmpty()) {
				throw new InvalidPersonInputException("Please fill First Name");
			}			
			String last_Name = lastName.getText();
			if(last_Name.isEmpty()) {
				throw new InvalidPersonInputException("Please fill Last Name");
			}
			LocalDate date;
			date = birthDate.getValue();
			if(birthDate == null) {
				throw new InvalidPersonInputException("Please select Date of Birth");
			}
			if(date.isAfter(LocalDate.now())) {
				throw new InvalidPersonInputException("Date of birth can't be in the future");
			}
			Neighberhood neighberhood = NeighberhoodBox.getValue();
			if(neighberhood == null) {
				throw new InvalidPersonInputException("Please select Neighborhood");
			}
			Gender gender;
			boolean sensitiveLactose = false;
			boolean sensitiveGluten = false;
			if(male.isSelected()) {
				gender = Gender.Male;
			}
			else if(female.isSelected()) {
				gender = Gender.Female;
			}
			else {
				gender = Gender.Unknown;
			}
		
			if(lactose.isSelected())
				sensitiveLactose = true;
			if(gluten.isSelected())
				sensitiveGluten = true;
		
			Customer customer = new Customer(firstName.getText(), lastName.getText(), birthDate.getValue(), 
					gender, NeighberhoodBox.getValue(), sensitiveLactose, sensitiveGluten, userName.getText(), password.getText());	
			MainController.rest.addCustomer(customer);
		
			//new Alert(Alert.AlertType.INFORMATION, "Welcome " + firstName.getText() + ".\n Please log in.").showAndWait();
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Welcome " + firstName.getText());
			alert.setHeaderText("Welcome " + firstName.getText() + ". \n Please log in to continue.");
			alert.showAndWait();
		
			moveToScene("/View/Login.fxml", (Stage)signInBtn.getScene().getWindow(), 700, 550);
		}catch(InvalidInputException iie) {
			resultConcole.setText(iie.getMessage());
		}catch(InvalidPersonInputException ipe) {
			resultConcole.setText(ipe.getMessage());
		} catch(Exception exc) {
			resultConcole.setText("an error has accured please try again");
		}
	}
	
	

}
