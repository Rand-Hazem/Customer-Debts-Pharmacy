
package Database;

/**
 *
 * @author rand
 */

/*
    t > table
    c > column
*/
public interface DatabaseKeys {
    public final String t_users="Users";
    public final String c_userId="userId";
    public final String c_user_name="username";
    public final String c_user_password="password";
    
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public final String t_customers="customers";
    public final String c_customer_Id="customerId";
    public final String c_customer_name="customerName";
    public final String c_customer_debtAmount="debtAmount";
    
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public final String t_products="products";
    public final String c_product_barcode="barcode";
    public final String c_product_name="productName";
    public final String c_product_price="Price";
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public final String t_debts="Debts";
    public final String c_debt_Id="debtId";
    public final String c_debt_customerId=c_customer_Id;
    public final String c_debt_date="date";
    public final String c_debt_paidPrice="paidAmount";
    public final String c_debt_totalPrice="totalPrice";
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    public final String t_debtProducts="DebtProducts";
    public final String c_debtProducts_debtId=c_debt_Id;
    public final String c_debtProducts_productBarcode=c_product_barcode;
    public final String c_debtProducts_amount="amount";
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public final String t_payment="payment";
    public final String c_payment_customerId=c_customer_Id;
    public final String c_payment_paidAmount="paidAmount";
    public final String c_payment_date="date";
    
}
