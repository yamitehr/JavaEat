package Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import Exceptions.InvalidInputException;
import Model.Component;
import Model.Dish;
import Model.Restaurant;
import Utils.DishType;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private TableView<Dish> allDishesTable;
	@FXML
	private TableColumn<Dish, Integer> dishIdCol;
	@FXML
	private TableColumn<Dish, String> dishNameCol;
	@FXML
	private TableColumn<Dish, String> componentsCol;
	@FXML
	private TableColumn<Dish, Double> priceCol;
	
	
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
	private TableView<Component> allComponentsTable;
	@FXML
	private TableColumn<Component, Integer> componentIdCol;
	@FXML
	private TableColumn<Component, String> comonentNameCol;
	@FXML
	private TableColumn<Component, String> sensitivitiesCol;
	@FXML
	private TableColumn<Component, Double> compPriceCol;
	@FXML
	private AnchorPane toReplacePane;
	
	
	//to check
	@FXML
	private Button addDishBtn;
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
		
				
		//Add all dishes
		dishIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
				
		dishNameCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(dish.getValue().getDishName()));
				
		componentsCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(
				dish.getValue().getComponenets()
				.stream()
				.map(d -> d.toString())
				.reduce((a, b) -> a + ", " + b).get()
				));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
				
				
		List<Dish> allDishes = new ArrayList<Dish>();
		allDishes = Restaurant.getInstance().getDishes().values().stream()
				.collect(Collectors.toList());
				
		allDishesTable.getItems().addAll(allDishes);
		
						
			//Add all components
		componentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		
		comonentNameCol.setCellValueFactory(comp -> new ReadOnlyObjectWrapper<String>(comp.getValue().getComponentName()));
				
		sensitivitiesCol.setCellValueFactory(component -> {
            boolean isGluten = component.getValue().isHasGluten();
            boolean isLactose = component.getValue().isHasLactose();
            String isSensitiveAsString = "";
            if(isGluten == true)
            {
            	isSensitiveAsString += "Gluten ";
            }
            if(isLactose == true)
            {
            	isSensitiveAsString += "Lactose";
            }

         return new ReadOnlyStringWrapper(isSensitiveAsString);
        });
		
		compPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
				
				
		List<Component> allComponents = new ArrayList<Component>();
		allComponents = Restaurant.getInstance().getComponenets().values().stream()
				.collect(Collectors.toList());
				
		allComponentsTable.getItems().addAll(allComponents);
	}
	
	public void removeDish(ActionEvent e) {
		Dish selectedDish = allDishesTable.getSelectionModel().getSelectedItem();
		if(selectedDish !=  null) {
			Restaurant.getInstance().removeDish(selectedDish);
			//update the list after removal
			allDishesTable.getItems().clear();
			allDishesTable.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getDishes().entrySet().stream().map(d -> d.getValue()).collect(Collectors.toList())));
		}
	}
	
	public void editDish(ActionEvent e) {
		Dish selectedDish = allDishesTable.getSelectionModel().getSelectedItem();
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
		Dish selectedDish = allDishesTable.getSelectionModel().getSelectedItem();
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
		allDishesTable.getItems().clear();
		allDishesTable.getItems().addAll(FXCollections.observableArrayList(
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
				allDishesTable.getItems().clear();
				allDishesTable.getItems().addAll(FXCollections.observableArrayList(
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
		Component selectedComponent = allComponentsTable.getSelectionModel().getSelectedItem();
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
		Component selectedComponent = allComponentsTable.getSelectionModel().getSelectedItem();
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
			allComponentsTable.getItems().clear();
			allComponentsTable.getItems().addAll(FXCollections.observableArrayList(
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
				allComponentsTable.getItems().clear();
				allComponentsTable.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getComponenets().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
				componentsList.getItems().clear();
				componentsList.getItems().addAll(FXCollections.observableArrayList(
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
		Component selectedComponent = allComponentsTable.getSelectionModel().getSelectedItem();
		if(selectedComponent !=  null) {
			Restaurant.getInstance().removeComponent(selectedComponent);
			//update the list after removal
			allComponentsTable.getItems().clear();
			allComponentsTable.getItems().addAll(FXCollections.observableArrayList(
			Restaurant.getInstance().getComponenets().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
		}
	}
	
	
}

