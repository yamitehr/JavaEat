package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

import Exceptions.InvalidPersonInputException;
import Model.Customer;
import Model.Restaurant;
import Utils.Gender;
import Utils.Neighberhood;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
		neighberhoodsBox.getItems().clear();
		
		ArrayList<String> neighberhoodNames = new ArrayList<String>();
		
		for(Neighberhood n : Neighberhood.values()) {
			neighberhoodNames.add(n.toString());
		}
		
		neighberhoodsBox.getItems().addAll(FXCollections.observableArrayList(neighberhoodNames));
		
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
				//update the lists
				//update the list after removal
				allCustomers.getItems().clear();
				allCustomers.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getCustomers().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
				//update the black list
				blackList.getItems().clear();
				blackList.getItems().addAll(FXCollections.observableArrayList(
						Restaurant.getInstance().getBlackList().stream().collect(Collectors.toList())));
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
