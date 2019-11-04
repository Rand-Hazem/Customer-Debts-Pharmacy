package Controllers;

import Classes.Check;
import Classes.MyAlert;
import Classes.PageSwapper;
import Classes.Product;
import Database.Database_;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 *
 * @author rand
 */
public class FXMLDocumentController_AddProduct implements Initializable, Check {

    @FXML
    private TextField addProduct_tf_barcode;
    @FXML
    private TextField addProduct_tf_name;
    @FXML
    private TextField addProduct_tf_price;
    @FXML
    private RadioButton addProduct_rb_add;
    @FXML
    private RadioButton addProduct_rb_edit;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private Button addProduct_b_homePage;

    @Override

    public void initialize(URL url, ResourceBundle rb) {
        // initialize toggleGroup
        toggleGroup = new ToggleGroup();
        addProduct_rb_add.setSelected(true);

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void submit() {

        String barcode = addProduct_tf_barcode.getText();
        String name = addProduct_tf_name.getText();
        String price = addProduct_tf_price.getText();

        Product p = null;

        if (validBarcode(barcode) && validProductName(name) && validPrice(price)) {
            p = new Product(barcode, name, Double.parseDouble(price));

            if (addProduct_rb_add.isSelected()) {
                addProduct(p);
            } else {
                editProduct(p);
            }

        } else {
            MyAlert.showMsg("Wrong input\n- You must fill all fields\n- Price must be number only", Alert.AlertType.ERROR);
        }

    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void addProduct(Product p) {
        Database_ d = new Database_();
        if (d.insertProduct(p)) {
            MyAlert.showMsg("Product inserted successfully", Alert.AlertType.INFORMATION);
            clearFields();
        } else {
            MyAlert.showMsg("Barcode already exists \nproduct already exists", Alert.AlertType.ERROR);
        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void editProduct(Product p) {
        Database_ d = new Database_();
        if (d.updateProduct(p)) {
            MyAlert.showMsg("Product updates successfully", Alert.AlertType.INFORMATION);
            clearFields();
        } else {
            MyAlert.showMsg("Wrong barcode\nNo such item for this barcode", Alert.AlertType.ERROR);
        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void clearFields() {
        addProduct_tf_barcode.setText("");
        addProduct_tf_name.setText("");
        addProduct_tf_price.setText("");
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void moveToMain(){
        PageSwapper.move(getClass().getResource(PageSwapper.MAIN_PAGE_PATH));
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
}
