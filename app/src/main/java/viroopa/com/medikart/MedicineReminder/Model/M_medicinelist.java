package viroopa.com.medikart.MedicineReminder.Model;

/**
 * Created by admin on 30/06/2015.
 */
public class M_medicinelist {



    private String med_id;
    private String MedicineName;
    private String Date;
    private String Time;
    private String PackSize;
    private String UOM;
    private String sound_uri;
    private String product_actual_id;

    public String getProduct_actual_id() {
        return product_actual_id;
    }

    public void setProduct_actual_id(String product_actual_id) {
        this.product_actual_id = product_actual_id;
    }



    public String getSound_uri() {
        return sound_uri;
    }

    public void setSound_uri(String sound_uri) {
        this.sound_uri = sound_uri;
    }



    public String getPackSize() {
        return PackSize;
    }

    public void setPackSize(String packSize) {
        PackSize = packSize;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }



    public void setStatus(String status) {
        Status = status;
    }

    private String Status;

    public Integer getImage_id() {
        return Image_id;
    }

    public void setImage_id(Integer image_id) {
        Image_id = image_id;
    }

    public Integer getFirst_color_id() {
        return first_color_id;
    }

    public void setFirst_color_id(Integer first_color_id) {
        this.first_color_id = first_color_id;
    }

    public Integer getSecond_color_id() {
        return second_color_id;
    }

    public void setSecond_color_id(Integer second_color_id) {
        this.second_color_id = second_color_id;
    }

    private Integer Image_id;
    private Integer first_color_id;
    private Integer second_color_id;




    public String getMed_id() {
        return med_id;
    }

    public void setMed_id(String med_id) {
        this.med_id = med_id;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getStatus() {
        return Status;
    }


}