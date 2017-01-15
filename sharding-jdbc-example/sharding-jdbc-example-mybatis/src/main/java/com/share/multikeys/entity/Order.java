package com.share.multikeys.entity;

import java.util.Date;

public final class Order {
    
    private Integer orderId;
    
    private Integer userId;
    
    private String status;
    
   private Date createTime;
    
    public Integer getOrderId() {
        return orderId;
    }
    
    public void setOrderId(final Integer orderId) {
        this.orderId = orderId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(final Integer userId) {
        this.userId = userId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    
    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
    public String toString() {
        return String.format("order_id: %s, user_id: %s, status: %s", orderId, userId, status);
    }
}
