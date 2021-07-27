package Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

public class ManagerCustomerController extends ControllerWrapper {
	@FXML
	private Button addCustomerBtn;
	@FXML
	private Button removeCustomerBtn;
	@FXML
	private ListView<Customer> allCustomers;
	@FXML
	private ListView<Customer> blackList;
	@FXML
	private Text firstNameField;
	@FXML
	private Text lastNameField;
	@FXML
	private Text dobField;
	@FXML
	private Text neighberhoodField;
	@FXML
	private Text genderField;
	@FXML
	private Text sensitivitiesField;


	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		////////All customers list view
		//Set the listview cell factory to show the right customer name
		allCustomers.setCellFactory(param -> new ListCell<Customer>() {
		    @Override
		    protected void updateItem(Customer item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null) {
		            setText(null);
		        } else {
		            setText(item.getFirstName() + " " + item.getLastName());
		        }
		    }
		});
				
		//Add all customers
		allCustomers.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getCustomers().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
		
		//Event listener for listview
		allCustomers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
		    @Override
		    public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {
		    	updateCustomerDetailsFields();
		    }
		});
		
		////////BlackList list view
		//Set the listview cell factory for black list
		blackList.setCellFactory(param -> new ListCell<Customer>() {
		    @Override
		    protected void updateItem(Customer item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null) {
		            setText(null);
		        } else {
		            setText(item.getFirstName() + " " + item.getLastName());
		        }
		    }
		});
		
		//Add the black list customers
		blackList.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getBlackList().stream().collect(Collectors.toList())));
		
	}
	public void MoveToAddCustomerScene(ActionEvent e) {
		moveToScene("/View/AddCustomer.fxml", (Stage)addCustomerBtn.getScene().getWindow());
	}
	
	public void moveToManagerLandingPageScene(ActionEvent e) {
		moveToScene("/View/Manager_LandingPage.fxml", (Stage)addCustomerBtn.getScene().getWindow());
	}
	
	public void updateCustomerDetailsFields() {
		Customer selectedCustomer = allCustomers.getSelectionModel().getSelectedItem();
		// fill text fields with values about the selected customer on the list
		if(selectedCustomer != null) {
			firstNameField.setText(selectedCustomer.getFirstName());
			lastNameField.setText(selectedCustomer.getLastName());
			dobField.setText(selectedCustomer.getBirthDay().toString());
			neighberhoodField.setText(selectedCustomer.getNeighberhood().name());
			genderField.setText(selectedCustomer.getGender().name());
			
			String sensitivities = "";
			if (selectedCustomer.isSensitiveToGluten()) {
				sensitivities += "Gluten";
				if (selectedCustomer.isSensitiveToLactose()) {
					sensitivities += ", Lactose";
				}
			} else if (selectedCustomer.isSensitiveToLactose()) {
				sensitivities += "Lactose";
			}
			sensitivitiesField.setText(sensitivities);
		//clean the text fields if there is no selection
		} else if(selectedCustomer == null) {
			firstNameField.setText("");
			lastNameField.setText("");
			dobField.setText("");
			neighberhoodField.setText("");
			genderField.setText("");
			sensitivitiesField.setText("");
		}
	}
	
	public void removeCustomer(ActionEvent e) {
		Customer selectedCustomer = allCustomers.getSelectionModel().getSelectedItem();
		if(selectedCustomer !=  null) {
			Restaurant.getInstance().removeCustomer(selectedCustomer);
			//update the list after removal
			allCustomers.getItems().clear();
			allCustomers.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getCustomers().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
			//update the black list
			blackList.getItems().clear();
			blackList.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().getBlackList().stream().collect(Collectors.toList())));
		}
	}
	
	public void addCustomerToBlackList(ActionEvent e) {
		Customer selectedCustomer = allCustomers.getSelectionModel().getSelectedItem();
		if(selectedCustomer !=  null) {
			Restaurant.getInstance().addCustomerToBlackList(selectedCustomer);
			//update the black list
			blackList.getItems().clear();
			blackList.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().getBlackList().stream().collect(Collectors.toList())));
		}
	}
}
