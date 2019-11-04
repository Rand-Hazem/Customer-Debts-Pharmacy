package Controllers;

import Classes.Check;
import Classes.Customer;
import Classes.DebtBillTableNode;
import Classes.DebtRecorde;
import Classes.MyAlert;
import Classes.PageSwapper;
import Classes.PaymentRecord;
import Database.Database_;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
/* For autocomplete text-field*/
import org.controlsfx.control.textfield.AutoCompletionBinding;
import static org.controlsfx.control.textfield.TextFields.bindAutoCompletion;

/**
 *
 * @author rand
 */
public class FXMLDocumentController_CustomerRecords implements Initializable, Check {

    @FXML
    private TextField customerRecords_tf_customerName;
    @FXML
    private TextField customerRecords_tf_paidPrice;
    @FXML
    private TableView customerRecords_tableView_debts;
    @FXML
    private TableView customerRecords_tableView_bill;
    @FXML
    private TableView customerRecords_tableView_paymentHistory;
    @FXML
    private Label customerRecords_l_totalDebt;
    @FXML
    private Button customerRecords_b_submit;

    @FXML
    private TableColumn customerRecords_tcDebts_date;
    @FXML
    private TableColumn customerRecords_tcDebts_totalPrice;
    @FXML
    private TableColumn customerRecords_tcDebts_paidPrice;

    @FXML
    private TableColumn customerRecords_tcPayment_date;
    @FXML
    private TableColumn customerRecords_tcPayment_paidPrice;

    @FXML
    private TableColumn customerRecords_tcBill_barcode;
    @FXML
    private TableColumn customerRecords_tcBill_product;
    @FXML
    private TableColumn customerRecords_tcBil_amount;

    private ArrayList<Customer> customerList;
    private Customer currentCustomer;

    private ObservableList<DebtRecorde> ol_debts;
    private ObservableList<PaymentRecord> ol_payments;
    private ObservableList<DebtBillTableNode> ol_bill;


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setAllCustomersToautoCompleteTextField();
        setSellFactoryForTabels();

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void setObservableListToTabels() {

        customerRecords_tableView_debts.setItems(ol_debts);
        customerRecords_tableView_bill.setItems(ol_bill);
        customerRecords_tableView_paymentHistory.setItems(ol_payments);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void setSellFactoryForTabels() {

        // Debts table
        customerRecords_tcDebts_date.setCellValueFactory(new PropertyValueFactory("date"));
        customerRecords_tcDebts_totalPrice.setCellValueFactory(new PropertyValueFactory("totalPrice"));
        customerRecords_tcDebts_paidPrice.setCellValueFactory(new PropertyValueFactory("paidPrice"));

        // Bill yable
        customerRecords_tcBill_barcode.setCellValueFactory(new PropertyValueFactory("barcode"));
        customerRecords_tcBill_product.setCellValueFactory(new PropertyValueFactory("productName"));
        customerRecords_tcBil_amount.setCellValueFactory(new PropertyValueFactory("amount"));

        // Payment table
        customerRecords_tcPayment_date.setCellValueFactory(new PropertyValueFactory("date"));
        customerRecords_tcPayment_paidPrice.setCellValueFactory(new PropertyValueFactory("paidAmount"));

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void setAllCustomersToautoCompleteTextField() {
        Database_ d = new Database_();
        customerList = d.getAllCustomers();

        // make textfield autoCompleted and set all customer list to it
        AutoCompletionBinding autoComTextField = bindAutoCompletion(customerRecords_tf_customerName, customerList);

        // Event listener when autoCompleted > user chose a customer
        autoComTextField.setOnAutoCompleted(new javafx.event.EventHandler<AutoCompletionBinding.AutoCompletionEvent<Customer>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<Customer> t) {

                if (t.getCompletion() != null) {
                    currentCustomer= t.getCompletion();
                    customerRecords_l_totalDebt.setText("Total debt amount : " + currentCustomer.getDebtAmount());
                    FillTabels();
                }
            }
        });
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void FillTabels() {

        Database_ d = new Database_();

        ol_debts = d.getCustomerDebts(currentCustomer.getId());
        ol_payments = d.getCustomerPayments(currentCustomer.getId());

        setObservableListToTabels();

        if (!ol_debts.isEmpty()) {
            customerRecords_tableView_debts.getSelectionModel().select(0); // becouse it doesn't invoke a listner to fill Bill-table so, i have to do it by my-self
            setOnRowSelectedForDebtTable();
        }

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    @FXML
    private void setOnRowSelectedForDebtTable() {

        int selectedRow = customerRecords_tableView_debts.getSelectionModel().getSelectedIndex();
        if (selectedRow < 0) {
            return;
        }

        Database_ d = new Database_();
        ol_bill = d.getBill(ol_debts.get(selectedRow).getDebtId());
        customerRecords_tableView_bill.setItems(ol_bill);

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * 
     * @return false if - no customer selected
     * - typed invalid customer (name of current customer dosn't match typed customer name)
     * - paid price is more than debt amount 
     *  true otherwise
     *  
     */
    private boolean checkBeforSubmition(){
        
        String paidPrice=customerRecords_tf_paidPrice.getText();
        
        if((currentCustomer!=null && !currentCustomer.getName().equals(customerRecords_tf_customerName.getText()))
                || currentCustomer == null){
            MyAlert.showMsg("Invalid customer name, please select valid one", Alert.AlertType.ERROR);
        }else if(!validPrice(paidPrice)){
            MyAlert.showMsg("Please, input a valid price\nIt must be number", Alert.AlertType.ERROR);
        }else if(Double.parseDouble("0"+paidPrice) == 0) {
             MyAlert.showMsg("There is no paid price to submit", Alert.AlertType.ERROR);
        }else if(Double.parseDouble("0"+paidPrice) > currentCustomer.getDebtAmount()){
            MyAlert.showMsg("Paid price is more than required debt amount", Alert.AlertType.ERROR);
        }else{
            return true;
        }
       return false;
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    
    
    @FXML
    private void submitPayment(){
        
        if(!checkBeforSubmition()){
            return;
        }
        
        
        if(!confirmSubmition()){
            return;
        }
        
        
        PaymentRecord paymnet=new PaymentRecord();
        paymnet.setCustomerId(currentCustomer.getId());
        paymnet.setPaidAmount(Double.parseDouble(customerRecords_tf_paidPrice.getText()));
        paymnet.setDate(new Date(System.currentTimeMillis())+"");
        
        Database_ d=new Database_();
        if(d.insertPayment(paymnet)){
            ol_payments.add(paymnet);
            currentCustomer.setDebtAmount(currentCustomer.getDebtAmount() - paymnet.getPaidAmount());
            customerRecords_l_totalDebt.setText("Total debt amount : " +currentCustomer.getDebtAmount());
            customerRecords_tf_paidPrice.setText("");
            MyAlert.showMsg("Payment inserted successfully", Alert.AlertType.INFORMATION);
        }
        
        
    }
    
    
 /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    private boolean confirmSubmition(){
        String s="Customer : "+currentCustomer.getName()
                +"\n"
                +"Paid amount : "+customerRecords_tf_paidPrice.getText()
                +"\n\n"
                +"confirm submission ?";
        
        ButtonType b_submit=new ButtonType("submit");
        
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION, s,ButtonType.CANCEL,b_submit );
        alert.showAndWait();
        if(alert.getResult() == b_submit){
            return true;
        }
        return false;
    }
    
 /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @FXML
    private void moveToMain() {
        PageSwapper.move(getClass().getResource(PageSwapper.MAIN_PAGE_PATH));
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    private void clear() {

        ol_bill.clear();
        ol_debts.clear();
        ol_payments.clear();
        setObservableListToTabels(); // means clear tables

        customerRecords_l_totalDebt.setText("Total debt amount : ");
        customerRecords_tf_paidPrice.setText("");

    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

}
