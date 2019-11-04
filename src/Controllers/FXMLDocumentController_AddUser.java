package Controllers;

import Classes.Check;
import Classes.MyAlert;
import Classes.PageSwapper;
import Classes.User;
import Database.Database_;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author rand
 */
public class FXMLDocumentController_AddUser implements Initializable, Check {

    @FXML
    private TextField addUser_tf_username;

    @FXML
    private PasswordField addUser_tf_password;

    @FXML
    private PasswordField addUser_tf_confirmPass;

    @FXML
    private Button addUser_b_add;

    @FXML
    private Button addUser_b_homePage;

    private boolean pass_and_confirm_identical = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setTextChangeListenerToConfirmPasswordField();
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void addUser() {

        String username = addUser_tf_username.getText();
        String password = addUser_tf_password.getText();

        // password and confirm password must be identical
        if (!pass_and_confirm_identical) {
            MyAlert.showMsg("Password and confirm password are different", Alert.AlertType.ERROR);
            return;
        }

        // username and password musn't be empty
        if (!validUsername(username) || !validPassword(password)) {
            MyAlert.showMsg("You must fill all fields", Alert.AlertType.ERROR);
            return;
        }

        Database_ d = new Database_();
        User user = new User(username, password);

        if (!d.insertUser(user)) {
            MyAlert.showMsg("Failed to add user, it may be duplicated username!\nplease, change username and try again", Alert.AlertType.ERROR);

        } else {
            MyAlert.showMsg("User added successfully", Alert.AlertType.INFORMATION);
            addUser_tf_username.setText("");
            addUser_tf_password.setText("");
            addUser_tf_confirmPass.setText("");

        }

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void setTextChangeListenerToConfirmPasswordField() {

        addUser_tf_confirmPass.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> oList, String oldValue, String newValue) {
                if (newValue.equals(addUser_tf_password.getText())) {
                    addUser_tf_confirmPass.setStyle("-fx-text-fill:#000");
                    pass_and_confirm_identical = true;
                } else {
                    addUser_tf_confirmPass.setStyle("-fx-text-fill:red");
                    pass_and_confirm_identical = false;
                }
            }
        });

    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void moveToMain(){
        PageSwapper.move(getClass().getResource(PageSwapper.MAIN_PAGE_PATH));
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
}
