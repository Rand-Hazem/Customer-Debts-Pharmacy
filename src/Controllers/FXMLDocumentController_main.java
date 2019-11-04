package Controllers;

import Classes.PageSwapper;
import Classes.SharedValues;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author rand
 */
public class FXMLDocumentController_main implements Initializable {

    @FXML
    private Label main_l_username;
    @FXML
    private Button main_b_setting;
    @FXML
    private Label main_l_addProduct;
    @FXML
    private Label main_l_customRecord;
    @FXML
    private Label main_l_debts;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (main_l_username != null) {
            main_l_username.setText(SharedValues.user.getName());
        }

    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void moveToAddUser(){
        PageSwapper.move(getClass().getResource(PageSwapper.ADD_USER_PAGE_PATH));
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void moveToAddEditProduct() {
        PageSwapper.move(getClass().getResource(PageSwapper.ADD_EDIT_PRODUCT_PAGE_PATH));
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void moveToCustomRecordes() {
        PageSwapper.move(getClass().getResource(PageSwapper.CUSTOMRECORDS_PAGE_PATH));
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void moveToDebts() {
        PageSwapper.move(getClass().getResource(PageSwapper.DEPTS_PAGE_PATH));
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void moveToSetting() {
        PageSwapper.move(getClass().getResource(PageSwapper.SETTING_PAGE_PATH));
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
}
