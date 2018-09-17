package viroopa.com.medikart.model;

/**
 * Created by admin on 30/06/2015.
 */
public class citylist {

    private int CityId;
    private String CityName;
    private String CityDesc;
    private int StateId;
    private String StateName;

    public citylist () {

    }
    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCityDesc() {
        return CityDesc;
    }

    public void setCityDesc(String cityDesc) {
        CityDesc = cityDesc;
    }

    public int getStateId() {
        return StateId;
    }

    public void setStateId(int stateId) {
        StateId = stateId;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }
}
