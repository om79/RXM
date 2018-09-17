package viroopa.com.medikart.model;

/**
 * Created by ABCD on 30/10/2015.
 */
public class M_memberlist {
    private int MemberId;

    private String MemberName;
    private Integer RelationshipId;
    private String  MemberDOB;
    private String MemberGender;
    private  int MemberFamilyNo;
    private  String Imageurl;
    private  String PatientId;

    public String getRelation_name() {
        return Relation_name;
    }

    public void setRelation_name(String relation_name) {
        Relation_name = relation_name;
    }

    private  String Relation_name;

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

    public Integer getRelationshipId() {
        return RelationshipId;
    }

    public void setRelationshipId(Integer relationshipId) {
        RelationshipId = relationshipId;
    }

    public String getMemberDOB() {
        return MemberDOB;
    }

    public void setMemberDOB(String memberDOB) {
        MemberDOB = memberDOB;
    }

    public String getMemberGender() {
        return MemberGender;
    }

    public void setMemberGender(String memberGender) {
        MemberGender = memberGender;
    }

    public int getMemberFamilyNo() {
        return MemberFamilyNo;
    }

    public void setMemberFamilyNo(int memberFamilyNo) {
        MemberFamilyNo = memberFamilyNo;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }


}
