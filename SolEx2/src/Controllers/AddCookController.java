package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import Exceptions.InvalidInputException;
import Exceptions.InvalidInputException;
import Model.Cook;
import Model.DeliveryArea;
import Model.DeliveryPerson;
import Model.Dish;
import Model.Restaurant;
import Utils.Expertise;
import Utils.Gender;
import Utils.Vehicle;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class AddCookController extends ControllerWrapper{
	//cooks
	@FXML
	private ComboBox<Expertise> expertiseBox;
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
	private TextArea messageToUserCook;
	@FXML
	private Button addCookBtn;
	@FXML
	private Button removeCookBtn;
	@FXML
	private TableView<Cook> allCooksTable;
	@FXML
	private TableColumn<Cook, Integer> cookIdCol;
	@FXML
	private TableColumn<Cook, String> cookNameCol;
	@FXML
	private TableColumn<Cook, String> cookDobCol;
	@FXML
	private TableColumn<Cook, String> cookGenderCol;
	@FXML
	private TableColumn<Cook, String> isChefCol;
	@FXML
	private TableColumn<Cook, String> expertiseCol;
	@FXML
	private Button editCookBtn;
	@FXML
	private TabPane tabPane;
	@FXML
	private TextField searchCookField;
	@FXML
	private Text messageCook;
	
	
	//delivery person
	@FXML
	private TextField first_Name_DP;
	@FXML
	private TextField last_Name_DP;
	@FXML
	private ToggleGroup Gender_group_DP;
	@FXML
	private DatePicker dateDP;
	@FXML
	private ComboBox<Vehicle> vehicleBox;
	@FXML
	private ComboBox<DeliveryArea> deliveryAreaBox;
	@FXML
	private TextArea messageToUserDeliveryPerson;
	@FXML
	private Button addDeliveryPersonBtn;
	@FXML
	private Button removeDeliveryPersonBtn;
	@FXML
	private TableView<DeliveryPerson> allDeliveryPersonsTable;
	@FXML
	private TableColumn<DeliveryPerson, Integer> dpIdCol;
	@FXML
	private TableColumn<DeliveryPerson, String> dpNameCol;
	@FXML
	private TableColumn<DeliveryPerson, String> dpDobCol;
	@FXML
	private TableColumn<DeliveryPerson, String> dpGenderCol;
	@FXML
	private TableColumn<DeliveryPerson, String> vehicleCol;
	@FXML
	private TableColumn<DeliveryPerson, String> areaCol;
	@FXML
	private Button editDeliveryPersonBtn;
	@FXML
	private TextField searchDPField;
	@FXML
	private Text messageDP;
	
	@FXML
	private AnchorPane toReplacePane;
	

	@FXML
    public void initialize() {
		initCookTab();
		initDeliveryPersonTab();
    }
	
	private void initCookTab() {
		editCookBtn.setDisable(true);
		expertiseBox.getItems().clear();
		ObservableList<Expertise> expertise = FXCollections.observableArrayList(Expertise.values());
		expertiseBox.getItems().clear();				
		expertiseBox.setItems(FXCollections.observableArrayList(expertise));
		
		//Add all cooks
		cookIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));	
		
		cookNameCol.setCellValueFactory(cook -> new ReadOnlyObjectWrapper<String>(cook.getValue().getFirstName() + " " + cook.getValue().getLastName()));	
		
		cookDobCol.setCellValueFactory(cook -> new ReadOnlyObjectWrapper<String>(cook.getValue().getBirthDay().toString()));
		
		cookGenderCol.setCellValueFactory(cook -> new ReadOnlyObjectWrapper<String>(cook.getValue().getGender().name()));
		
		expertiseCol.setCellValueFactory(cook -> new ReadOnlyObjectWrapper<String>(cook.getValue().getExpert().toString()));
		
		isChefCol.setCellValueFactory(cook -> {
            boolean isChef = cook.getValue().isChef();
            String isChefAsString;
            if(isChef == true)
            {
                isChefAsString = "Yes";
            }
            else
            {
                isChefAsString = "No";
            }

         return new ReadOnlyStringWrapper(isChefAsString);
        });
		
		List<Cook> allCooks = new ArrayList<Cook>();
		allCooks = Restaurant.getInstance().getCooks().values().stream()
				.collect(Collectors.toList());
		
		allCooksTable.getItems().addAll(allCooks);
		
		 searchCookField.textProperty().addListener((observable, oldValue, newValue) -> {
			 searchCookByID();
			});	
		 
		 first_Name.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					    	if (newValue != "") {
						    	messageCook.setText("");
						    	if(!newValue.matches("[a-zA-Z\s]+")) {
						    		first_Name.setText(newValue.substring(0, newValue.length()-1));
						    		messageCook.setText("Letters Only!");
						    	}
					    	}
					    }
					});
		 
		 last_Name.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					    	if (newValue != "") {
						    	messageCook.setText("");
						    	if(!newValue.matches("[a-zA-Z\s]+")) {
						    		last_Name.setText(newValue.substring(0, newValue.length()-1));
						    		messageCook.setText("Letters Only!");
						    	}
					    	}
					    }
					});
		 
		 date.setOnAction(d -> {
			 if(date.getValue() != null) {
				 messageCook.setText("");
				 if(date.getValue().isAfter(LocalDate.now())) {
					 messageCook.setText("Date cannot be in the future!");
				 }
			 }
		 });
	}
	
	private void searchCookByID() {
		String keyword = searchCookField.getText();
		ObservableList<Cook> filteredData = FXCollections.observableArrayList();
		  if (keyword.isEmpty()) {
			  filteredData.addAll(Restaurant.getInstance().getCooks().values());
			  allCooksTable.setItems(filteredData);
		  }
		  else {
			  Cook cook = Restaurant.getInstance().getRealCook(Integer.parseInt(searchCookField.getText()));
			  if(cook != null)
				  filteredData.add(cook);
		     allCooksTable.setItems(filteredData);
		  }
		
	}

	private void initDeliveryPersonTab() {
		editDeliveryPersonBtn.setDisable(true);	
		vehicleBox.getItems().clear();
		ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(Vehicle.values());
		vehicleBox.getItems().clear();				
		vehicleBox.setItems(FXCollections.observableArrayList(vehicles));
		
		//add areas list
		//Set the cell factory to show the delivery areas
		deliveryAreaBox.setCellFactory(param -> new ListCell<DeliveryArea>() {
				   @Override
				   protected void updateItem(DeliveryArea item, boolean empty) {
				       super.updateItem(item, empty);

				       if (empty || item == null) {
				           setText(null);
				       } else {
				           setText(item.getAreaName() + " (id= " + item.getId() + ")");
				       }
				   }
			});
						
			//Add all areas
			deliveryAreaBox.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().getAreas().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
			
			
					
			//Add all delivery people
			dpIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));	
			
			dpNameCol.setCellValueFactory(dp -> new ReadOnlyObjectWrapper<String>(dp.getValue().getFirstName() + " " + dp.getValue().getLastName()));	
			
			dpDobCol.setCellValueFactory(dp -> new ReadOnlyObjectWrapper<String>(dp.getValue().getBirthDay().toString()));
			
			dpGenderCol.setCellValueFactory(dp -> new ReadOnlyObjectWrapper<String>(dp.getValue().getGender().name()));
			
			vehicleCol.setCellValueFactory(dp -> new ReadOnlyObjectWrapper<String>(dp.getValue().getVehicle().toString()));
			
			areaCol.setCellValueFactory(dp -> new ReadOnlyObjectWrapper<String>(dp.getValue().getArea().toString()));
			
			List<DeliveryPerson> allDeliveryPersons = new ArrayList<DeliveryPerson>();
			allDeliveryPersons = Restaurant.getInstance().getDeliveryPersons().values().stream()
					.collect(Collectors.toList());
			
			allDeliveryPersonsTable.getItems().addAll(allDeliveryPersons);
			
			 searchDPField.textProperty().addListener((observable, oldValue, newValue) -> {
				 searchDPByID();
				});
			 
			 first_Name_DP.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, 
							String newValue) {
						    	if (newValue != "") {
							    	messageDP.setText("");
							    	if(!newValue.matches("[a-zA-Z\s]+")) {
							    		first_Name_DP.setText(newValue.substring(0, newValue.length()-1));
							    		messageDP.setText("Letters Only!");
							    	}
						    	}
						    }
						});
			 
			 last_Name_DP.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, 
							String newValue) {
						    	if (newValue != "") {
						    		messageDP.setText("");
							    	if(!newValue.matches("[a-zA-Z\s]+")) {
							    		last_Name_DP.setText(newValue.substring(0, newValue.length()-1));
							    		messageDP.setText("Letters Only!");
							    	}
						    	}
						    }
						});
			 
			 dateDP.setOnAction(d -> {
				 if(dateDP.getValue() != null) {
					 messageDP.setText("");
					 if(dateDP.getValue().isAfter(LocalDate.now())) {
						 messageDP.setText("Date cannot be in the future!");
					 }
				 }
			 });
	}
	
	private void searchDPByID() {
		String keyword = searchDPField.getText();
		ObservableList<DeliveryPerson> filteredData = FXCollections.observableArrayList();
		  if (keyword.isEmpty()) {
			  filteredData.addAll(Restaurant.getInstance().getDeliveryPersons().values());
			  allDeliveryPersonsTable.setItems(filteredData);
		  }
		  else {
			  DeliveryPerson dp = Restaurant.getInstance().getRealDeliveryPerson(Integer.parseInt(searchDPField.getText()));
			  if(dp != null)
				  filteredData.add(dp);
		     allDeliveryPersonsTable.setItems(filteredData);
		  }
		
	}

	public void editCook(ActionEvent e) {
		Cook selectedCook = allCooksTable.getSelectionModel().getSelectedItem();
		if(selectedCook !=  null) {
			addCookBtn.setDisable(true);
			editCookBtn.setDisable(false);
			first_Name.setText(selectedCook.getFirstName());
			last_Name.setText(selectedCook.getLastName());
			date.setValue(selectedCook.getBirthDay());
			if(selectedCook.getGender().equals(Gender.Male))
				Gender_group.getToggles().get(0).setSelected(true);
			if(selectedCook.getGender().equals(Gender.Female))
				Gender_group.getToggles().get(1).setSelected(true);
			if(selectedCook.getGender().equals(Gender.Unknown))
				Gender_group.getToggles().get(2).setSelected(true);
			expertiseBox.setValue(selectedCook.getExpert());
			isChef.setSelected(selectedCook.isChef());	
			if(isChef.isSelected())
				isChef.setDisable(true);
			
			date.setDisable(true);
			date.setEditable(false);
		}
	}
	
	public void setEditCook(ActionEvent e) {
		Cook selectedCook = allCooksTable.getSelectionModel().getSelectedItem();
		try {
			if(!selectedCook.getFirstName().equals(first_Name.getText())) {
				if(first_Name.getText().isEmpty())
					throw new InvalidInputException("First Name cannot be empty");
				selectedCook.setFirstName(first_Name.getText());
			}
			if(!selectedCook.getLastName().equals(last_Name.getText())) {
				if(last_Name.getText().isEmpty())
					throw new InvalidInputException("Last Name cannot be empty");
				selectedCook.setLastName(last_Name.getText());
			}
			Gender gender = null;
			//get gender
			RadioButton selectedRadioButton = (RadioButton) Gender_group.getSelectedToggle();
			String toogleGroupValue = selectedRadioButton.getText();
			for(Gender g : Gender.values()) {
				if(g.name().equals(toogleGroupValue)) {
					gender = g;
				}
			}
			selectedCook.setGender(gender);
				
		if(!selectedCook.getExpert().equals(expertiseBox.getValue())) {
			if(expertiseBox.getValue() == null)
				throw new InvalidInputException("you must choose Expert");
			selectedCook.setExpert(expertiseBox.getValue());
		}
		if(!selectedCook.isChef() && isChef.isSelected())
			selectedCook.setChef(true);
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Cook edited successfully!");
		alert.showAndWait();
		messageToUserCook.setText("");
		first_Name.clear();
		last_Name.clear();
		date.setValue(null);
		Gender_group.getSelectedToggle().setSelected(false);
		expertiseBox.getSelectionModel().clearSelection();
		isChef.setSelected(false);
		allCooksTable.getItems().clear();
		allCooksTable.getItems().addAll(FXCollections.observableArrayList(
		Restaurant.getInstance().getCooks().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
		editCookBtn.setDisable(true);
		addCookBtn.setDisable(false);
		date.setDisable(false);
		isChef.setDisable(false);
		}catch(InvalidInputException inputE) {
			messageToUserCook.setText(inputE.getMessage());
		}
	}
	
	
	//Adds the cook to the restaurant
	public void addCook(ActionEvent e) {
		
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
			
			//get Expertise
			Expertise expertise = null;
			for(Expertise expert : Expertise.values()) {
				if(expert.equals(expertiseBox.getValue())) {
					expertise = expert;
				}
			}
			if(expertise == null) {
				throw new InvalidInputException("Please select Expertise");
			}
	
			boolean isChefChoice = isChef.isSelected();
			///
			
			Cook newCook = new Cook(firstName, lastName, birthDate, gender, 
											expertise, isChefChoice);
			
			//add customer to the restaurant
			if(Restaurant.getInstance().addCook(newCook)) {
				messageToUserCook.setText("Cook added successfully");
				first_Name.clear();
				last_Name.clear();
				Gender_group.getSelectedToggle().setSelected(false);
				expertiseBox.getSelectionModel().clearSelection();
				date.setValue(null);
				isChef.setSelected(false);
				allCooksTable.getItems().clear();
				allCooksTable.getItems().addAll(FXCollections.observableArrayList(
						Restaurant.getInstance().getCooks().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
			}else {
				messageToUserCook.setText("an error has accured please try again");
			}
		} catch(InvalidInputException ipe) {
			messageToUserCook.setText(ipe.getMessage());
		} catch(Exception exc) {
			messageToUserCook.setText("an error has accured please try again");
		}
	}
	
	public void removeCook(ActionEvent e) {
		Cook selectedCook = allCooksTable.getSelectionModel().getSelectedItem();
		if(selectedCook !=  null) {
			Restaurant.getInstance().removeCook(selectedCook);
			//update the list after removal
			allCooksTable.getItems().clear();
			allCooksTable.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().getCooks().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
		}
	}
	
	
	public void editDeliveryPerson(ActionEvent e) {
		DeliveryPerson selectedDeliveryPerson = allDeliveryPersonsTable.getSelectionModel().getSelectedItem();
		if(selectedDeliveryPerson !=  null) {
			addDeliveryPersonBtn.setDisable(true);
			editDeliveryPersonBtn.setDisable(false);
			first_Name_DP.setText(selectedDeliveryPerson.getFirstName());
			last_Name_DP.setText(selectedDeliveryPerson.getLastName());
			dateDP.setValue(selectedDeliveryPerson.getBirthDay());
			if(selectedDeliveryPerson.getGender().equals(Gender.Male))
				Gender_group_DP.getToggles().get(0).setSelected(true);
			if(selectedDeliveryPerson.getGender().equals(Gender.Female))
				Gender_group_DP.getToggles().get(1).setSelected(true);
			if(selectedDeliveryPerson.getGender().equals(Gender.Unknown))
				Gender_group_DP.getToggles().get(2).setSelected(true);
			vehicleBox.setValue(selectedDeliveryPerson.getVehicle());
			deliveryAreaBox.setValue(selectedDeliveryPerson.getArea());	
			
			dateDP.setDisable(true);
			dateDP.setEditable(false);
		}
	}
	
	public void setEditDeliveryPerson(ActionEvent e) {
		DeliveryPerson selectedDeliveryPerson = allDeliveryPersonsTable.getSelectionModel().getSelectedItem();
		try {
			if(!selectedDeliveryPerson.getFirstName().equals(first_Name_DP.getText())) {
				if(first_Name_DP.getText().isEmpty())
					throw new InvalidInputException("First Name cannot be empty");
				selectedDeliveryPerson.setFirstName(first_Name_DP.getText());
			}
			if(!selectedDeliveryPerson.getLastName().equals(last_Name_DP.getText())) {
				if(last_Name_DP.getText().isEmpty())
					throw new InvalidInputException("Last Name cannot be empty");
				selectedDeliveryPerson.setLastName(last_Name_DP.getText());
			}
			Gender gender = null;
			//get gender
			RadioButton selectedRadioButton = (RadioButton) Gender_group.getSelectedToggle();
			String toogleGroupValue = selectedRadioButton.getText();
			for(Gender g : Gender.values()) {
				if(g.name().equals(toogleGroupValue)) {
					gender = g;
				}
			}
			selectedDeliveryPerson.setGender(gender);
				
		if(!selectedDeliveryPerson.getVehicle().equals(vehicleBox.getValue())) {
			if(vehicleBox.getValue() == null)
				throw new InvalidInputException("you must choose Vehicle");
			selectedDeliveryPerson.setVehicle(vehicleBox.getValue());
		}
		if(!selectedDeliveryPerson.getArea().equals(deliveryAreaBox.getValue())) {
				if(deliveryAreaBox.getValue() == null)
					throw new InvalidInputException("You must choose delivery area");
				selectedDeliveryPerson.setArea(deliveryAreaBox.getValue());
		}
				
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("DeliveryPerson edited successfully!");
		alert.showAndWait();
		messageToUserDeliveryPerson.setText("");
		first_Name_DP.clear();
		last_Name_DP.clear();
		dateDP.setValue(null);
		Gender_group_DP.getSelectedToggle().setSelected(false);
		vehicleBox.getSelectionModel().clearSelection();
		deliveryAreaBox.getSelectionModel().clearSelection();
		allDeliveryPersonsTable.getItems().clear();
		allDeliveryPersonsTable.getItems().addAll(FXCollections.observableArrayList(
		Restaurant.getInstance().getDeliveryPersons().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
		editDeliveryPersonBtn.setDisable(true);
		addDeliveryPersonBtn.setDisable(false);
		date.setDisable(false);
		}catch(InvalidInputException inputE) {
			messageToUserDeliveryPerson.setText(inputE.getMessage());
		}
	}
	
	
	
	
	//Adds the delivery person to the restaurant
		public void addDeliveryPerson(ActionEvent e) {
			
			try {
				
				String firstName = first_Name_DP.getText();
				if(firstName.isEmpty()) {
					throw new InvalidInputException("Please fill First Name");
				}
				
				String lastName = last_Name_DP.getText();
				if(lastName.isEmpty()) {
					throw new InvalidInputException("Please fill Last Name");
				}
				
				//get DOB
				LocalDate birthDate;
				birthDate = dateDP.getValue();
				if(birthDate == null) {
					throw new InvalidInputException("Please select Date of Birth");
				}
				if(birthDate.isAfter(LocalDate.now())) {
					throw new InvalidInputException("Date of birth can't be in the future");
				}
				
				Gender gender = null;
				//get gender
				try {
					RadioButton selectedRadioButton = (RadioButton) Gender_group_DP.getSelectedToggle();
					String toogleGroupValue = selectedRadioButton.getText();
					for(Gender g : Gender.values()) {
						if(g.name().equals(toogleGroupValue)) {
							gender = g;
						}
					}
				} catch(Exception exc) {
					throw new InvalidInputException("Please fill Gender");
				}
				
				//get Vehicle
				Vehicle vehicle = null;
				for(Vehicle v : Vehicle.values()) {
					if(v.equals(vehicleBox.getValue())) {
						vehicle = v;
					}
				}
				if(vehicle == null) {
					throw new InvalidInputException("Please select Vehicle");
				}
		
				DeliveryArea selectedDeliveryArea = deliveryAreaBox.getSelectionModel().getSelectedItem();
				if(selectedDeliveryArea == null) {
					throw new InvalidInputException("Please select Delivery Area");
				}
				
				
				///
				
				DeliveryPerson newDeliveryPerson = new DeliveryPerson(firstName, lastName, birthDate, gender, 
												vehicle, selectedDeliveryArea);
				
				//add deliveryPerson to the restaurant
				if(Restaurant.getInstance().addDeliveryPerson(newDeliveryPerson, selectedDeliveryArea)) {
					messageToUserDeliveryPerson.setText("Delivery Person added successfully");
					first_Name_DP.clear();
					last_Name_DP.clear();
					Gender_group_DP.getSelectedToggle().setSelected(false);
					vehicleBox.getSelectionModel().clearSelection();
					dateDP.setValue(null);
					allDeliveryPersonsTable.getItems().clear();
					allDeliveryPersonsTable.getItems().addAll(FXCollections.observableArrayList(
							Restaurant.getInstance().getDeliveryPersons().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
				}else {
					messageToUserDeliveryPerson.setText("an error has accured please try again");
				}
				
			} catch(InvalidInputException ipe) {
				messageToUserDeliveryPerson.setText(ipe.getMessage());
			} catch(Exception exc) {
				messageToUserDeliveryPerson.setText("an error has accured please try again");
			}
		}
		
		public void removeDeliveryPerson(ActionEvent e) {
			DeliveryPerson selectedDeliveryPerson = allDeliveryPersonsTable.getSelectionModel().getSelectedItem();
			if(selectedDeliveryPerson !=  null) {
				Restaurant.getInstance().removeDeliveryPerson(selectedDeliveryPerson);
				//update the list after removal
				allDeliveryPersonsTable.getItems().clear();
				allDeliveryPersonsTable.getItems().addAll(FXCollections.observableArrayList(
						Restaurant.getInstance().getDeliveryPersons().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
			}
		}
}
