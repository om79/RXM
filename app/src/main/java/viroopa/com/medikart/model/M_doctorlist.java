package viroopa.com.medikart.model;

/**
 * Created by prakash on 17/08/15.
 */
public class M_doctorlist {

    private int CityId;
    private String doctorid;
    private String doctorname;
    private int memberid;

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public int getMemberid() {
        return memberid;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public M_doctorlist () {

    }
}
