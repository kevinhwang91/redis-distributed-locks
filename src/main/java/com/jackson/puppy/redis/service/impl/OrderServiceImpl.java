package com.jackson.puppy.redis.service.impl;

import com.jackson.puppy.redis.annotation.RedisDistributedLock;
import com.jackson.puppy.redis.dao.OrderDao;
import com.jackson.puppy.redis.domain.Order;
import com.jackson.puppy.redis.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kevin
 * @since 4/23/2018
 */
@Service
public class OrderServiceImpl implements OrderService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OrderDao orderDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	@RedisDistributedLock(expire = 5000, retry = 1, retryInterval = 300, keyIndex = 0)
	public Boolean pay(String orderNumber) {
		final Order order = orderDao.getByOrderNumber(orderNumber);
		if ("unpaid".equals(order.getStatus())) {
			logger.info("request to zhifubao or wechat.......");
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
			}
			order.setStatus("paid");
			orderDao.updateByOrderNumber(order);
			return true;
		}
		return false;
	}

	@Override
	public void rollBackPay(final String orderNumber) {
		final Order order = orderDao.getByOrderNumber(orderNumber);
		order.setStatus("unpaid");
		orderDao.updateByOrderNumber(order);
	}
}
