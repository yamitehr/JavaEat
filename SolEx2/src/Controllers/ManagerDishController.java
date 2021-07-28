package Controllers;

import java.util.stream.Collectors;

import Model.Component;
import Model.Dish;
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

public class ManagerDishController extends ControllerWrapper {
	
	@FXML
	private ListView<Dish> allDishes;
	@FXML
	private Button addDishBtn;
	@FXML
	private Button backBtn;
	@FXML
	private Text DishNameField;
	@FXML
	private Text dishTypeField;
	@FXML
	private Text componentField;
	@FXML
	private Text timeToMakeField;
	@FXML
	private Text priceField;
	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		////////All cooks list view
		//Set the listview cell factory to show the right cook name
		allDishes.setCellFactory(param -> new ListCell<Dish>() {
		    @Override
		    protected void updateItem(Dish item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null) {
		            setText(null);
		        } else {
		            setText(item.getDishName());
		        }
		    }
		});
				
		//Add all cooks
		allDishes.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getDishes().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
		
		//Event listener for listview
		allDishes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Dish>() {
		    @Override
		    public void changed(ObservableValue<? extends Dish> observable, Dish oldValue, Dish newValue) {
		    	updateDishDetailsFields();
		    }
		});
	}
	
	public void MoveToAddDishScene(ActionEvent e) {
		moveToScene("/View/AddDish.fxml", (Stage)addDishBtn.getScene().getWindow());
	}
	
	public void moveToManagerLandingPageScene(ActionEvent e) {
		moveToScene("/View/Manager_LandingPage.fxml", (Stage)backBtn.getScene().getWindow());
	}
	
	public void updateDishDetailsFields() {
		Dish selectedDish = allDishes.getSelectionModel().getSelectedItem();
		// fill text fields with values about the selected customer on the list
		if(selectedDish != null) {
			DishNameField.setText(selectedDish.getDishName());
			dishTypeField.setText(selectedDish.getType().name());
			componentField.setText(selectedDish.getComponenets().toString());
			timeToMakeField.setText(String.valueOf(selectedDish.getTimeToMake()));
			priceField.setText(String.valueOf(selectedDish.getPrice()));
			
		//clean the text fields if there is no selection
		} else if(selectedDish == null) {
			DishNameField.setText("");
			dishTypeField.setText("");
			componentField.setText("");
			timeToMakeField.setText("");
			priceField.setText("");
		}
	}
	
	public void removeDish(ActionEvent e) {
		Dish selectedComponent = allDishes.getSelectionModel().getSelectedItem();
		if(selectedComponent !=  null) {
			Restaurant.getInstance().removeDish(selectedComponent);
			//update the list after removal
			allDishes.getItems().clear();
			allDishes.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getDishes().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
		}
	}
}

