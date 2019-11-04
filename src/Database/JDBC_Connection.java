/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rand
 */
public class JDBC_Connection implements DatabaseKeys {
    
    private final String winPath="C:\\Program Files";
    private String linuxPath="";

    public JDBC_Connection() {
        
        linuxPath=System.getProperty("user.home"); // home/rand
        
        createFolder();
        createTabels();

    }

    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void createFolder() {
        File f = new File(linuxPath+"/Desktop/Customer debts");
        if (!f.exists()) {
            f.mkdir();
        }
        File fExe = new File(linuxPath+"/Desktop/Customer debts/data.exe");
        if (!fExe.exists()) {
            fExe.mkdirs();
        }

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public Connection getConnection() {

        Connection con = null;

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:"+linuxPath+"/Desktop/Customer debts/data.exe/database.db");
        } catch (SQLException ex) {
            Logger.getLogger(JDBC_Connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JDBC_Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return con;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void createTabels() {
        String usersTable = "create table if not exists " + t_users
                + "("
                + c_userId + " integer primary key autoincrement, "
                + c_user_name + " varchar not null unique, "
                + c_user_password + " varchar not null"
                + ")";

        String productsTable = "create table if not exists " + t_products
                + "("
                + c_product_barcode + " varchar primary key, "
                + c_product_name + " varchar not null, "
                + c_product_price + " double not null"
                + ")"
                ;

        String customerTable = "create table if not exists " + t_customers
                + "("
                + c_customer_Id + " integer primary key autoincrement, "
                + c_customer_name + " varchar unique not null,"
                + c_customer_debtAmount + " double not null"
                + ")"
                ;

        String debtsTable = "create table if not exists " + t_debts
                + "("
                + c_debt_Id + " integer primary key autoincrement,"
                + c_debt_customerId + " not null, "
                + c_debt_date + " date not null, "
                + c_debt_totalPrice + " double not null, "
                + c_debt_paidPrice + " double not null, "
                + "constraint fk_customerId foreign key (" + c_debt_customerId + ") references " + t_customers + "(" + c_customer_Id + ") "
                + ")"
                ;

        
        String debtProducts = "create table if not exists " + t_debtProducts
                + "("
                + c_debtProducts_debtId + " integer not null, "
                + c_debtProducts_productBarcode + " varchar not null,"
                + c_debtProducts_amount + " double not null,"
                + "constraint fk_barcode foreign key (" + c_debtProducts_productBarcode + ") references " + t_products + "(" + c_product_barcode + ")"
                +")"
                ;

        
        String paymentTable = "create table if not exists " + t_payment
                + "("
                + c_payment_date + " TIMESTAMP primary key not null, "
                + c_payment_customerId + " integer not null, "
                + c_payment_paidAmount + " double not null, "
                + "constraint pk_customerId foreign key(" + c_payment_customerId + ") references " + t_customers + "(" + c_customer_Id + ")"
                + ")"
                ;

        String[] arr = {usersTable, productsTable, customerTable, debtsTable, debtProducts, paymentTable};
        createTable(arr);

    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void createTable(String[] queries) {

        Connection con = getConnection();

        try {
            con.setAutoCommit(false);
            Statement st = con.createStatement();

            for (int i = 0; i < queries.length; i++) {
                st.execute(queries[i]);
            }
            // save and close the database
            con.commit();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(JDBC_Connection.class.getName()).log(Level.SEVERE, null, ex);

            try {
                // do not save anything (rollback)
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(JDBC_Connection.class.getName()).log(Level.SEVERE, null, ex1);
            }

        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(JDBC_Connection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
}
