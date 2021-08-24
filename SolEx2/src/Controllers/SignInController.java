package Controllers;

import java.io.File;
import java.time.LocalDate;

import Exceptions.InvalidInputException;
import Exceptions.InvalidInputException;
import Model.Customer;
import Model.Restaurant;
import Utils.Gender;
import Utils.Neighberhood;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SignInController extends ControllerWrapper {

	private ObservableList<Neighberhood> neighberhood = FXCollections.observableArrayList(Neighberhood.values());
	@FXML
	private ChoiceBox<Neighberhood> NeighberhoodBox;	
	
	@FXML
	private TextField userName;
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private DatePicker birthDate;
	@FXML
	private RadioButton male;
	@FXML
	private RadioButton female;
	@FXML
	private RadioButton unknown;
	@FXML
	private CheckBox gluten;
	@FXML
	private CheckBox lactose;
	@FXML
	private PasswordField password;
	@FXML
	private TextArea resultConcole;
	@FXML
	private Button signInBtn;
	@FXML
	private ToggleGroup gender;
	@FXML
	private Button returnBtn;
	
	@FXML
    private Tooltip tooltip;
    
    @FXML
    private ProgressBar passwordMeter;
    
    public static double WEEK = 0.33;
    public static double MEDIUM = 0.66;
    public static double STRONG = 1;
    
	@FXML
	private void initialize() {
		NeighberhoodBox.setItems(neighberhood);
		returnBtn.setOnAction(e -> {
			moveToScene("/View/Login.fxml", (Stage)signInBtn.getScene().getWindow(), Consts.defaultWidthLogin, Consts.defaultHeightLogin);
		});
		tooltip.setShowDuration(new Duration(100000));
		
		firstName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				    	if (newValue != "") {
				    		resultConcole.setText("");
					    	if(!newValue.matches("[a-zA-Z\s]+")) {
					    		firstName.setText(newValue.substring(0, newValue.length()-1));
					    		resultConcole.setText("Letters Only!");
					    	}
				    	}
				    }
				});
		
		 lastName.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					    	if (newValue != "") {
					    		resultConcole.setText("");
						    	if(!newValue.matches("[a-zA-Z\s]+")) {
						    		lastName.setText(newValue.substring(0, newValue.length()-1));
						    		resultConcole.setText("Letters Only!");
						    	}
					    	}
					    }
					});
		 
		 birthDate.setOnAction(d -> {
			 if(birthDate.getValue() != null) {
				 resultConcole.setText("");
				 if(birthDate.getValue().isAfter(LocalDate.now())) {
					 resultConcole.setText("Date cannot be in the future!");
				 }
			 }
		 });
	}

	
	public void register(ActionEvent e) throws Exception {
		try {
			String user_Name = userName.getText();
			if(user_Name.isEmpty()) {
				throw new InvalidInputException("Please fill User Name");
			}
			for(Customer c : Restaurant.getInstance().getCustomers().values()) {
				if(c.getUserName().equals(user_Name)) {
				throw new InvalidInputException("User Name already exists");
				}
			}
			String first_Name = firstName.getText();
			if(first_Name.isEmpty()) {
				throw new InvalidInputException("Please fill First Name");
			}			
			String last_Name = lastName.getText();
			if(last_Name.isEmpty()) {
				throw new InvalidInputException("Please fill Last Name");
			}
			LocalDate date;
			date = birthDate.getValue();
			if(birthDate == null) {
				throw new InvalidInputException("Please select Date of Birth");
			}
			if(date.isAfter(LocalDate.now())) {
				throw new InvalidInputException("Date of birth can't be in the future");
			}
			Neighberhood neighberhood = NeighberhoodBox.getValue();
			if(neighberhood == null) {
				throw new InvalidInputException("Please select Neighborhood");
			}
			Gender gender;
			if(male.isSelected()) {
				gender = Gender.Male;
			}
			else if(female.isSelected()) {
				gender = Gender.Female;
			}
			else {
				gender = Gender.Unknown;
			}
			
			boolean sensitiveLactose = false;
			boolean sensitiveGluten = false;
			if(lactose.isSelected())
				sensitiveLactose = true;
			if(gluten.isSelected())
				sensitiveGluten = true;
		
			Customer customer = new Customer(firstName.getText(), lastName.getText(), birthDate.getValue(), 
					gender, NeighberhoodBox.getValue(), sensitiveLactose, sensitiveGluten, userName.getText(), password.getText());	
			MainController.rest.addCustomer(customer);
		
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Welcome " + firstName.getText());
			alert.setHeaderText("Welcome " + firstName.getText() + ". \n Please log in to continue.");
			alert.showAndWait();
		
			moveToScene("/View/Login.fxml", (Stage)signInBtn.getScene().getWindow(), Consts.defaultWidthLogin, Consts.defaultHeightLogin);
		}catch(InvalidInputException iie) {
			soundOfButton("error.mp3");
			resultConcole.setText(iie.getMessage());
		} catch(Exception exc) {
			soundOfButton("error.mp3");
			resultConcole.setText("an error has accured please try again");
		}
	}
	
	public static double getStrength(String pw)
    {
        if(pw.length() < 3)
        {
            return 0;
        }
        boolean containsNum = false;
        boolean containsSpec = false;
        boolean containsBig = false;
        for(int i=0;i<pw.length();i++)
        {
            if(Character.isDigit(pw.charAt(i)))
            {
                containsNum = true;
            }
            if(Character.isUpperCase(pw.charAt(i)))
            {
                containsBig = true;
            }
            if (String.valueOf(pw.charAt(i)).matches("[^a-zA-Z0-9]")) 
            {
                containsSpec = true;
            }
        }
        if(containsNum && containsSpec && containsBig)
        {
            return STRONG;
        }
        else if(containsNum && containsBig || containsNum && containsSpec || containsSpec && containsBig)
        {
            return MEDIUM;
        }
        
        return WEEK;
    }
	
	@FXML
	void setPasswordMeter() {
	    if(getStrength(password.getText()) == 0 )
	    {
	        tooltip.setText("Too short!");
	    }
	    else if(getStrength(password.getText()) == WEEK )
	    {
	        tooltip.setText("Password is week!");
	        passwordMeter.setStyle("-fx-accent: red;");
	    }
	    else if(getStrength(password.getText()) == MEDIUM )
	    {
	        tooltip.setText("Password is medium!");
	        passwordMeter.setStyle("-fx-accent: orange;");
	    }
	    else if(getStrength(password.getText()) == STRONG)
	    {
	        tooltip.setText("Password is strong!");
	        passwordMeter.setStyle("-fx-accent: green;");
	    }
	    passwordMeter.setProgress(getStrength(password.getText()));
	}

	 

}
