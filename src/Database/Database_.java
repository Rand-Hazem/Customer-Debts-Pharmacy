/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Classes.Customer;
import Classes.DebtBillTableNode;
import Classes.DebtRecorde;
import Classes.MyAlert;
import Classes.PaymentRecord;
import Classes.Product;
import Classes.SharedValues;
import Classes.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 *
 * @author rand
 */
public class Database_ implements DatabaseKeys {

    private JDBC_Connection jdbc_connection;
    private Connection con;
    private Statement st;

    public Database_() {
        jdbc_connection = new JDBC_Connection();
        con = jdbc_connection.getConnection();

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void openConnection() {
        try {
            if (con != null && con.isClosed()) {
                con = jdbc_connection.getConnection();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * close statement then connection
     */
    private void closeConnection() {
        if (con != null) {
            try {
                st.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private boolean insert(String query) {
        openConnection();

        try {
            st = con.createStatement();
            st.execute(query);
            closeConnection();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            closeConnection();
            return false;
        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private int update(String query) {
        openConnection();
        int updatedRecords = 0;
        try {
            st = con.createStatement();
            updatedRecords = st.executeUpdate(query);
            
            closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            closeConnection();
        }
        return updatedRecords;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private int delete(String query) {
        openConnection();
        int deleted = 0;
        try {
            st = con.createStatement();
            deleted = st.executeUpdate(query);
            // st.close();
            closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            closeConnection();

        }
        return deleted;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private ResultSet select(String query) {
        openConnection();

        ResultSet resultSet = null;

        try {
            st = con.createStatement();
            resultSet = st.executeQuery(query);
            // here i wont close the connection becouse it will close the result set too, so the connection will be closed after get all records from result set

        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            closeConnection();
        }

        return resultSet;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean insertUser(User user) {

        String query = "insert into " + t_users
                + "("
                + c_user_name + ", "
                + c_user_password
                + ") "
                + " values("
                + "'" + user.getName() + "'"
                + ", "
                + "'" + user.getPassword() + "'"
                + ")";

        return insert(query);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean deleteUser() {
        String query = "delete from " + t_users
                + " where "
                + c_userId
                + " = "
                + SharedValues.user.getUserId();

        return (delete(query) > 0);

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean updateUser(User user) {
        String query = "update " + t_users
                + " set "
                + c_user_name + "='" + user.getName() + "'"
                + ", "
                + c_user_password + "='" + user.getPassword() + "'"
                +" where "
                +c_userId+" = "+user.getUserId()+";"
                ;

        return (update(query) > 0);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean validUserInformation(User user) {
        boolean validLogIn = false;

        if (user == null) {
            return false;
        }

        openConnection();
        PreparedStatement pStatment = null;

        String query = "select * from "
                + t_users
                + " where "
                + c_user_name
                + "=? AND "
                + c_user_password
                + "=?";

        try {
            pStatment = con.prepareStatement(query);
            pStatment.setString(1, user.getName());
            pStatment.setString(2, user.getPassword());

            ResultSet resultSet = pStatment.executeQuery();
            if (resultSet.next()) {
                user.setUserId(resultSet.getInt(1));
                SharedValues.user = user;
                validLogIn = true;
            }

            pStatment.close();
            con.close();
            return validLogIn;

        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public ArrayList<Customer> getAllCustomers() {

        ArrayList<Customer> list = new ArrayList<>();
        String query = "select * from " + t_customers + " order by " + c_customer_name;
        ResultSet resultSet = select(query);

        //----- but the item in the list
        try {
            while (resultSet.next()) {
                Customer c = new Customer(resultSet.getInt(c_customer_Id), resultSet.getString(c_customer_name), resultSet.getDouble(c_customer_debtAmount));
                list.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            closeConnection();
        }

        //----- Close the resut set
        try {
            resultSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeConnection();
        return list;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean insertProduct(Product product) {

        String query = "insert into " + t_products
                + " values ("
                + "'" + product.getBarcode() + "'"
                + ","
                + "'" + product.getName() + "'"
                + ","
                + product.getPrice()
                + ")";

        return insert(query);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean updateProduct(Product product) {
        String query = "update " + t_products
                + " set "
                + c_product_name
                + "= '" + product.getName() + "'"
                + ", "
                + c_product_price + "=" + product.getPrice()
                + " where "
                + c_product_barcode
                + "='" + product.getBarcode() + "';";

        return (update(query) > 0); // correct barcode , primary jey exists it will updae, otherwise it won't

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public Product selectProduct(String barcode) {
        Product p = null;
        String query = "select * from " + t_products
                + " where "
                + c_product_barcode + "='" + barcode + "'";

        ResultSet resultSet = select(query);
        try {
            while (resultSet.next()) {
                p = new Product(resultSet.getString(c_product_barcode), resultSet.getString(c_product_name), resultSet.getDouble(c_product_price));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
        }

        return p;

    }



    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private boolean insertCustomer(Customer customer) {

        openConnection();

        boolean success = false;
        String query = "insert into " + t_customers
                + " ("
                + c_customer_name + ","
                + c_customer_debtAmount
                + ")"
                + " values ("
                + "'" + customer.getName() + "'"
                + ","
                + "0"
                + ")";

        try {
            st = con.createStatement();
            st.executeUpdate(query);

            if (st.getGeneratedKeys().next()) { // get the auto greated customer-id
                customer.setId(st.getGeneratedKeys().getInt(1));
                success = true;
            } else {
                success = false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            return false;

        } finally {
            closeConnection();
        }

        return success;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
 /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private String getInsertMultiDebtProductRecordQuery(ObservableList<DebtBillTableNode> debtProducts, int debtId) {

        String query = "insert into " + t_debtProducts
                + " values ";
        for (int i = 0; i < debtProducts.size(); i++) {
            query += "("
                    + debtId
                    + ", "
                    + "'" + debtProducts.get(i).getBarcode() + "'"
                    + ", "
                    + debtProducts.get(i).getAmount()
                    + ")";
            if (i != debtProducts.size() - 1) {
                query += ", ";
            }
        }
        return query + ";";

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private String getIncreasedCustomerDebtAmoutQuery(Customer c, double debtAmount) {
        String query = "update " + t_customers
                + " set " + c_customer_debtAmount
                + "=" + c_customer_debtAmount + "+" + debtAmount
                + " where "
                + c_customer_Id + "=" + c.getId();

        return query;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private String getInsertDebtQuery(DebtRecorde record, Customer customer) {
        String query = "insert into " + t_debts
                + " ("
                + c_debt_customerId + ", "
                + c_debt_date + ", "
                + c_debt_totalPrice + ", "
                + c_debt_paidPrice
                + ")"
                + " values ("
                + customer.getId()
                + ", "
                +" CURRENT_DATE "   // record.getDate()
                + ", "
                + record.getTotalPrice()
                + ", "
                + record.getPaidPrice()
                + ")";
        return query;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    // it return debtRecorde to catch the new-customer and add it to customer-comboBox 
    public DebtRecorde insertDebts(DebtRecorde record, Customer customer, boolean isNewCustomer, ObservableList ol_debtProducts) {

        // *** Step 1 
        // if it is new custmoer, insert it to customer table and set customerId to customer object
        if (isNewCustomer && !insertCustomer(customer)) {
            MyAlert.showMsg("Something went wrong please try again\nCustomer name already exists!", Alert.AlertType.ERROR);
            return null;
        } else {
            record.setCustomerId(customer.getId());
        }

        openConnection();

        try {
            // *** Step2 
            // becouse many table will be updated and its depentent, so, transaction needed
            con.setAutoCommit(false);
            st = con.createStatement();

            // *** Step 3 
            // insert debt to DEBTS table and get debtId
            int debtId;
            st.executeUpdate(getInsertDebtQuery(record, customer));
            ResultSet result_idGenerated = st.getGeneratedKeys();

            /// ***  Step 4 
            // if debt was inserted successfully
            if (result_idGenerated.next()) {
                debtId = result_idGenerated.getInt(1); // get debt id
                st.executeUpdate(getInsertMultiDebtProductRecordQuery(ol_debtProducts, debtId)); // insert debt-bill to debtProduct table
                st.executeUpdate(getIncreasedCustomerDebtAmoutQuery(customer, record.getDebtAmount())); // update debtAmount form customer
                con.commit();
                result_idGenerated.close();
            } else {
                MyAlert.showMsg("Something went wrong please try again\ninsert customer", Alert.AlertType.ERROR);
                con.rollback(); // do not save anything
                result_idGenerated.close();
                return null;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return null;

        } finally {
            closeConnection();
        }

        return record;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public ObservableList<PaymentRecord> getCustomerPayments(int customerId) {

        ObservableList<PaymentRecord> ol_payment = FXCollections.observableArrayList();

        String query = "select * from " + t_payment
                + " where "
                + c_payment_customerId + " = " + customerId
                + " order by " + c_payment_date;

        ResultSet resultSet = select(query);

        try {
            while (resultSet.next()) {
                PaymentRecord payment = new PaymentRecord();
                payment.setPaidAmount(resultSet.getDouble(c_payment_paidAmount));
                payment.setDate( resultSet.getString(c_payment_date));
                ol_payment.add(payment);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                resultSet.close();
                closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ol_payment;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public ObservableList<DebtRecorde> getCustomerDebts(int customerId) {

        ObservableList<DebtRecorde> ol_debts = FXCollections.observableArrayList();

        String query = "select "
                + c_debt_Id + ", "
                + c_debt_date + ", "
                + c_debt_totalPrice + ", "
                + c_debt_paidPrice
                + " from " + t_debts
                + " where " + c_debt_customerId + " = " + customerId;

        ResultSet resultSet = select(query);

        try {
            while (resultSet.next()) {
                DebtRecorde debt = new DebtRecorde();
                debt.setDebtId(resultSet.getInt(c_debt_Id));
                debt.setDate(resultSet.getString(c_debt_date));
                debt.setTotalPrice(resultSet.getDouble(c_debt_totalPrice));
                debt.setPaidPrice(resultSet.getDouble(c_debt_paidPrice));
                
                ol_debts.add(debt);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);

        } finally {

            try {
                resultSet.close();
                closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ol_debts;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public ObservableList<DebtBillTableNode> getBill(int debtId) {

        ObservableList<DebtBillTableNode> ol_bill = FXCollections.observableArrayList();

        String query = "select * from " + t_debtProducts + " A "
                + " inner join "
                + t_products + " B "
                + " ON "
                + "A." + c_debtProducts_productBarcode + "=" + "B." + c_product_barcode
                +" where "
                +c_debtProducts_debtId+ " = "+debtId;

        ResultSet resultSet = select(query);

        try {
            while (resultSet.next()) {
                DebtBillTableNode billNode = new DebtBillTableNode();
                billNode.setBarcode(resultSet.getString(c_debtProducts_productBarcode));
                billNode.setAmount(resultSet.getDouble(c_debtProducts_amount));
                billNode.setProductName(resultSet.getString(c_product_name));

                ol_bill.add(billNode);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                resultSet.close();
                closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ol_bill;

    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public boolean insertPayment(PaymentRecord record) {
        String payment_query = "insert into " + t_payment
                + " values ("
                +" CURRENT_TIMESTAMP" // record.getDate()
                + ", "
                + record.getCustomerId()
                + ", "
                + record.getPaidAmount()
                + ")";
        
        String decreaseCustomerDebt_query="update "+t_customers
                +" set "
                +c_customer_debtAmount+"="+c_customer_debtAmount+"-"+record.getPaidAmount()
                +" where "
                +c_customer_Id +" = "+record.getCustomerId();   
        
        
        boolean succeed=false;
        openConnection();
        try {
            con.setAutoCommit(false);
            st=con.createStatement();
            
            st.executeUpdate(payment_query);
            st.executeUpdate(decreaseCustomerDebt_query);
            
            con.commit();
            succeed=true;
        } catch (SQLException ex) {
            Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex);
            MyAlert.showMsg("Something went wrong, please try again", Alert.AlertType.ERROR);
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Database_.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        }finally{
            closeConnection();
        }
        
        
        
        return succeed;
    }
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
}
