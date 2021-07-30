package Controllers;

import java.util.ArrayList;
import java.util.stream.Collectors;

import Exceptions.InvalidInputException;
import Model.Component;
import Model.Dish;
import Model.Restaurant;
import Utils.DishType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
	
	@FXML
	private ListView<Dish> allDishes;
	
	
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
	private Button addComponentBtn;

	@FXML
	private ListView<Component> allComponents;
	@FXML
	private Text componentNameField;
	@FXML
	private Text priceCompField;
	@FXML
	private Text sensitivitiesField;	
	@FXML
	private AnchorPane toReplacePane;
	
	
	//to check
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
		ObservableList<DishType> types = FXCollections.observableArrayList(DishType.values());
		typesBox.getItems().clear();				
		typesBox.setItems(FXCollections.observableArrayList(types));
		
		ObservableList<Component> components = FXCollections.observableArrayList(Restaurant.getInstance().getComponenets().values());
		componentsList.setItems(components);
		componentsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		
		
		////////All dishes list view
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
		
		//Event listener for listview
				allDishes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Dish>() {
				    @Override
				    public void changed(ObservableValue<? extends Dish> observable, Dish oldValue, Dish newValue) {
				    	updateDishDetailsFields();
				    }
				});
				
		//Add all dishes
		allDishes.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getDishes().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
		
		//Set the listview cell factory to show the right component name
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
		Dish selectedDish = allDishes.getSelectionModel().getSelectedItem();
		if(selectedDish !=  null) {
			Restaurant.getInstance().removeDish(selectedDish);
			//update the list after removal
			allDishes.getItems().clear();
			allDishes.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getDishes().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
		}
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
				allDishes.getItems().clear();
				allDishes.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getDishes().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
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
	
	public void updateComponentDetailsFields() {
		Component selectedComponent = allComponents.getSelectionModel().getSelectedItem();
		if(selectedComponent != null) {
			componentNameField.setText(selectedComponent.getComponentName());
			priceCompField.setText(String.valueOf(selectedComponent.getPrice()));
		
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
			priceCompField.setText("");
			sensitivitiesField.setText("");
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
				allComponents.getItems().clear();
				allComponents.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getComponenets().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
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

