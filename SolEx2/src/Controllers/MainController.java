package Controllers;

import Model.Customer;
import Model.Restaurant;
import Model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController extends ControllerWrapper{
	public static Restaurant rest = Restaurant.getInstance();

	@FXML
    private Label lblStatus;
    
    @FXML
    private TextField txtUserName;
    
    @FXML
    private TextField txtPassword;
    
    @FXML
	private Button loginBtn;
    @FXML
    private Button signInBtn;
    @FXML
    private AnchorPane loginPane;
    
    
    public void login(ActionEvent e) throws Exception {
    	boolean isExist = false;
    	if(txtUserName.getText().equals("manager") && txtPassword.getText().equals("manager")) {
    		soundOfButton("welcome.mp3");
    		moveToScene("/View/Manager_LandingPage.fxml", (Stage)txtUserName.getScene().getWindow());
    		isExist = true;
    	}
    	
    	for(Customer cust: rest.getCustomers().values()) {
    		if(txtUserName.getText().equals(cust.getUserName()) &&
    				txtPassword.getText().equals(cust.getPassword())) {
    				isExist = true;
    				State.setCurrentCustomer(cust);
    				soundOfButton("welcome.mp3");
    				moveToScene("/View/CustomerLandingPage.fxml", (Stage)txtUserName.getScene().getWindow());
    		}
    	}
    	
    	if(!isExist) {
    		lblStatus.setText("User name or password incorrect!");	
    		soundOfButton("error.mp3");
    	}
    		    		
    }	
    
    public void signIn(ActionEvent e) throws Exception {
        moveToScene("/View/Register.fxml", (Stage)signInBtn.getScene().getWindow(), Consts.defaultWidthRegistration, Consts.defaultHeightRegistration);
    }

}
