package viroopa.com.medikart.buying.model;

/**
 * Created by prakash on 18/08/15.
 */
public class M_orderlist {

    public M_orderlist() {
    }

    String MemberId;
    String OrderDate;
    String OrderId;
    String OrderNo;
    String status;
    String DeliveryAgentId;
    String ExpectedDeliveryDate;
    String orderMonth;
    String OrderYear;
    public String getOrderYear() {
        return OrderYear;
    }

    public void setOrderYear(String orderYear) {
        OrderYear = orderYear;
    }



    public String getOrderMonth() {
        return orderMonth;
    }

    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }



    public String getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(String netAmount) {
        NetAmount = netAmount;
    }

    String NetAmount;

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryAgentId() {
        return DeliveryAgentId;
    }

    public void setDeliveryAgentId(String deliveryAgentId) {
        DeliveryAgentId = deliveryAgentId;
    }

    public String getExpectedDeliveryDate() {
        return ExpectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        ExpectedDeliveryDate = expectedDeliveryDate;
    }
}
