package viroopa.com.medikart.dmMonitor.Module;

import java.io.Serializable;

public class DMM_AnalysisItemDetail implements Serializable {

	private long id;

	private String GroupId;
	private String FilterCondition;
	private String ToDate;

	public String getToDate() {
		return ToDate;
	}

	public void setToDate(String toDate) {
		ToDate = toDate;
	}



	public String getFilterAMPMCondition() {
		return FilterAMPMCondition;
	}

	public void setFilterAMPMCondition(String filterAMPMCondition) {
		FilterAMPMCondition = filterAMPMCondition;
	}

	private String FilterAMPMCondition;
	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	private String checkFlag;

	public DMM_AnalysisItemDetail(long id, String GroupId, String FilterCondition, String FilterAMPMCondition,String ToDate) {
		this.id=id;
		this.GroupId = GroupId;
		this.ToDate=ToDate;
		this.FilterCondition = FilterCondition;
		this.FilterAMPMCondition=FilterAMPMCondition;
	}


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getGroupId() {
		return GroupId;
	}
	public void setGroupId(String GroupId) {
		this.GroupId = GroupId;
	}
	public String getFilterCondition() {
		return FilterCondition;
	}
	public void setFilterCondition(String FilterCondition) {
		this.FilterCondition = FilterCondition;
	}


}
