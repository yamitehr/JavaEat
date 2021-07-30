package Controllers;

import java.util.stream.Collectors;

import Model.Cook;
import Model.DeliveryPerson;
import Model.Restaurant;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ManagerCookController_DELETE extends ControllerWrapper {
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
	
	//delivery persons
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
		initCooks();
		initDeliveryPerson();
    }
	
	private void initCooks() {
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
	}
	
	private void initDeliveryPerson() {
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


	public void MoveToAddCookScene(ActionEvent e) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddCook.fxml"));
		AnchorPane pane = loader.load();
		toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
		toReplacePane.getChildren().add(pane);
	}
	
	public void MoveToAddDeliveryPersonScene(ActionEvent e) {
		moveToScene("/View/AddDeliveryPerson.fxml", (Stage)addDeliveryPersonBtn.getScene().getWindow());
	}
	
	/*public void moveToManagerLandingPageScene(ActionEvent e) {
		moveToScene("/View/Manager_LandingPage.fxml", (Stage)addCookBtn.getScene().getWindow());
	}*/
	
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
