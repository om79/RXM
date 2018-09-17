package viroopa.com.medikart.bpmonitor.Module;

import java.util.ArrayList;

/**
 * Created by Administrator on 23/Nov/2015.
 */
public class BPM_Analysis {


    private String AnalysisName;

    private ArrayList<ItemList> mItemListArray;

    public BPM_Analysis(String AnalysisName, ArrayList<ItemList> mItemListArray) {
        super();
        this.AnalysisName = AnalysisName;
        this.mItemListArray = mItemListArray;
    }

    public String getAnalysisName() {
        return AnalysisName;
    }

    public void setAnalysisName(String AnalysisName) {
        this.AnalysisName = AnalysisName;
    }

    public ArrayList<ItemList> getmItemListArray() {
        return mItemListArray;
    }

    public void setmItemListArray(ArrayList<ItemList> mItemListArray) {
        this.mItemListArray = mItemListArray;
    }

    public static class ItemList {
        private String title,systolic,diastolic,pulse,weight,max,min,avg,trend,sys_max,sys_min,avg_sys,dia_max,dia_min,avg_dia,max_pulse,min_pulse,avg_pulse,max_wt,min_wt,avg_wt;
//        ,sys_img,pu_img,dia_img,wt_img,,,;




        public ItemList(String title, String systolic,String diastolic,String pulse,String weight,String max,String min,String avg,String trend,String sys_max,String sys_min,String avg_sys,String dia_max,String dia_min,String avg_dia,String max_pulse,String min_pulse,String avg_pulse,String max_wt,String min_wt,String avg_wt) {
            super();
            this.title = title;
            this.systolic = systolic;
            this.diastolic = diastolic;
            this.pulse = pulse;
            this.weight = weight;
            this.max = max;
            this.min = min;
            this.avg = avg;
            this.trend = trend;
            this.sys_max = sys_max;
            this.sys_min = sys_min;
            this.avg_sys = avg_sys;
            this.dia_max = dia_max;
            this.dia_min = dia_min;
            this.avg_dia = avg_dia;
            this.max_pulse = max_pulse;
            this.min_pulse = min_pulse;
            this.avg_pulse = avg_pulse;
            this.max_wt = max_wt;
            this.min_wt = min_wt;
            this.avg_wt = avg_wt;

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSystolic() {
            return systolic;
        }

        public void setSystolic(String systolic) {
            this.systolic = systolic;
        }

        public String getDiastolic() {
            return diastolic;
        }

        public void setDiastolic(String diastolic) {
            this.diastolic = diastolic;
        }

        public String getPulse() {
            return pulse;
        }

        public void setPulse(String pulse) {
            this.pulse = pulse;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getAvg() {
            return avg;
        }

        public void setAvg(String avg) {
            this.avg = avg;
        }

        public String getTrend() {
            return trend;
        }

        public void setTrend(String trend) {
            this.trend = trend;
        }

        public String getSys_max() {
            return sys_max;
        }

        public void setSys_max(String sys_max) {
            this.sys_max = sys_max;
        }

        public String getSys_min() {
            return sys_min;
        }

        public void setSys_min(String sys_min) {
            this.sys_min = sys_min;
        }

        public String getAvg_sys() {
            return avg_sys;
        }

        public void setAvg_sys(String avg_sys) {
            this.avg_sys = avg_sys;
        }

        public String getDia_max() {
            return dia_max;
        }

        public void setDia_max(String dia_max) {
            this.dia_max = dia_max;
        }

        public String getDia_min() {
            return dia_min;
        }

        public void setDia_min(String dia_min) {
            this.dia_min = dia_min;
        }

        public String getAvg_dia() {
            return avg_dia;
        }

        public void setAvg_dia(String avg_dia) {
            this.avg_dia = avg_dia;
        }

        public String getMax_pulse() {
            return max_pulse;
        }

        public void setMax_pulse(String max_pulse) {
            this.max_pulse = max_pulse;
        }

        public String getMin_pulse() {
            return min_pulse;
        }

        public void setMin_pulse(String min_pulse) {
            this.min_pulse = min_pulse;
        }

        public String getAvg_pulse() {
            return avg_pulse;
        }

        public void setAvg_pulse(String avg_pulse) {
            this.avg_pulse = avg_pulse;
        }

        public String getMax_wt() {
            return max_wt;
        }

        public void setMax_wt(String max_wt) {
            this.max_wt = max_wt;
        }

        public String getMin_wt() {
            return min_wt;
        }

        public void setMin_wt(String min_wt) {
            this.min_wt = min_wt;
        }

        public String getAvg_wt() {
            return avg_wt;
        }

        public void setAvg_wt(String avg_wt) {
            this.avg_wt = avg_wt;
        }
    }
}
