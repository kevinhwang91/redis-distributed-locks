package com.jackson.puppy.redis.domain;

/**
 * @author Kevin
 * @since 4/14/2018
 */
public class Order {

	private Integer id;

	private String orderNumber;

	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Order{" +
				"id=" + id +
				", orderNumber='" + orderNumber + '\'' +
				", status='" + status + '\'' +
				'}';
	}
}
