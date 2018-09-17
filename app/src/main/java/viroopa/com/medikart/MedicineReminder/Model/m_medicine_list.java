package viroopa.com.medikart.MedicineReminder.Model;

/**
 * Created by Administrator on 26/Aug/2015.
 */
public class m_medicine_list {
    public m_medicine_list() {
    }

    private String Medicine_Id;
    private String Medicine_Name;
    private String Schedule_Id;
    private String schedule_date;
    private String schedule_status;
    private String schedule_dosage;
    private int refill_flag;

    public int getRefill_flag() {
        return refill_flag;
    }

    public void setRefill_flag(int refill_flag) {
        this.refill_flag = refill_flag;
    }



    public int getNo_reminder_flag() {
        return no_reminder_flag;
    }

    public void setNo_reminder_flag(int no_reminder_flag) {
        this.no_reminder_flag = no_reminder_flag;
    }

    private int no_reminder_flag;

    public String getRefill_date() {
        return refill_date;
    }

    public void setRefill_date(String refill_date) {
        this.refill_date = refill_date;
    }

    private String refill_date;

    public Integer getImd_id() {
        return imd_id;
    }

    public void setImd_id(Integer imd_id) {
        this.imd_id = imd_id;
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

    private Integer imd_id;
    private Integer first_color_id;
    private Integer second_color_id;

    public String getPill_buddy_image_name() {
        return pill_buddy_image_name;
    }

    public void setPill_buddy_image_name(String pill_buddy_image_name) {
        this.pill_buddy_image_name = pill_buddy_image_name;
    }

    private String pill_buddy_image_name;









    public String getSchedule_dosage() {
        return schedule_dosage;
    }

    public void setSchedule_dosage(String schedule_dosage) {
        this.schedule_dosage = schedule_dosage;
    }

    public String getSchedule_Id() {
        return Schedule_Id;
    }

    public void setSchedule_Id(String schedule_Id) {
        Schedule_Id = schedule_Id;
    }

    public String getMedicine_Id() {
        return Medicine_Id;
    }

    public void setMedicine_Id(String medicine_Id) {
        Medicine_Id = medicine_Id;
    }

    public String getMedicine_Name() {
        return Medicine_Name;
    }

    public void setMedicine_Name(String medicine_Name) {
        Medicine_Name = medicine_Name;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }

    public String getSchedule_status() {
        return schedule_status;
    }

    public void setSchedule_status(String schedule_status) {
        this.schedule_status = schedule_status;
    }


}
