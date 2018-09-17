package viroopa.com.medikart.buying.model;

/**
 * Created by prakash on 18/08/15.
 */
public class M_cart {

    public M_cart() {
    }

    String Cart_H_Id ,CheckOutId ,ProductName,PackSize, Price ,
            QTY, Amount,Type , Discount, Image,DosageForm_Name  ,Category ,Form_Name;

    public String getCart_H_Id() {
        return Cart_H_Id;
    }

    public void setCart_H_Id(String cart_H_Id) {
        Cart_H_Id = cart_H_Id;
    }

    public String getCheckOutId() {
        return CheckOutId;
    }

    public void setCheckOutId(String checkOutId) {
        CheckOutId = checkOutId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPackSize() {
        return PackSize;
    }

    public void setPackSize(String packSize) {
        PackSize = packSize;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDosageForm_Name() {
        return DosageForm_Name;
    }

    public void setDosageForm_Name(String dosageForm_Name) {
        DosageForm_Name = dosageForm_Name;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getForm_Name() {
        return Form_Name;
    }

    public void setForm_Name(String form_Name) {
        Form_Name = form_Name;
    }
}
