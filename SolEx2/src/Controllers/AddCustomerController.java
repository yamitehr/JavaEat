package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import Exceptions.InvalidInputException;
import Model.Customer;
import Model.Restaurant;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

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
	private TextArea messageToUser;
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
	private TableColumn<Customer, LocalDate> customerDobCol;
	@FXML
	private TableColumn<Customer, Gender> customerGenderCol;
	@FXML
	private TableColumn<Customer, Neighberhood> neighberhoodCol;
	@FXML
	private TableColumn<Customer, String> sensitivitiesCol;
	@FXML
	private TableColumn<Customer, String> userNameCol;
	@FXML
	private TableColumn<Customer, String> passwordCol;
	@FXML
	private ListView<Customer> blackList;
	@FXML
	private TextField searchCustomerField;
	

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
		
		customerDobCol.setCellValueFactory(cust -> new ReadOnlyObjectWrapper<LocalDate>(cust.getValue().getBirthDay()));
		
		customerGenderCol.setCellValueFactory(cust -> new ReadOnlyObjectWrapper<Gender>(cust.getValue().getGender()));
		
		neighberhoodCol.setCellValueFactory(cust -> new ReadOnlyObjectWrapper<Neighberhood>(cust.getValue().getNeighberhood()));
		
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
		
		userNameCol.setCellValueFactory(cust -> new ReadOnlyObjectWrapper<String>(cust.getValue().getUserName()));
		
		passwordCol.setCellValueFactory(cust -> new ReadOnlyObjectWrapper<String>(cust.getValue().getPassword()));
		
		List<Customer> allCustomers = new ArrayList<Customer>();
		allCustomers = Restaurant.getInstance().getCustomers().values().stream()
				.collect(Collectors.toList());
		
		allCustomersTable.getItems().addAll(allCustomers);
		
		searchCustomerField.textProperty().addListener((observable, oldValue, newValue) -> {
			 searchCustomerByID();
			});
		
		first_Name.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				    	if (newValue != "") {
				    		messageToUser.setText("");
					    	if(!newValue.matches("[a-zA-Z\s]+")) {
					    		first_Name.setText(newValue.substring(0, newValue.length()-1));
					    		messageToUser.setText("Letters Only!");
					    	}
				    	}
				    }
				});
	 
		last_Name.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				    	if (newValue != "") {
				    		messageToUser.setText("");
					    	if(!newValue.matches("[a-zA-Z\s]+")) {
					    		last_Name.setText(newValue.substring(0, newValue.length()-1));
					    		messageToUser.setText("Letters Only!");
					    	}
				    	}
				    }
				});
	 
		date.setOnAction(d -> {
		 if(date.getValue() != null) {
			 messageToUser.setText("");
			 if(date.getValue().isAfter(LocalDate.now())) {
				 messageToUser.setText("Date cannot be in the future!");
			 }
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
		            setText(item.getId() + " " + item.getFirstName() + " " + item.getLastName());
		        }
		    }
		});
		
		//Add the black list customers
		blackList.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getBlackList().stream().collect(Collectors.toList())));
	}
	
	private void searchCustomerByID() {
		String keyword = searchCustomerField.getText();
		ObservableList<Customer> filteredData = FXCollections.observableArrayList();
		  if (keyword.isEmpty()) {
			  filteredData.addAll(Restaurant.getInstance().getCustomers().values());
			  allCustomersTable.setItems(filteredData);
		  }
		  else {
			  Customer cust = Restaurant.getInstance().getRealCustomer(Integer.parseInt(searchCustomerField.getText()));
			  if(cust != null)
				  filteredData.add(cust);
			  allCustomersTable.setItems(filteredData);
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
			if(selectedCustomer.getGender().equals(Gender.Male))
				Gender_group.getToggles().get(0).setSelected(true);
			if(selectedCustomer.getGender().equals(Gender.Female))
				Gender_group.getToggles().get(1).setSelected(true);
			if(selectedCustomer.getGender().equals(Gender.Unknown))
				Gender_group.getToggles().get(2).setSelected(true);
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
					throw new InvalidInputException("Please fill First Name");
				selectedCustomer.setFirstName(first_Name.getText());
			}
			if(!selectedCustomer.getLastName().equals(last_Name.getText())) {
				if(last_Name.getText().isEmpty())
					throw new InvalidInputException("Please fill Last Name");
				selectedCustomer.setLastName(last_Name.getText());
			}
			try {
				Gender gender = null;
				//get gender
				RadioButton selectedRadioButton = (RadioButton) Gender_group.getSelectedToggle();
				String toogleGroupValue = selectedRadioButton.getText();
				for(Gender g : Gender.values()) {
					if(g.name().equals(toogleGroupValue)) {
						gender = g;
					}
				}
			selectedCustomer.setGender(gender);
		}catch(Exception exc) {
			throw new InvalidInputException("Please fill Gender");
		}
				
		if(!selectedCustomer.getNeighberhood().equals(neighberhoodsBox.getValue())) {
			if(neighberhoodsBox.getValue() == null)
				throw new InvalidInputException("Please choose Neighberhood");
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
		
		Toggle selectedGender = Gender_group.getSelectedToggle(); 
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
			soundOfButton("error.mp3");
			messageToUser.setText(inputE.getMessage());
		}
	}
	
	//Adds the customer to the restaurant
	public void addCustomer(ActionEvent e) {
		
		try {
			
			String firstName = first_Name.getText();
			if(firstName.isEmpty()) {
				throw new InvalidInputException("Please fill First Name");
			}
			
			String lastName = last_Name.getText();
			if(lastName.isEmpty()) {
				throw new InvalidInputException("Please fill Last Name");
			}
			
			//get DOB
			LocalDate birthDate;
			birthDate = date.getValue();
			if(birthDate == null) {
				throw new InvalidInputException("Please select Date of Birth");
			}
			if(birthDate.isAfter(LocalDate.now())) {
				throw new InvalidInputException("Date of birth can't be in the future");
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
				throw new InvalidInputException("Please fill Gender");
			}
			
			//get Neighborhood
			Neighberhood neighberhood = neighberhoodsBox.getValue();
				
			if(neighberhood == null) {
				throw new InvalidInputException("Please select Neighborhood");
			}
	
			boolean isSensitiveToLactose = isLactose.isSelected();
			boolean isSensitiveToGluten = isGluten.isSelected();
			///
			
			Customer newCustomer = new Customer(firstName, lastName, birthDate, gender, 
											neighberhood, isSensitiveToLactose, isSensitiveToGluten);
			
			//add customer to the restaurant
			if(Restaurant.getInstance().addCustomer(newCustomer)) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setHeaderText("Customer added successfully!");
				alert.showAndWait();
				messageToUser.setText("");
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
				soundOfButton("error.mp3");
				messageToUser.setText("an error has accured please try again");
			}
			
		}  catch(InvalidInputException ipe) {
			soundOfButton("error.mp3");
			messageToUser.setText(ipe.getMessage());
		} catch(Exception exc) {
			soundOfButton("error.mp3");
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
