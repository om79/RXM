package viroopa.com.medikart.MedicineReminder.Model;

/**
 * Created by admin on 07/08/2015.
 */
public class mednotificationList {

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getDoasge() {
        return Doasge;
    }

    public void setDoasge(String doasge) {
        Doasge = doasge;
    }

    public String getSchedule_date_time() {
        return Schedule_date_time;
    }

    public void setSchedule_date_time(String schedule_date_time) {
        Schedule_date_time = schedule_date_time;
    }





    private String Id;
    private String MedicineName;
    private String Doasge;
    private String Schedule_date_time;

    public Integer getSnooze_count() {
        return Snooze_count;
    }

    public void setSnooze_count(Integer snooze_count) {
        Snooze_count = snooze_count;
    }

    private Integer Snooze_count;

    public Integer getImage_id() {
        return image_id;
    }

    public void setImage_id(Integer image_id) {
        this.image_id = image_id;
    }

    public Integer getF_color_id() {
        return f_color_id;
    }

    public void setF_color_id(Integer f_color_id) {
        this.f_color_id = f_color_id;
    }

    public Integer getS_color_id() {
        return s_color_id;
    }

    public void setS_color_id(Integer s_color_id) {
        this.s_color_id = s_color_id;
    }

    private Integer image_id;
    private Integer f_color_id;
    private Integer s_color_id;

    private String Status;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }





}