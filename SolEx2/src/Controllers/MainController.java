package Controllers;

import Model.Customer;
import Model.Restaurant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController extends ControllerWrapper{
	public static Restaurant rest = Restaurant.getInstance();
	private static Customer nowLogin;
	
    public static Customer getNowLogin() {
		return nowLogin;
	}

	public static void setNowLogin(Customer nowLogin) {
		MainController.nowLogin = nowLogin;
	}

	@FXML
    private Label lblStatus;
    
    @FXML
    private TextField txtUserName;
    
    @FXML
    private TextField txtPassword;
    
    @FXML
	private AnchorPane mainScreen;
    
    public void login(ActionEvent e) throws Exception {
    	boolean isExist = false;
    	
    	if(txtUserName.getText().equals("manager") && txtPassword.getText().equals("manager")) {
    		moveToScene("/View/Manager_LandingPage.fxml", (Stage)txtUserName.getScene().getWindow());
    		lblStatus.setText("Welcome manager");
    		isExist = true;
    	}
    	
    	for(Customer cust: rest.getCustomers().values()) {
    		if(txtUserName.getText().equals(cust.getUserName()) &&
    				txtPassword.getText().equals(cust.getPassword())) {
    				nowLogin = cust;
    				Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerMenu.fxml"));
    				Scene scene = new Scene(root);
    				Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
    				stage.setScene(scene);
    				stage.centerOnScreen();
    				stage.setTitle("Menu");
    				stage.show();
    		}
    	}
    	if(!isExist)
    		lblStatus.setText("Oops!");	    		
    }	
    
    public void signIn(ActionEvent e) throws Exception {
    	AnchorPane loginForm = FXMLLoader.load(getClass().getResource(("/View/SignIn.fxml")));
        mainScreen.getChildren().setAll(loginForm);
    	/*FXMLLoader loader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
		AnchorPane pane = loader.load();
		mainScreen.getChildren().removeAll(mainScreen.getChildren());
		mainScreen.getChildren().add(pane);*/
    }

}
