package Controllers;

import java.util.ArrayList;

import Exceptions.InvalidInputException;
import Model.Component;
import Model.Dish;
import Model.Restaurant;
import Utils.DishType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddDishController extends ControllerWrapper {
	@FXML
	private TextField dish_Name;
	@FXML
	private ComboBox<DishType> typesBox;
	@FXML
	private TextField timeToMake;
	@FXML
	private ListView<Component> componentsList;
	@FXML
	private Text messageToUser;
	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		ObservableList<DishType> types = FXCollections.observableArrayList(DishType.values());
		typesBox.getItems().clear();				
		typesBox.setItems(FXCollections.observableArrayList(types));
		
		ObservableList<Component> components = FXCollections.observableArrayList(Restaurant.getInstance().getComponenets().values());
		componentsList.setItems(components);
		componentsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	public void moveToManagerDishScene(ActionEvent e) {
		moveToScene("/View/Manager_Dish.fxml", (Stage)dish_Name.getScene().getWindow());
	}
	
	public void addDish(ActionEvent e) {
		try {
			String dishName = dish_Name.getText();
			if(dishName.isEmpty()) {
				throw new InvalidInputException("Please fill dish name");
			}
			DishType dishType = typesBox.getValue();
			if(dishType == null) {
				throw new InvalidInputException("Please select Dish Type");
			}
			
			int time;
			if(timeToMake.getText().isEmpty()) {
				throw new InvalidInputException("Please fill dish time to make");
			}
			else
				time = Integer.parseInt(timeToMake.getText());
			ArrayList<Component> dishComponents = new ArrayList<Component>();
			dishComponents.addAll(componentsList.getSelectionModel().getSelectedItems());	
			if(dishComponents.isEmpty())
				throw new InvalidInputException("Please choose at least one component!");		   
			Dish newDish = new Dish(dishName, dishType, dishComponents, time);
			///		
		
			if(Restaurant.getInstance().addDish(newDish)) {
				messageToUser.setFill(Color.BLUE);
				messageToUser.setText("Dish added successfully");
				dish_Name.clear();
				timeToMake.clear();
				typesBox.getSelectionModel().clearSelection();
			}else {
				messageToUser.setFill(Color.RED);
				messageToUser.setText("an error has accured, please try again.");
			}
		}catch(InvalidInputException inputE) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText(inputE.getMessage());
		}catch(NumberFormatException ne) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText("Wrong Input!");
		}catch(Exception ex) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText("an error has accured please try again");
		}
	}
}

