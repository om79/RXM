package viroopa.com.medikart.buying.model;

import java.util.ArrayList;

public class orderModel {

	private String pName;
	private String sName;
	private ArrayList<ItemList> mItemListArray;

	public orderModel(String pName, String sNAme, ArrayList<ItemList> mItemListArray) {
		super();
		this.pName = pName;
		this.sName=sNAme;
		this.mItemListArray = mItemListArray;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.sName = sName;
	}
	public String getsName() {
		return sName;
	}

	public void setsName(String pName) {
		this.pName = pName;
	}

	public ArrayList<ItemList> getmItemListArray() {
		return mItemListArray;
	}

	public void setmItemListArray(ArrayList<ItemList> mItemListArray) {
		this.mItemListArray = mItemListArray;
	}

	public static class ItemList {


		String MemberId;
		String OrderDate;
		String OrderId;
		String OrderNo;
		String status;
		String DeliveryAgentId;
		String ExpectedDeliveryDate;
		String orderMonth;
		String OrderYear;
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

		public String getOrderMonth() {
			return orderMonth;
		}

		public void setOrderMonth(String orderMonth) {
			this.orderMonth = orderMonth;
		}

		public String getOrderYear() {
			return OrderYear;
		}

		public void setOrderYear(String orderYear) {
			OrderYear = orderYear;
		}

		public String getNetAmount() {
			return NetAmount;
		}

		public void setNetAmount(String netAmount) {
			NetAmount = netAmount;
		}



		public ItemList(String MemberId, String OrderDate, String OrderId, String OrderNo, String status, String DeliveryAgentId,String ExpectedDeliveryDate,String orderMonth,String OrderYear,String NetAmount) {
			super();

			this.MemberId=MemberId;
			this.OrderDate=OrderDate;
			this.OrderId=OrderId;
			this.OrderNo=OrderNo;
			this.status=status;
			this.DeliveryAgentId=DeliveryAgentId;
			this.ExpectedDeliveryDate=ExpectedDeliveryDate;
			this.orderMonth=orderMonth;
			this.OrderYear=OrderYear;
			this.NetAmount=NetAmount;
		}


	}
}
