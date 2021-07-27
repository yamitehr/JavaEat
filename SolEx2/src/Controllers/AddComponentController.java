package Controllers;

import Model.Component;
import Model.Restaurant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddComponentController extends ControllerWrapper {
	
	@FXML
	private TextField component_Name;
	@FXML
	private TextField price;
	@FXML
	private CheckBox isLactose;
	@FXML
	private CheckBox isGluten;
	@FXML
	private Text messageToUser;
	

	public void moveToManagerComponentScene(ActionEvent e) {
		moveToScene("/View/Manager_Components.fxml", (Stage)component_Name.getScene().getWindow());
	}
	
	//Adds the component to the restaurant
	public void addComponent(ActionEvent e) {
		String componentName = component_Name.getText();
		double priceOfComp = Double.valueOf(price.getText());;	
		boolean isHasLactose = isLactose.isSelected();
		boolean isHasGluten = isGluten.isSelected();
		///
		
		
		Component newComponent = new Component(componentName, isHasLactose, isHasGluten, priceOfComp);
		
		try {
			if(Restaurant.getInstance().addComponent(newComponent)) {
				messageToUser.setFill(Color.BLUE);
				messageToUser.setText("Component added successfully");
				component_Name.clear();
				isLactose.setSelected(false);
				isGluten.setSelected(false);
			}else {
				messageToUser.setFill(Color.RED);
				messageToUser.setText("an error has accured, please try again.");
			}
		}catch(Exception ex) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText("an error has accured please try again");
		}
	}

}
