package viroopa.com.medikart.common;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class DayWithMonthAxisValueFormatter implements AxisValueFormatter
{

  /*  protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };*/

    private BarLineChartBase<?> chart;

    public DayWithMonthAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        float days = (float) value;


        String numberD = String.valueOf(days);
       // numberD = numberD.substring( numberD.indexOf ( "." ) );


        String[] split = numberD.split("\\."); // escape .
        String firstSubString = split[0];
        String secondSubString = split[1];


        String s="Android_a_b.pdf";
        String[] parts = s.split("\\."); // escape .
        String part1 = parts[0];
        String part2 = parts[1];


            return firstSubString+" "+secondSubString;

    }





    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
