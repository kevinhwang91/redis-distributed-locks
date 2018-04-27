package com.jackson.puppy.redis;

import com.jackson.puppy.redis.service.OrderService;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * @author Kevin
 * @since 4/25/2018
 */
public class ParallelPayThread implements Callable<Boolean> {

	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	private CountDownLatch start;

	private CountDownLatch finish;

	private int taskId;

	private String orderNumber;

	private OrderService orderService;

	public ParallelPayThread(CountDownLatch start, CountDownLatch finish, int taskId, String orderNumber, OrderService orderService) {
		this.start = start;
		this.finish = finish;
		this.taskId = taskId;
		this.orderNumber = orderNumber;
		this.orderService = orderService;
	}

	@Override
	public Boolean call() {
		Boolean isSucceed = false;
		try {
			start.await();
			isSucceed = orderService.pay(orderNumber);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			finish.countDown();
		}
		return isSucceed;
	}
}
