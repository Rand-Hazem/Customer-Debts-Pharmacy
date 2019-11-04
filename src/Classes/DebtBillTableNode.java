
package Classes;

/**
 *
 * @author rand
 */
public class DebtBillTableNode {
    
    private String barcode;
    private String productName;
    private double amount;
    private double price;

    
    public DebtBillTableNode(){
        
    }
    
    
    public DebtBillTableNode(String barcode, String productName, double amount, double price){
        this.barcode=barcode;
        this.productName=productName;
        this.amount=amount;
        this.price=price;
    }
    
    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    
    
    
}
