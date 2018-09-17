package viroopa.com.medikart.common;

/**
 * Created by 1144 on 12-10-2016.
 */

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;

public class Style {

    public static void headerCellStyle(PdfPCell cell){

        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        // padding
        cell.setPaddingTop(5f);
        cell.setPaddingBottom(5f);

        // background color
        cell.setBackgroundColor(new BaseColor(0, 102, 0));


        // border
        cell.setBorder(0);
        cell.setBorderWidthBottom(1f);
        cell.setBorderColor(new BaseColor(0, 102, 0));
    }
    public static void labelCellStyle(PdfPCell cell){
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingLeft(3f);
        cell.setPaddingTop(0f);

        // background color
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        // border
        cell.setBorder(0);
        cell.setBorderWidthBottom(1);
        cell.setBorderColorBottom(BaseColor.GRAY);

        // height
        cell.setMinimumHeight(18f);
    }

    public static void valueCellStyle(PdfPCell cell){
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(0f);
        cell.setPaddingBottom(5f);

        // border
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.5f);

        // height
        cell.setMinimumHeight(18f);
    }

    public static void rowStyle(PdfPCell cell){
        // alignment
      //  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(4f);

        // border
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(new BaseColor(0, 102, 0));
        // cell.setBorderWidthBottom(0.5f);

        // height
        cell.setMinimumHeight(18f);
    }
    public static void rowStyle_taken(PdfPCell cell){
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(4f);

        // border
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(new BaseColor(0, 102, 0));
        cell.setBorderWidthBottom(2f);
        cell.setBorderColorBottom(BaseColor.GREEN);
        // height
        cell.setMinimumHeight(18f);
    }
    public static void rowStyle_taken_late(PdfPCell cell){
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(4f);

        // border
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(new BaseColor(0, 102, 0));
        cell.setBorderWidthBottom(2f);
        cell.setBorderColorBottom(BaseColor.RED);
        // height
        cell.setMinimumHeight(18f);
    }
    public static void rowStyle_taken_early(PdfPCell cell){
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(4f);

        // border
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(new BaseColor(0, 102, 0));
        cell.setBorderWidthBottom(2f);
        cell.setBorderColorBottom(BaseColor.YELLOW);
        // height
        cell.setMinimumHeight(18f);
    }

    public static void rowStyle_pending(PdfPCell cell){
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(4f);

        // border
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(new BaseColor(0, 102, 0));
        cell.setBorderWidthBottom(2f);
        cell.setBorderColorBottom(BaseColor.ORANGE);
        // height
        cell.setMinimumHeight(18f);
    }
    public static void rowStyle_skiped(PdfPCell cell){
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(4f);

        // border
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(new BaseColor(0, 102, 0));
        cell.setBorderWidthBottom(2f);
        cell.setBorderColorBottom(BaseColor.RED);
        // height
        cell.setMinimumHeight(18f);
    }
}