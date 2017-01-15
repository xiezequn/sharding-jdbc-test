package com.share.multikeys.entity;
/** 
* @author weigen.ye 
* @date 创建时间：2016年10月8日 下午5:43:00 
*
*/
public class Item {
    
	private Integer itemId;
	private Integer orderId;
	private String itemName;
	
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
}
