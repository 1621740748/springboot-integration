package com.esb.seckill;

import java.io.Serializable;

public class SeckillOrder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3911591079798279809L;
	
	private Long id;
    private Long userId;
    private Long  orderId;
    private Long goodsId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
    
    
}
