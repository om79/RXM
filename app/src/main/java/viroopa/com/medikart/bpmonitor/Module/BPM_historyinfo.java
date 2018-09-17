package viroopa.com.medikart.bpmonitor.Module;

/**
 * Created by Administrator on 26/Aug/2015.
 */
public class BPM_historyinfo {
    public BPM_historyinfo() {
    }

    private String Member_Id;
    private String Body_Part;
    private String Position;
    private String Systolic;
    private String Diastolic;
    private String Pulse;
    private String Weight;
    private String Weight_Unit;
    private int Arrthythmia;
    private String Comments;
    private String Bp_Date;
    private String Bp_Time;
    private int BpTimehr;



    private String sys_max;
    private String sys_min;
    private String dia_max;
    private String dia_min;
    private String max_pulse;
    private String min_pulse;



    private String max_wt;
    private String min_wt;
    private String title;



    public int getBpTimehr() {
        return BpTimehr;
    }

    public void setBpTimehr(int bpTimehr) {
        BpTimehr = bpTimehr;
    }

    public String getMember_Id() {
        return Member_Id;
    }

    public void setMember_Id(String member_Id) {
        Member_Id = member_Id;
    }

    public String getBody_Part() {
        return Body_Part;
    }

    public void setBody_Part(String body_Part) {
        Body_Part = body_Part;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getSystolic() {
        return Systolic;
    }

    public void setSystolic(String systolic) {
        Systolic = systolic;
    }

    public String getDiastolic() {
        return Diastolic;
    }

    public void setDiastolic(String diastolic) {
        Diastolic = diastolic;
    }

    public String getPulse() {
        return Pulse;
    }

    public void setPulse(String pulse) {
        Pulse = pulse;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getWeight_Unit() {
        return Weight_Unit;
    }

    public void setWeight_Unit(String weight_Unit) {
        Weight_Unit = weight_Unit;
    }

    public int getArrthythmia() {
        return Arrthythmia;
    }

    public void setArrthythmia(int arrthythmia) {
        Arrthythmia = arrthythmia;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getBp_Date() {
        return Bp_Date;
    }

    public void setBp_Date(String bp_Date) {
        Bp_Date = bp_Date;
    }

    public String getBp_Time() {
        return Bp_Time;
    }

    public void setBp_Time(String bp_Time) {
        Bp_Time = bp_Time;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;}


    public String getMin_wt() {

        return min_wt;
    }

    public void setMin_wt(String min_wt) {
        this.min_wt = min_wt;
    }

    public String getMax_wt() {

        return max_wt;
    }

    public void setMax_wt(String max_wt) {
        this.max_wt = max_wt;
    }

    public String getMin_pulse() {

        return min_pulse;
    }

    public void setMin_pulse(String min_pulse) {
        this.min_pulse = min_pulse;
    }

    public String getMax_pulse() {

        return max_pulse;
    }

    public void setMax_pulse(String max_pulse) {
        this.max_pulse = max_pulse;
    }

    public String getDia_min() {

        return dia_min;
    }

    public void setDia_min(String dia_min) {
        this.dia_min = dia_min;
    }

    public String getDia_max() {

        return dia_max;
    }

    public void setDia_max(String dia_max) {
        this.dia_max = dia_max;
    }

    public String getSys_min() {

        return sys_min;
    }

    public void setSys_min(String sys_min) {
        this.sys_min = sys_min;
    }

    public String getSys_max() {

        return sys_max;
    }

    public void setSys_max(String sys_max) {
        this.sys_max = sys_max;
    }
}
