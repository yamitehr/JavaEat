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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
	private Button editDishbtn;
	@FXML
	private Button editComponentBtn;

	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		editDishbtn.setDisable(true);
		editComponentBtn.setDisable(true);
		
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
	
	public void editDish(ActionEvent e) {
		Dish selectedDish = allDishes.getSelectionModel().getSelectedItem();
		if(selectedDish !=  null) {
			addDishBtn.setDisable(true);
			editDishbtn.setDisable(false);
			dish_Name.setText(selectedDish.getDishName());
			typesBox.setValue(selectedDish.getType());
			typesBox.setEditable(false);
			timeToMake.setText(String.valueOf(selectedDish.getTimeToMake()));
			for(int i = 0; i < selectedDish.getComponenets().size(); i++) {
				componentsList.getSelectionModel().select(selectedDish.getComponenets().get(i));
			}			
		}
	}
	
	public void setEditDish(ActionEvent e) {
		Dish selectedDish = allDishes.getSelectionModel().getSelectedItem();
		try {
			if(!selectedDish.getDishName().equals(dish_Name.getText())) {
				if(dish_Name.getText().isEmpty())
					throw new InvalidInputException("Dish Name cannot be empty");
				selectedDish.setDishName(dish_Name.getText());
		}
		if(!selectedDish.getType().equals(typesBox.getValue())) {
			if(typesBox.getValue() == null)
				throw new InvalidInputException("you must choose Dish Type");
			selectedDish.setType(typesBox.getValue());
		}
		if(selectedDish.getTimeToMake() != Integer.parseInt(timeToMake.getText()))
			selectedDish.setTimeToMake(Integer.parseInt(timeToMake.getText()));
		
		ArrayList<Component> changedComponents = new ArrayList<Component>();
		changedComponents.addAll(componentsList.getSelectionModel().getSelectedItems());
		if(changedComponents.isEmpty()) 
			throw new InvalidInputException("You must choose at least one component");
		for(int i = 0; i < selectedDish.getComponenets().size(); i++) {
			if(!changedComponents.contains(selectedDish.getComponenets().get(i)))
				selectedDish.removeComponent(selectedDish.getComponenets().get(i));			
		}
		for(Component c: changedComponents) {
			if(!selectedDish.getComponenets().contains(c))
				selectedDish.addComponent(c);
		}
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Dish edited successfully!");
		alert.showAndWait();
		messageToUserDish.setText("");
		dish_Name.clear();
		timeToMake.clear();
		typesBox.getSelectionModel().clearSelection();
		componentsList.getSelectionModel().clearSelection();
		allDishes.getItems().clear();
		allDishes.getItems().addAll(FXCollections.observableArrayList(
		Restaurant.getInstance().getDishes().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
		editDishbtn.setDisable(true);
		addDishBtn.setDisable(false);
		}catch(InvalidInputException inputE) {
			messageToUserDish.setFill(Color.RED);
			messageToUserDish.setText(inputE.getMessage());
		}catch(NumberFormatException ne) {
			messageToUserDish.setFill(Color.RED);
			messageToUserDish.setText("Wrong Input!");
		}
		
		
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
	
	public void editComponent(ActionEvent e) {
		Component selectedComponent = allComponents.getSelectionModel().getSelectedItem();
		if(selectedComponent !=  null) {
			addComponentBtn.setDisable(true);
			editComponentBtn.setDisable(false);
			component_Name.setText(selectedComponent.getComponentName());
			price.setText(String.valueOf(selectedComponent.getPrice()));
			if(selectedComponent.isHasGluten()) 
				isGluten.setSelected(true);
			if(selectedComponent.isHasLactose()) 
				isLactose.setSelected(true);
			isGluten.setDisable(true);
			isLactose.setDisable(true);
		}
	}
	
	public void setEditComponent(ActionEvent e) {
		Component selectedComponent = allComponents.getSelectionModel().getSelectedItem();
		try {
			if(!selectedComponent.getComponentName().equals(component_Name.getText())) {
				if(component_Name.getText().isEmpty())
					throw new InvalidInputException("Component Name cannot be empty");
				selectedComponent.setComponentName(component_Name.getText());
			}
			if(selectedComponent.getPrice() != Double.parseDouble(price.getText()))
				selectedComponent.setPrice(Double.parseDouble(price.getText()));
			
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setHeaderText("Component edited successfully!");
			alert.showAndWait();
			messageToUserComp.setText("");
			component_Name.clear();
			price.clear();
			isGluten.setSelected(false);
			isLactose.setSelected(false);
			isGluten.setDisable(false);
			isLactose.setDisable(false);
			allComponents.getItems().clear();
			allComponents.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getComponenets().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
			editComponentBtn.setDisable(true);
			addComponentBtn.setDisable(false);
		}
		catch(InvalidInputException inputE) {
			messageToUserComp.setFill(Color.RED);
			messageToUserComp.setText(inputE.getMessage());
		}catch(NumberFormatException ne) {
			messageToUserComp.setFill(Color.RED);
			messageToUserComp.setText("Wrong Input!");
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

