package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import Exceptions.InvalidPersonInputException;
import Model.Cook;
import Model.DeliveryArea;
import Model.DeliveryPerson;
import Model.Restaurant;
import Utils.Expertise;
import Utils.Gender;
import Utils.Vehicle;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddCookController extends ControllerWrapper{
	//cooks
	@FXML
	private ComboBox<String> expertiseBox;
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
	private Text messageToUserCook;
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
	
	
	//delivery person
	@FXML
	private ComboBox<String> vehicleBox;
	@FXML
	private ComboBox<DeliveryArea> deliveryAreaBox;
	@FXML
	private Text messageToUserDeliveryPerson;
	@FXML
	private Button addDeliveryPersonBtn;
	@FXML
	private Button removeDeliveryPersonBtn;
	@FXML
	private ListView<DeliveryPerson> allDeliveryPeople;
	@FXML
	private Text vehicleField;
	@FXML
	private Text deliveryAreaField;
	
	@FXML
	private AnchorPane toReplacePane;
	

	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		expertiseBox.getItems().clear();
		
		ArrayList<String> expertiseNames = new ArrayList<String>();
		
		for(Expertise expert : Expertise.values()) {
			expertiseNames.add(expert.toString());
		}
		
		expertiseBox.getItems().addAll(FXCollections.observableArrayList(expertiseNames));
		
		vehicleBox.getItems().clear();
		
		ArrayList<String> vehicleNames = new ArrayList<String>();
		
		for(Vehicle v : Vehicle.values()) {
			vehicleNames.add(v.toString());
		}
		
		vehicleBox.getItems().addAll(FXCollections.observableArrayList(vehicleNames));
		
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
			    	updateCookDetailsFields();
			    }
			});
			
			
	////////All delivery people list view
			//Set the listview cell factory to show the right delivery person name
			allDeliveryPeople.setCellFactory(param -> new ListCell<DeliveryPerson>() {
			    @Override
			    protected void updateItem(DeliveryPerson item, boolean empty) {
			        super.updateItem(item, empty);

			        if (empty || item == null) {
			            setText(null);
			        } else {
			            setText(item.getFirstName() + " " + item.getLastName());
			        }
			    }
			});
					
			//Add all delivery people
			allDeliveryPeople.getItems().addAll(FXCollections.observableArrayList(
					Restaurant.getInstance().getDeliveryPersons().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
			
			//Event listener for listview
			allDeliveryPeople.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DeliveryPerson>() {
			    @Override
			    public void changed(ObservableValue<? extends DeliveryPerson> observable, DeliveryPerson oldValue, DeliveryPerson newValue) {
			    	updateDeliveryPersonDetailsFields();
			    }
			});
	}
	
	public void moveToManagerCooksScene(ActionEvent e) {
		moveToScene("/View/Manager_Cooks.fxml", (Stage)first_Name.getScene().getWindow());
	}
	
	
	
	public void updateCookDetailsFields() {
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
	
	
	public void updateDeliveryPersonDetailsFields() {
		DeliveryPerson selectedDeliveryPerson = allDeliveryPeople.getSelectionModel().getSelectedItem();
		// fill text fields with values about the selected delivery person on the list
		if(selectedDeliveryPerson != null) {
			firstNameField.setText(selectedDeliveryPerson.getFirstName());
			lastNameField.setText(selectedDeliveryPerson.getLastName());
			dobField.setText(selectedDeliveryPerson.getBirthDay().toString());
			vehicleField.setText(selectedDeliveryPerson.getVehicle().name());
			genderField.setText(selectedDeliveryPerson.getGender().name());
			deliveryAreaField.setText(selectedDeliveryPerson.getArea().getAreaName());
			
		//clean the text fields if there is no selection
		} else if(selectedDeliveryPerson == null) {
			firstNameField.setText("");
			lastNameField.setText("");
			dobField.setText("");
			vehicleField.setText("");
			genderField.setText("");
			deliveryAreaField.setText("");
		}
	}
	//Adds the cook to the restaurant
	public void addCook(ActionEvent e) {
		
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
			Expertise expertise = null;
			for(Expertise expert : Expertise.values()) {
				if(expert.name().equals(expertiseBox.getValue())) {
					expertise = expert;
				}
			}
			if(expertise == null) {
				throw new InvalidPersonInputException("Please select Neighborhood");
			}
	
			boolean isChefChoice = isChef.isSelected();
			///
			
			Cook newCook = new Cook(firstName, lastName, birthDate, gender, 
											expertise, isChefChoice);
			
			//add customer to the restaurant
			if(Restaurant.getInstance().addCook(newCook)) {
				messageToUserCook.setFill(Color.BLUE);
				messageToUserCook.setText("Cook added successfully");
				first_Name.clear();
				last_Name.clear();
				Gender_group.getSelectedToggle().setSelected(false);
				expertiseBox.getSelectionModel().clearSelection();
				date.setValue(null);
				isChef.setSelected(false);
			}else {
				messageToUserCook.setFill(Color.RED);
				messageToUserCook.setText("an error has accured please try again");
			}
			
		} catch(InvalidPersonInputException ipe) {
			messageToUserCook.setFill(Color.RED);
			messageToUserCook.setText(ipe.getMessage());
		} catch(Exception exc) {
			messageToUserCook.setFill(Color.RED);
			messageToUserCook.setText("an error has accured please try again");
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
	
	
	
	//Adds the delivery person to the restaurant
		public void addDeliveryPerson(ActionEvent e) {
			
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
				
				//get Vehicle
				Vehicle vehicle = null;
				for(Vehicle v : Vehicle.values()) {
					if(v.name().equals(vehicleBox.getValue())) {
						vehicle = v;
					}
				}
				if(vehicle == null) {
					throw new InvalidPersonInputException("Please select Vehicle");
				}
		
				DeliveryArea selectedDeliveryArea = deliveryAreaBox.getSelectionModel().getSelectedItem();
				if(selectedDeliveryArea == null) {
					throw new InvalidPersonInputException("Please select Delivery Area");
				}
				
				
				///
				
				DeliveryPerson newDeliveryPerson = new DeliveryPerson(firstName, lastName, birthDate, gender, 
												vehicle, selectedDeliveryArea);
				
				//add deliveryPerson to the restaurant
				if(Restaurant.getInstance().addDeliveryPerson(newDeliveryPerson, selectedDeliveryArea)) {
					messageToUserDeliveryPerson.setFill(Color.BLUE);
					messageToUserDeliveryPerson.setText("Cook added successfully");
					first_Name.clear();
					last_Name.clear();
					Gender_group.getSelectedToggle().setSelected(false);
					vehicleBox.getSelectionModel().clearSelection();
					date.setValue(null);
				}else {
					messageToUserDeliveryPerson.setFill(Color.RED);
					messageToUserDeliveryPerson.setText("an error has accured please try again");
				}
				
			} catch(InvalidPersonInputException ipe) {
				messageToUserDeliveryPerson.setFill(Color.RED);
				messageToUserDeliveryPerson.setText(ipe.getMessage());
			} catch(Exception exc) {
				messageToUserDeliveryPerson.setFill(Color.RED);
				messageToUserDeliveryPerson.setText("an error has accured please try again");
			}
		}
		
		public void removeDeliveryPerson(ActionEvent e) {
			DeliveryPerson selectedDeliveryPerson = allDeliveryPeople.getSelectionModel().getSelectedItem();
			if(selectedDeliveryPerson !=  null) {
				Restaurant.getInstance().removeDeliveryPerson(selectedDeliveryPerson);
				//update the list after removal
				allDeliveryPeople.getItems().clear();
				allDeliveryPeople.getItems().addAll(FXCollections.observableArrayList(
						Restaurant.getInstance().getDeliveryPersons().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
			}
		}
}
