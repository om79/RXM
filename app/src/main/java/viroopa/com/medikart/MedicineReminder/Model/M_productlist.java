package viroopa.com.medikart.MedicineReminder.Model;

/**
 * Created by admin on 07/08/2015.
 */
public class M_productlist {

    private String Id;
    private String SearchName;
    private String ManufactureName;
    private String DosageForm_Name;
    private String PackSize;
    private String Composition;
    private String Price;
    private String PinCode;
    private String ImagePath;
    private String Form_Name;
    private String productname;
    private String ImageExist;
    private String UOM;

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }



    public String getImageExist() {
        return ImageExist;
    }

    public void setImageExist(String imageExist) {
        ImageExist = imageExist;
    }



    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }



    public M_productlist() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSearchName() {
        return SearchName;
    }

    public void setSearchName(String searchName) {
        SearchName = searchName;
    }

    public String getManufactureName() {
        return ManufactureName;
    }

    public void setManufactureName(String manufactureName) {
        ManufactureName = manufactureName;
    }

    public String getDosageForm_Name() {
        return DosageForm_Name;
    }

    public void setDosageForm_Name(String dosageForm_Name) {
        DosageForm_Name = dosageForm_Name;
    }

    public String getPackSize() {
        return PackSize;
    }

    public void setPackSize(String packSize) {
        PackSize = packSize;
    }

    public String getComposition() {
        return Composition;
    }

    public void setComposition(String composition) {
        Composition = composition;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getForm_Name() {
        return Form_Name;
    }

    public void setForm_Name(String form_Name) {
        Form_Name = form_Name;
    }
}
