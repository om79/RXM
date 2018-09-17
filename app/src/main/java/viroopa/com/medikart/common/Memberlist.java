package viroopa.com.medikart.common;

/**
 * Created by prakash on 14/04/16.
 */
public class Memberlist {


    private int MemberId;

    private String MemberName;
    private int RelationshipId;
    private String  MemberDOB;

    private String MemberGender;
    private  int MemberFamilyNo;
    private  String Imageurl;
    private  int PatientId;

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }


    public Memberlist () {

    }


    public int getPatientId() {
        return PatientId;
    }

    public void setPatientId(int patientid) {
        PatientId = patientid;
    }
    public int getMemberId() {
        return MemberId;
    }

    public void setMemberId(int memberId) {
        MemberId = memberId;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String memberName) {
        MemberName = memberName;
    }

    public int getRelationshipId() {
        return RelationshipId;
    }

    public void setRelationshipId(int relationshipId) {
        RelationshipId = relationshipId;
    }

    public String getMemberDOB() {
        return MemberDOB;
    }

    public void setMemberDOB(String memberdOB) {
        MemberDOB = memberdOB;
    }

    public String getMemberGender() {
        return MemberGender;
    }

    public void setMemberGender(String membergender) {
        MemberGender = membergender;
    }


    public int getMemberFamilyNo() {
        return MemberFamilyNo;
    }

    public void setMemberFamilyNo(int memberfamilyNo) {
        MemberFamilyNo = memberfamilyNo;
    }
}

