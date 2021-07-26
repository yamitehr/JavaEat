package Controllers;

import java.awt.ScrollPane;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import Model.Customer;
import Utils.Gender;
import Utils.Neighberhood;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class SignInController {

	private ObservableList<Neighberhood> neighberhood = FXCollections.observableArrayList(Neighberhood.values());
	@FXML
	private ChoiceBox<Neighberhood> NeighberhoodBox;
	
	@FXML
	private void initialize() {
		NeighberhoodBox.setValue(Neighberhood.DownTown);
		NeighberhoodBox.setItems(neighberhood);
	}
	@FXML
	private AnchorPane mainScreen;
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
	private Label check;
	
	public void register(ActionEvent e) throws Exception {
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
		
		AnchorPane loginForm = FXMLLoader.load(getClass().getResource(("Login.fxml")));
        mainScreen.getChildren().setAll(loginForm);
		/*FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
		AnchorPane pane = loader.load();
		mainScreen.getChildren().removeAll(mainScreen.getChildren());
		mainScreen.getChildren().add(pane);*/
	}
	
	

}
