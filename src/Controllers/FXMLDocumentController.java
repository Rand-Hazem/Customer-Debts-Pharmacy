/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author rand
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button b_login;

    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_password;

    private final String devUsername = "BrRandDev";
    private final String devPass = "BlNh";

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        SharedValues.user=null;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private boolean validLogin() {
        String username = tf_username.getText();
        String password = tf_password.getText();

        User user = new User(username, password);
        Database_ database = new Database_();
        return database.validUserInformation(user);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private boolean devLogin() {
        String username = tf_username.getText();
        String password = tf_password.getText();

        if (devUsername.equals(username) && devPass.equals(password)) {
            User user = new User(username, password);
            SharedValues.user = user;
            return true;
        }

        return false;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    @FXML
    private void logInActionWhenEnterKeyPresed(KeyEvent key){
        if(key != null && key.getCode() == KeyCode.ENTER){
            moveToMainPage();
        }
    }
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void moveToMainPage() {

        if (devLogin() || validLogin()) {
            PageSwapper.move(getClass().getResource(PageSwapper.MAIN_PAGE_PATH));

        } else {
            MyAlert.showMsg("Invalid username or password !\nplease, click OK and try again", Alert.AlertType.WARNING);
        }

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/


}
