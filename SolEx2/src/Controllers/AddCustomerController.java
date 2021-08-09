package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Exceptions.InvalidInputException;
import Exceptions.InvalidPersonInputException;
import Model.Cook;
import Model.Customer;
import Model.Restaurant;
import Utils.Expertise;
import Utils.Gender;
import Utils.Neighberhood;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddCustomerController extends ControllerWrapper{
	@FXML
	private ComboBox<Neighberhood> neighberhoodsBox;

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
	private Button editCustomerBtn;
	@FXML
	private Button removeCustomerBtn;
	@FXML
	private TableView<Customer> allCustomersTable;
	@FXML
	private TableColumn<Customer, Integer> customerIdCol;
	@FXML
	private TableColumn<Customer, String> customerNameCol;
	@FXML
	private TableColumn<Customer, String> customerDobCol;
	@FXML
	private TableColumn<Customer, String> customerGenderCol;
	@FXML
	private TableColumn<Customer, String> neighberhoodCol;
	@FXML
	private TableColumn<Customer, String> sensitivitiesCol;
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
		
		ObservableList<Neighberhood> neighberhoods = FXCollections.observableArrayList(Neighberhood.values());
		neighberhoodsBox.getItems().clear();				
		neighberhoodsBox.setItems(FXCollections.observableArrayList(neighberhoods));
	
				
		//Add all customers
		customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));	
		
		customerNameCol.setCellValueFactory(cust -> new ReadOnlyObjectWrapper<String>(cust.getValue().getFirstName() + " " + cust.getValue().getLastName()));	
		
		customerDobCol.setCellValueFactory(cust -> new ReadOnlyObjectWrapper<String>(cust.getValue().getBirthDay().toString()));
		
		customerGenderCol.setCellValueFactory(cust -> new ReadOnlyObjectWrapper<String>(cust.getValue().getGender().name()));
		
		neighberhoodCol.setCellValueFactory(cust -> new ReadOnlyObjectWrapper<String>(cust.getValue().getNeighberhood().toString()));
		
		sensitivitiesCol.setCellValueFactory(cust -> {
			 boolean isSensitiveToGluten = cust.getValue().isSensitiveToGluten();
	            boolean isSensitiveToLactose = cust.getValue().isSensitiveToLactose();
	            String isSensitiveToAsString = "";
	            if(isSensitiveToGluten == true)
	            {
	            	isSensitiveToAsString += "Gluten ";
	            }
	            if(isSensitiveToLactose == true)
	            {
	            	isSensitiveToAsString += "Lactose";
	            }

	         return new ReadOnlyStringWrapper(isSensitiveToAsString);
        });
		
		List<Customer> allCustomers = new ArrayList<Customer>();
		allCustomers = Restaurant.getInstance().getCustomers().values().stream()
				.collect(Collectors.toList());
		
		allCustomersTable.getItems().addAll(allCustomers);
		
		////////BlackList list view
		//Set the listview cell factory for black list
		blackList.setCellFactory(param -> new ListCell<Customer>() {
		    @Override
		    protected void updateItem(Customer item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null) {
		            setText(null);
		        } else {
		            setText(item.getId() + " " + item.getFirstName() + " " + item.getLastName());
		        }
		    }
		});
		
		//Add the black list customers
		blackList.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getBlackList().stream().collect(Collectors.toList())));
	}
	
	public void updateCustomerDetailsFields() {
		Customer selectedCustomer = allCustomersTable.getSelectionModel().getSelectedItem();
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
	
	public void editCustomer(ActionEvent e) {
		Customer selectedCustomer = allCustomersTable.getSelectionModel().getSelectedItem();
		if(selectedCustomer !=  null) {
			addCustomerBtn.setDisable(true);
			editCustomerBtn.setDisable(false);
			first_Name.setText(selectedCustomer.getFirstName());
			last_Name.setText(selectedCustomer.getLastName());
			date.setValue(selectedCustomer.getBirthDay());
			Gender_group.setUserData(selectedCustomer.getGender());
			neighberhoodsBox.setValue(selectedCustomer.getNeighberhood());
			isGluten.setSelected(selectedCustomer.isSensitiveToGluten());	
			isLactose.setSelected(selectedCustomer.isSensitiveToLactose());
			
			date.setDisable(true);
		}
	}
	
	public void setEditCustomer(ActionEvent e) {
		Customer selectedCustomer = allCustomersTable.getSelectionModel().getSelectedItem();
		try {
			if(!selectedCustomer.getFirstName().equals(first_Name.getText())) {
				if(first_Name.getText().isEmpty())
					throw new InvalidInputException("First Name cannot be empty");
				selectedCustomer.setFirstName(first_Name.getText());
			}
			if(!selectedCustomer.getLastName().equals(last_Name.getText())) {
				if(last_Name.getText().isEmpty())
					throw new InvalidInputException("Last Name cannot be empty");
				selectedCustomer.setLastName(last_Name.getText());
			}
			if(!selectedCustomer.getGender().equals(Gender_group.getUserData()))
				selectedCustomer.setGender((Gender)Gender_group.getUserData());
				
		if(!selectedCustomer.getNeighberhood().equals(neighberhoodsBox.getValue())) {
			if(neighberhoodsBox.getValue() == null)
				throw new InvalidInputException("you must choose Neighberhood");
			selectedCustomer.setNeighberhood(neighberhoodsBox.getValue());
		}
		if(!selectedCustomer.isSensitiveToGluten() && isGluten.isSelected())
			selectedCustomer.setSensitiveToGluten(true);
		if(selectedCustomer.isSensitiveToGluten() && !isGluten.isSelected())
			selectedCustomer.setSensitiveToGluten(false);
		if(!selectedCustomer.isSensitiveToLactose() && isLactose.isSelected())
			selectedCustomer.setSensitiveToLactose(true);
		if(selectedCustomer.isSensitiveToLactose() && !isLactose.isSelected())
			selectedCustomer.setSensitiveToLactose(false);
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Customer edited successfully!");
		alert.showAndWait();
		messageToUser.setText("");
		first_Name.clear();
		last_Name.clear();
		date.setValue(null);
		
		Toggle selectedGender =Gender_group.getSelectedToggle(); 
		if (selectedGender != null) {
			selectedGender.setSelected(false);	
		}
		neighberhoodsBox.getSelectionModel().clearSelection();
		isGluten.setSelected(false);
		isLactose.setSelected(false);
		allCustomersTable.getItems().clear();
		allCustomersTable.getItems().addAll(FXCollections.observableArrayList(
		Restaurant.getInstance().getCustomers().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
		editCustomerBtn.setDisable(true);
		addCustomerBtn.setDisable(false);
		date.setDisable(false);
		}catch(InvalidInputException inputE) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText(inputE.getMessage());
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
			Neighberhood neighberhood = neighberhoodsBox.getValue();
				
			if(neighberhood == null) {
				throw new InvalidPersonInputException("Please select Neighborhood");
			}
	
			boolean isSensitiveToLactose = isLactose.isSelected();
			boolean isSensitiveToGluten = isGluten.isSelected();
			///
			
			Customer newCustomer = new Customer(firstName, lastName, birthDate, gender, 
											neighberhood, isSensitiveToLactose, isSensitiveToGluten);
			
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
				allCustomersTable.getItems().clear();
				allCustomersTable.getItems().addAll(FXCollections.observableArrayList(
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
		Customer selectedCustomer = allCustomersTable.getSelectionModel().getSelectedItem();
		if(selectedCustomer !=  null) {
			Restaurant.getInstance().removeCustomer(selectedCustomer);
			//update the list after removal
			allCustomersTable.getItems().clear();
			allCustomersTable.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getCustomers().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
			//update the black list
			blackList.getItems().clear();
			blackList.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().getBlackList().stream().collect(Collectors.toList())));
		}
	}
	
	public void addCustomerToBlackList(ActionEvent e) {
		Customer selectedCustomer = allCustomersTable.getSelectionModel().getSelectedItem();
		if(selectedCustomer !=  null) {
			Restaurant.getInstance().addCustomerToBlackList(selectedCustomer);
			//update the black list
			blackList.getItems().clear();
			blackList.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().getBlackList().stream().collect(Collectors.toList())));
		}
	}
}
