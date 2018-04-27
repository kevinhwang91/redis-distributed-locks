package com.jackson.puppy.redis;

import com.jackson.puppy.redis.dao.OrderDao;
import com.jackson.puppy.redis.domain.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTests {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OrderDao orderDao;

	@Test
	public void findAllOrder() {
		final List<Order> orders = orderDao.findAll();
		for (Order order : orders) {
			logger.info("id={}, orderNumber={}, status={}", order.getId(), order.getOrderNumber(), order.getStatus());
		}
	}

	@Test
	@Transactional
	public void updateByOrderNumber() {
		final String orderNumber = "201804241200000001";
		final Order order = orderDao.getByOrderNumber(orderNumber);
		order.setStatus("test status");
		orderDao.updateByOrderNumber(order);
		final Order newOrder = orderDao.getByOrderNumber(orderNumber);
		Assert.assertEquals(order.getStatus(), newOrder.getStatus());
	}

}
