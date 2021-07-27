package Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Model.Cook;
import Model.Customer;
import Model.Restaurant;
import Utils.Neighberhood;
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

public class ManagerCooksController extends ControllerWrapper {
	@FXML
	private Button addCookBtn;
	@FXML
	private Button removeCookBtn;
	@FXML
	private ListView<Cook> allCooks;
	@FXML
	private Text firstNameField;
	@FXML
	private Text lastNameField;
	@FXML
	private Text dobField;
	@FXML
	private Text expertiseField;
	@FXML
	private Text genderField;
	@FXML
	private Text isChefFiled;


	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		////////All cooks list view
		//Set the listview cell factory to show the right cook name
		allCooks.setCellFactory(param -> new ListCell<Cook>() {
		    @Override
		    protected void updateItem(Cook item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null) {
		            setText(null);
		        } else {
		            setText(item.getFirstName() + " " + item.getLastName());
		        }
		    }
		});
				
		//Add all cooks
		allCooks.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getCooks().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
		
		//Event listener for listview
		allCooks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Cook>() {
		    @Override
		    public void changed(ObservableValue<? extends Cook> observable, Cook oldValue, Cook newValue) {
		    	updateCustomerDetailsFields();
		    }
		});
	}

	public void MoveToAddCookScene(ActionEvent e) {
		moveToScene("/View/AddCook.fxml", (Stage)addCookBtn.getScene().getWindow());
	}
	
	public void moveToManagerLandingPageScene(ActionEvent e) {
		moveToScene("/View/Manager_LandingPage.fxml", (Stage)addCookBtn.getScene().getWindow());
	}
	
	public void updateCustomerDetailsFields() {
		Cook selectedCook = allCooks.getSelectionModel().getSelectedItem();
		// fill text fields with values about the selected customer on the list
		if(selectedCook != null) {
			firstNameField.setText(selectedCook.getFirstName());
			lastNameField.setText(selectedCook.getLastName());
			dobField.setText(selectedCook.getBirthDay().toString());
			expertiseField.setText(selectedCook.getExpert().name());
			genderField.setText(selectedCook.getGender().name());
			
			String isChefText = "";
			if (selectedCook.isChef()) {
				isChefText += "Yes";
			} else {
				isChefText += "No";
			}
			isChefFiled.setText(isChefText);
			
		//clean the text fields if there is no selection
		} else if(selectedCook == null) {
			firstNameField.setText("");
			lastNameField.setText("");
			dobField.setText("");
			expertiseField.setText("");
			genderField.setText("");
			isChefFiled.setText("");
		}
	}
	
	public void removeCook(ActionEvent e) {
		Cook selectedCook = allCooks.getSelectionModel().getSelectedItem();
		if(selectedCook !=  null) {
			Restaurant.getInstance().removeCook(selectedCook);
			//update the list after removal
			allCooks.getItems().clear();
			allCooks.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().getCooks().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
		}
	}
}
