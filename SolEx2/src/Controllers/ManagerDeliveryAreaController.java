package Controllers;

import java.util.stream.Collectors;

import Model.Cook;
import Model.DeliveryArea;
import Model.DeliveryPerson;
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

public class ManagerDeliveryAreaController extends ControllerWrapper {
	@FXML
	private Button addDeliveryAreaBtn;
	@FXML
	private Button removeDeliveryAreaBtn;
	@FXML
	private ListView<DeliveryArea> allDeliveryAreas;
	@FXML
	private Text areaNameField;
	@FXML
	private Text deliveryTimeField;
	@FXML
	private Text neighborhoodField;


	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		////////All delivery people list view
		//Set the listview cell factory to show the right delivery person name
		allDeliveryAreas.setCellFactory(param -> new ListCell<DeliveryArea>() {
		    @Override
		    protected void updateItem(DeliveryArea item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null) {
		            setText(null);
		        } else {
		            setText(item.getAreaName());
		        }
		    }
		});
				
		//Add all delivery people
		allDeliveryAreas.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getAreas().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
		
		//Event listener for listview
		allDeliveryAreas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DeliveryArea>() {
		    @Override
		    public void changed(ObservableValue<? extends DeliveryArea> observable, DeliveryArea oldValue, DeliveryArea newValue) {
		    	updateCustomerDetailsFields();
		    }
		});
	}

	public void MoveToAddDeliveryPersonScene(ActionEvent e) {
		moveToScene("/View/AddDeliveryArea.fxml", (Stage)addDeliveryAreaBtn.getScene().getWindow());
	}
	
	public void moveToManagerLandingPageScene(ActionEvent e) {
		moveToScene("/View/Manager_LandingPage.fxml", (Stage)addDeliveryAreaBtn.getScene().getWindow());
	}
	
	public void updateCustomerDetailsFields() {
		DeliveryArea selectedDeliveryArea = allDeliveryAreas.getSelectionModel().getSelectedItem();
		// fill text fields with values about the selected delivery area on the list
		if(selectedDeliveryArea != null) {
			areaNameField.setText(selectedDeliveryArea.getAreaName());
			deliveryTimeField.setText(String.valueOf(selectedDeliveryArea.getDeliverTime()));
			String neighborhoodsStr = "";
			if (selectedDeliveryArea.getNeighberhoods().size() > 0) {
				for(Neighberhood n : selectedDeliveryArea.getNeighberhoods()) {
					neighborhoodsStr += n.name() + ", ";
				}
				neighborhoodsStr = neighborhoodsStr.substring(0, neighborhoodsStr.length() - 3);
			}
			neighborhoodField.setText(neighborhoodsStr);
			
		//clean the text fields if there is no selection
		} else if(selectedDeliveryArea == null) {
			areaNameField.setText("");
			deliveryTimeField.setText("");
			neighborhoodField.setText("");
		}
	}
	
	//TODO: finish method
	public void removeDeliveryArea(ActionEvent e) {
		DeliveryArea selectedDeliveryArea = allDeliveryAreas.getSelectionModel().getSelectedItem();
		if(selectedDeliveryArea !=  null) {
		
		}
	}
}
