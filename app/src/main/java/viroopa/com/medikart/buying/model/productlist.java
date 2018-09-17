package viroopa.com.medikart.buying.model;

/**
 * Created by admin on 07/08/2015.
 */
public class productlist {

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
    private Boolean IsPharma;

    public Boolean getIsPharma() {
        return IsPharma;
    }

    public void setIsPharma(Boolean isPharma) {
        IsPharma = isPharma;
    }



    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    private String CategoryId;

    public String getCatalogue() {
        return Catalogue;
    }

    public void setCatalogue(String catalogue) {
        Catalogue = catalogue;
    }

    private String Catalogue;

    public String getJson_type() {
        return json_type;
    }

    public void setJson_type(String json_type) {
        this.json_type = json_type;
    }

    private String json_type;


    public String getMfg() {
        return Mfg;
    }

    public void setMfg(String mfg) {
        Mfg = mfg;
    }

    private String Mfg;

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String heading) {
        Heading = heading;
    }

    public String getSearch_count() {
        return search_count;
    }

    public void setSearch_count(String search_count) {
        this.search_count = search_count;
    }

    private String Heading;
    private String search_count;


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



    public productlist() {
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
