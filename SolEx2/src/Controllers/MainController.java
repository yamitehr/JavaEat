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
    		moveToScene("/View/Manager_LandingPage.fxml", (Stage)txtUserName.getScene().getWindow());
    	//	lblStatus.setText("Welcome manager");
    		isExist = true;
    	}
    	
    	for(Customer cust: rest.getCustomers().values()) {
    		if(txtUserName.getText().equals(cust.getUserName()) &&
    				txtPassword.getText().equals(cust.getPassword())) {
    				State.setCurrentCustomer(cust);
//    				Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerMenu.fxml"));
//    				Scene scene = new Scene(root);
//    				Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
//    				stage.setScene(scene);
//    				stage.centerOnScreen();
//    				stage.setTitle("Menu");
//    				stage.show();
    				moveToScene("/view/CustomerLandingPage.fxml", (Stage)loginBtn.getScene().getWindow());
    		}
    	}
    	
    	if(!isExist)
    		lblStatus.setText("Oops!");	
    		    		
    }	
    
    public void signIn(ActionEvent e) throws Exception {
        moveToScene("/View/SignIn.fxml", (Stage)signInBtn.getScene().getWindow());
    	/*FXMLLoader loader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
		AnchorPane pane = loader.load();
		mainScreen.getChildren().removeAll(mainScreen.getChildren());
		mainScreen.getChildren().add(pane);*/
    }

}
