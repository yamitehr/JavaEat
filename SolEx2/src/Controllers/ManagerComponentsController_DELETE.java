package Controllers;

import java.util.stream.Collectors;
import Model.Component;
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

public class ManagerComponentsController_DELETE extends ControllerWrapper {
	
	@FXML
	private Button addComponentBtn;
	@FXML
	private Button backBtn;
	@FXML
	private ListView<Component> allComponents;
	@FXML
	private Text componentNameField;
	@FXML
	private Text priceField;
	@FXML
	private Text sensitivitiesField;
	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		//Set the listview cell factory to show the right customer name
		allComponents.setCellFactory(param -> new ListCell<Component>() {
		    @Override
		    protected void updateItem(Component item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null) {
		            setText(null);
		        } else {
		            setText(item.getComponentName());
		        }
		    }
		});
				
		//Add all components
		allComponents.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getComponenets().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
		
		//Event listener for listview
		allComponents.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Component>() {
		    @Override
		    public void changed(ObservableValue<? extends Component> observable, Component oldValue, Component newValue) {
		    	updateComponentDetailsFields();
		    }
		});
	}
	
	
	public void MoveToAddComponentScene(ActionEvent e) {
		moveToScene("/View/AddComponent.fxml", (Stage)addComponentBtn.getScene().getWindow());
	}
	
	public void moveToManagerLandingPageScene(ActionEvent e) {
		moveToScene("/View/Manager_LandingPage.fxml", (Stage)backBtn.getScene().getWindow());
	}
	
	
	public void updateComponentDetailsFields() {
		Component selectedComponent = allComponents.getSelectionModel().getSelectedItem();
		if(selectedComponent != null) {
			componentNameField.setText(selectedComponent.getComponentName());
			priceField.setText(String.valueOf(selectedComponent.getPrice()));
		
			String sensitivities = "";
			if (selectedComponent.isHasGluten()) {
				sensitivities += "Gluten";
				if (selectedComponent.isHasLactose()) {
					sensitivities += ", Lactose";
				}
			} else if (selectedComponent.isHasLactose()) {
				sensitivities += "Lactose";
			}
			sensitivitiesField.setText(sensitivities);
		} else if(selectedComponent == null) {
			componentNameField.setText("");
			priceField.setText("");
			sensitivitiesField.setText("");
		}	
	}
	
	public void removeComponent(ActionEvent e) {
		Component selectedComponent = allComponents.getSelectionModel().getSelectedItem();
		if(selectedComponent !=  null) {
			Restaurant.getInstance().removeComponent(selectedComponent);
			//update the list after removal
			allComponents.getItems().clear();
			allComponents.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getComponenets().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
		}
	}


}
