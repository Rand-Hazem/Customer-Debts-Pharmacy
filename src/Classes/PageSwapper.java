package Classes;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

/**
 *
 * @author rand
 */
public class PageSwapper {

    public static final String LOGIN_PAGE_PATH = "/customer/debts/pharmacy/FXMLDocument.fxml";
    public static final String MAIN_PAGE_PATH = "/customer/debts/pharmacy/FXMLDoc_main.fxml";
    public static final String SETTING_PAGE_PATH = "/customer/debts/pharmacy/FXMLDoc_Setting.fxml";
    public static final String DEPTS_PAGE_PATH = "/customer/debts/pharmacy/FXMLDoc_Debts.fxml";
    public static final String CUSTOMRECORDS_PAGE_PATH = "/customer/debts/pharmacy/FXMLDoc_CustomerRecords.fxml";
    public static final String ADD_USER_PAGE_PATH="/customer/debts/pharmacy/FXMLDoc_AddUser.fxml";
    public static final String ADD_EDIT_PRODUCT_PAGE_PATH="/customer/debts/pharmacy/FXMLDoc_AddProduct.fxml";
    

    public static void move(URL fxmlFilePath) {

        Parent root = null;
        try {
            root = FXMLLoader.load(fxmlFilePath);
            Scene scene = new Scene(root);
            SharedValues.mainStage.setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(PageSwapper.class.getName()).log(Level.SEVERE, null, ex);
            showErrorMsg();
        }

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private static void showErrorMsg() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Internal error occers, fxml file is missing");
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
}
