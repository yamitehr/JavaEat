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
import javafx.scene.control.CheckBox;
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
	private Text messageToUserDish;
	@FXML
	private Text messageToUserComp;
	
	
	//components
	@FXML
	private TextField component_Name;
	@FXML
	private TextField price;
	@FXML
	private CheckBox isLactose;
	@FXML
	private CheckBox isGluten;
	
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
	
	/*public void moveToManagerDishScene(ActionEvent e) {
		moveToScene("/View/Manager_Dish.fxml", (Stage)dish_Name.getScene().getWindow());
	}*/
	
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
				messageToUserDish.setFill(Color.BLUE);
				messageToUserDish.setText("Dish added successfully");
				dish_Name.clear();
				timeToMake.clear();
				typesBox.getSelectionModel().clearSelection();
			}else {
				messageToUserDish.setFill(Color.RED);
				messageToUserDish.setText("an error has accured, please try again.");
			}
		}catch(InvalidInputException inputE) {
			messageToUserDish.setFill(Color.RED);
			messageToUserDish.setText(inputE.getMessage());
		}catch(NumberFormatException ne) {
			messageToUserDish.setFill(Color.RED);
			messageToUserDish.setText("Wrong Input!");
		}catch(Exception ex) {
			messageToUserDish.setFill(Color.RED);
			messageToUserDish.setText("an error has accured please try again");
		}
	}
	
	public void addComponent(ActionEvent e) {
		try {
			String componentName = component_Name.getText();
			if(componentName.isEmpty()) {
				throw new InvalidInputException("Please fill component name");
			}
			double priceOfComp;
			if(price.getText().isEmpty()) {
				throw new InvalidInputException("Please fill component price");
			}
			else
				priceOfComp = Double.parseDouble(price.getText());
			boolean isHasLactose = isLactose.isSelected();
			boolean isHasGluten = isGluten.isSelected();
			///		
			Component newComponent = new Component(componentName, isHasLactose, isHasGluten, priceOfComp);
		
			if(Restaurant.getInstance().addComponent(newComponent)) {
				messageToUserComp.setFill(Color.BLUE);
				messageToUserComp.setText("Component added successfully");
				component_Name.clear();
				price.clear();
				isLactose.setSelected(false);
				isGluten.setSelected(false);
			}else {
				messageToUserComp.setFill(Color.RED);
				messageToUserComp.setText("an error has accured, please try again.");
			}
		}catch(InvalidInputException inputE) {
			messageToUserComp.setFill(Color.RED);
			messageToUserComp.setText(inputE.getMessage());
		}catch(NumberFormatException ne) {
			messageToUserComp.setFill(Color.RED);
			messageToUserComp.setText("Wrong Input!");
		}catch(Exception ex) {
			messageToUserComp.setFill(Color.RED);
			messageToUserComp.setText("an error has accured please try again");
		}
	}
}

