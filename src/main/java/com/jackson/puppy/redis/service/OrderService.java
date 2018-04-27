package com.jackson.puppy.redis.service;

public interface OrderService {

	Boolean pay(String orderNumber) throws Exception;

	void rollBackPay(String orderNumber);
}
