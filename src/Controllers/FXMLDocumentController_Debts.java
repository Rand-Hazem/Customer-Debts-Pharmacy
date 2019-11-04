package Controllers;

import Classes.Check;
import Classes.Customer;
import Classes.DebtBillTableNode;
import Classes.DebtRecorde;
import Classes.MyAlert;
import Classes.PageSwapper;
import Classes.Product;
import Database.Database_;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLDocumentController_Debts implements Initializable, Check {

    @FXML
    private ComboBox<Customer> debts_cb_customersName;
    @FXML
    private TextField debts_tf_barcode;
    @FXML
    private Label debts_l_productName;
    @FXML
    private TextField debts_tf_amount;
    @FXML
    private TextField debts_tf_priceForOnePiece;
    @FXML
    private TextField debts_tf_totalPrice;
    @FXML
    private TextField debts_tf_paidPrice;
    @FXML
    private TextField debts_tf_remainPrice;
    @FXML
    private Button debts_b_add;
    @FXML
    private Button debts_b_submit;
    @FXML
    private TableView debts_table_debtsRecords;
    @FXML
    private Button debts_b_mainPage;
    @FXML
    private TableColumn debts_tc_price;
    @FXML
    private TableColumn debts_tc_amount;
    @FXML
    private TableColumn debts_tc_productName;
    @FXML
    private TableColumn debts_tc_barcode;

    private ObservableList<DebtBillTableNode> tableObservableList = FXCollections.observableArrayList();
    private ArrayList<Customer> customersList;
    private boolean valiedProduct = false;
    private boolean isNewCustomer = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        allowOnlyNumberInputToTextField_Listener(debts_tf_amount);
        allowOnlyNumberInputToTextField_Listener(debts_tf_priceForOnePiece);
        allowOnlyNumberInputToTextField_Listener(debts_tf_totalPrice);
        allowOnlyNumberInputToTextField_Listener(debts_tf_paidPrice);

        fillCustomersList();
        setAutoComleteTextFieldToComboBox();
        getProductProperityWhenBarcodeCatched();

        // some technic to show data in table :3 
        setCellValueFactoryForTableColumns();
        debts_table_debtsRecords.setItems(tableObservableList);

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void fillCustomersList() {

        Database_ d = new Database_();
        customersList = d.getAllCustomers();
        debts_cb_customersName.getItems().addAll(customersList);

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void setAutoComleteTextFieldToComboBox() {
//        AutoCompletionBinding auto = bindAutoCompletion(
//                debts_cb_customersName.getEditor(), customersList);
//
//        auto.setOnAutoCompleted(new javafx.event.EventHandler<AutoCompletionBinding.AutoCompletionEvent<Customer>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<Customer> t) {
//
//                if (t.getCompletion() != null) {
//                  debts_cb_customersName.getSelectionModel().select(t.getCompletion());
//                }
//            }
//
//        });
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void getProductProperityWhenBarcodeCatched() {

        debts_tf_barcode.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldValue, Object newValue) {

                if (!validBarcode(newValue.toString())) {
                    return;
                }

                Database_ d = new Database_();
                Product p = d.selectProduct(newValue.toString());

                setProductInformationInComponent(p);

            }
        });

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void setProductInformationInComponent(Product p) {
        if (p == null) {
            valiedProduct = false;
            MyAlert.showMsg("No such product for this barcode", Alert.AlertType.ERROR);
            clearProductDetailFields();
        } else {
            valiedProduct = true;
            debts_l_productName.setText(p.getName());
            debts_tf_amount.setText("1");
            debts_tf_priceForOnePiece.setText(p.getPrice() + "");
        }

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void allowOnlyNumberInputToTextField_Listener(TextField tf) {
        tf.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldValue, Object newValue) {

                if (newValue.toString().isEmpty()) {
                    tf.setText("0");
                } else if (!validPrice(newValue.toString())) {
                    tf.setText(oldValue.toString());
                    return;
                }
                debts_tf_remainPrice.setText(
                        (Double.parseDouble("0" + debts_tf_totalPrice.getText()) - Double.parseDouble("0" + debts_tf_paidPrice.getText())) + "");

            }
        });
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void addDebt() {

        if (!valiedProduct) {
            MyAlert.showMsg("Wrong product barcode!", Alert.AlertType.ERROR);
            return;
        }

        String barcode = debts_tf_barcode.getText();
        String productName = debts_l_productName.getText();
        double amount = Double.parseDouble(debts_tf_amount.getText());
        double price = Double.parseDouble(debts_tf_priceForOnePiece.getText());

        DebtBillTableNode node = new DebtBillTableNode(barcode, productName, amount, amount * price);
        tableObservableList.add(node);
        increaseTotalPrice(node.getPrice());

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * increase total price when node added to the table
     */
    private void increaseTotalPrice(double price) {
        double p = Double.parseDouble("0" + debts_tf_totalPrice.getText()) + price;
        debts_tf_totalPrice.setText(p + "");

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void decreaseTotalPrice(double price) {
        double p = Double.parseDouble("0" + debts_tf_totalPrice.getText()) - price;
        if (p >= 0) {
            debts_tf_totalPrice.setText(p + "");
        } else {
            debts_tf_totalPrice.setText("0");
        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void setCellValueFactoryForTableColumns() {
        debts_tc_barcode.setCellValueFactory(new PropertyValueFactory("barcode"));
        debts_tc_productName.setCellValueFactory(new PropertyValueFactory("productName"));
        debts_tc_amount.setCellValueFactory(new PropertyValueFactory("amount"));
        debts_tc_price.setCellValueFactory(new PropertyValueFactory("price"));
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * that method is set when mouse click listener when a user click mouse in
     * row, that method invoked
     */
    @FXML
    private void setOnItemSelectedListenerForDebtTable() {

        int selectedRowIndex = debts_table_debtsRecords.getSelectionModel().getSelectedIndex();
        if (selectedRowIndex < 0) {
            return;
        }

        // show alert aske user to delete slected row
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this row ?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        Button bYes = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
        Button bNO = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
        bNO.setDefaultButton(true);
        bYes.setDefaultButton(false);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            double price = tableObservableList.get(selectedRowIndex).getPrice();
            decreaseTotalPrice(price);
            // delete row from tableObservableList
            tableObservableList.remove(selectedRowIndex);
        }

    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private Customer getCustomerFromComboBox() {

        String customerName = debts_cb_customersName.getEditor().getText();
        Customer selectionCustomer = debts_cb_customersName.getSelectionModel().getSelectedItem();

        if (!customerName.isEmpty() && (selectionCustomer == null
                || !customerName.equals(selectionCustomer.getName()))) {
            Customer c = new Customer(customerName, Double.parseDouble("0" + debts_tf_totalPrice.getText()) - Double.parseDouble("0" + debts_tf_paidPrice.getText()));
            isNewCustomer = true;
            return c;
        } else if (selectionCustomer != null) {
            isNewCustomer = false;
            return debts_cb_customersName.getValue();
        }

        return null;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private boolean checkLogicDataBeforSubmit() {
        boolean b = false;

        if (getCustomerFromComboBox() == null) {
            MyAlert.showMsg("Please, choose or enter a customer name", Alert.AlertType.ERROR);
        } else if (tableObservableList.size() == 0) {
            MyAlert.showMsg("No such data to insert", Alert.AlertType.ERROR);
        } else if (debts_tf_totalPrice.getText().equals(debts_tf_paidPrice.getText())) {
            MyAlert.showMsg("Total price and paid price are equal, why do you want to insert a debt!!", Alert.AlertType.ERROR);
        } else {
            b = true;
        }
        /*
        else if (!valiedProduct && debts_tf_barcode.getText().isEmpty()) {
            MyAlert.showMsg("No such product", Alert.AlertType.ERROR);
        }
         */

        return b;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void submit() {

        if (!checkLogicDataBeforSubmit()) {
            return;
        }

        DebtRecorde debtRecord = new DebtRecorde();
        debtRecord.setTotalPrice(Double.parseDouble(debts_tf_totalPrice.getText()));
        debtRecord.setPaidPrice(Double.parseDouble("0" + debts_tf_paidPrice.getText()));
        if (!isNewCustomer) {
            debtRecord.setCustomerId(debts_cb_customersName.getValue().getId());
        }

        // insert data
        Database_ d = new Database_();
        DebtRecorde insertedDebt = d.insertDebts(debtRecord, getCustomerFromComboBox(), isNewCustomer, tableObservableList);

        // check if data inserted or not
        if (insertedDebt != null) {
            MyAlert.showMsg("Debt inserted successfully", Alert.AlertType.INFORMATION);
        } else {
            return;
        }

        // if the custmer is new, add it to the combo box
        if (isNewCustomer) {
            customersList.add(new Customer(insertedDebt.getCustomerId(), debts_cb_customersName.getEditor().getText(), insertedDebt.getDebtAmount()));
            debts_cb_customersName.getItems().add(new Customer(insertedDebt.getCustomerId(), debts_cb_customersName.getEditor().getText(), insertedDebt.getDebtAmount()));

            
        }

        clearAll();

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void moveToMainPage() {
        PageSwapper.move(getClass().getResource(PageSwapper.MAIN_PAGE_PATH));

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void clearAll() {
        clearProductDetailFields();
        debts_tf_totalPrice.setText("");
        debts_tf_paidPrice.setText("");
        debts_tf_remainPrice.setText("");
        debts_cb_customersName.getEditor().setText("");
        isNewCustomer = false;
        valiedProduct = false;
        tableObservableList.clear();

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void clearProductDetailFields() {
        debts_tf_barcode.setText("");
        debts_l_productName.setText("");
        debts_tf_amount.setText("1");
        debts_tf_priceForOnePiece.setText("");
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
}
