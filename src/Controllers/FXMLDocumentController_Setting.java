package Controllers;

import Classes.Check;
import Classes.MyAlert;
import Classes.PageSwapper;
import Classes.SharedValues;
import Classes.User;
import Database.Database_;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author rand
 */
public class FXMLDocumentController_Setting implements Initializable, Check {

    @FXML
    private TextField setting_tf_username;
    @FXML
    private PasswordField setting_tf_password;
    @FXML
    private PasswordField setting_tf_newPassword;
    @FXML
    private PasswordField setting_tf_confirmNewpassword;
    @FXML
    private Button setting_b_changePass;
    @FXML
    private Button setting_b_home;
    @FXML
    private Label setting_l_deleteAccount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // set current username to the textfield
        setting_tf_username.setText(SharedValues.user.getName());
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private boolean check(String username, String pass, String confirmPass) {
        if (!checkCurrentUserPassword()) {
            return false;
        }

        if (!validUsername(username) || !validPassword(pass) || !validPassword(confirmPass)) {
            MyAlert.showMsg("you must fill all fields", Alert.AlertType.ERROR);
            return false;
        }

        if (!pass.equals(confirmPass)) {
            MyAlert.showMsg("New password and confirm new password are different", Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void updatePassword() {

        String username = setting_tf_username.getText();
        String newPass = setting_tf_newPassword.getText();
        String confirmNewPass = setting_tf_confirmNewpassword.getText();

        if(!check(username, newPass, confirmNewPass)){
            return;
        }
        
        
        Database_ d = new Database_();
        User user = new User(SharedValues.user.getUserId(), username, newPass);
        boolean updated = d.updateUser(user);
        
        if(updated){
            MyAlert.showMsg("Account updated successfully", Alert.AlertType.INFORMATION);
            SharedValues.user=user;
        }else{
           MyAlert.showMsg("Somthing got wrong, account not updated!", Alert.AlertType.ERROR);
        }
        

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void deleteAccount() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this account ?",
               ButtonType.NO , ButtonType.YES);

              
        alert.setHeaderText(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES && checkCurrentUserPassword()) {

            Database_ d = new Database_();
            boolean successed = d.deleteUser();
            if (successed) {
                MyAlert.showMsg("Account deleted successfully", Alert.AlertType.INFORMATION);
                moveToLogInPage();

            } else {
                MyAlert.showMsg("Somthing got wrong, account can't be deleted", Alert.AlertType.ERROR);
            }

        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private boolean checkCurrentUserPassword() {
        String pass = setting_tf_password.getText();
        if (!SharedValues.user.getPassword().equals(pass)) {
            MyAlert.showMsg("Wrong password\nTo make any change on this account you must enter correct password", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void moveToMain(){
        PageSwapper.move(getClass().getResource(PageSwapper.MAIN_PAGE_PATH));
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void moveToLogInPage() {
        PageSwapper.move(getClass().getResource(PageSwapper.LOGIN_PAGE_PATH));

    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

}
