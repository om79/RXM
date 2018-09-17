package viroopa.com.medikart.dmMonitor.Module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DMM_AnalysisHeading implements Serializable {

	private long id;
	private String HeadingName;
	private String Min;

	public String getMax() {
		return Max;
	}

	public void setMax(String max) {
		Max = max;
	}

	public String getAvg() {
		return Avg;
	}

	public void setAvg(String avg) {
		Avg = avg;
	}

	private String Max;
	private String Avg;



	private List<DMM_AnalysisItemDetail> itemList = new ArrayList<DMM_AnalysisItemDetail>();

	public DMM_AnalysisHeading(long id, String HeadingName, String Min, String Max, String Avg) {
		this.id = id;
		this.HeadingName = HeadingName;
		this.Min = Min;
		this.Max = Max;
		this.Avg = Avg;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHeadingName() {
		return HeadingName;
	}

	public void setHeadingName(String HeadingName) {
		this.HeadingName = HeadingName;
	}

	public String getMin() {
		return Min;
	}

	public void setMin(String Min) {
		this.Min = Min;
	}

	public List<DMM_AnalysisItemDetail> getItemList() {
		return itemList;
	}

	public void setItemList(List<DMM_AnalysisItemDetail> itemList) {
		this.itemList = itemList;
	}
	
	

}
