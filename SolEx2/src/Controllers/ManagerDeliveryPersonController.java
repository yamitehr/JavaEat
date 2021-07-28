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
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ManagerDeliveryPersonController extends ControllerWrapper {
	@FXML
	private Button addDeliveryPersonBtn;
	@FXML
	private Button removeDeliveryPersonBtn;
	@FXML
	private ListView<DeliveryPerson> allDeliveryPeople;
	@FXML
	private Text firstNameField;
	@FXML
	private Text lastNameField;
	@FXML
	private Text dobField;
	@FXML
	private Text vehicleField;
	@FXML
	private Text genderField;
	@FXML
	private Text deliveryAreaField;


	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
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
		    	updateCustomerDetailsFields();
		    }
		});
	}

	public void MoveToAddDeliveryPersonScene(ActionEvent e) {
		moveToScene("/View/AddDeliveryPerson.fxml", (Stage)addDeliveryPersonBtn.getScene().getWindow());
	}
	
	public void moveToManagerLandingPageScene(ActionEvent e) {
		moveToScene("/View/Manager_LandingPage.fxml", (Stage)addDeliveryPersonBtn.getScene().getWindow());
	}
	
	public void updateCustomerDetailsFields() {
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
