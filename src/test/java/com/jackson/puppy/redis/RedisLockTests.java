package com.jackson.puppy.redis;

import com.jackson.puppy.redis.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisLockTests {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OrderService orderService;

	@Test
	@Transactional
	public void useRedisLock() {
		try {
			orderService.pay("201804241200000002");
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	@Test
	public void useRedisLockParallel() {
		//  @RedisDistributedLock(expire = 5000, retry = 1, retryInterval = 300, keyIndex = 0)
		//	可以修改expire = 1，让锁超时失效抛异常，业务数据回滚或者注释掉这个redis锁注解，会有并发问题。测试均会不通过。
		int parallel = 1000;
		int succeedCount = 0;
		final String orderNumber = "201804241200000002";
		//  start和finish信号是为了让多线程同时并发
		CountDownLatch start = new CountDownLatch(1);
		CountDownLatch finish = new CountDownLatch(parallel);
		final ExecutorService threadPool = Executors.newCachedThreadPool();
		final List<Future<Boolean>> futures = new ArrayList<>();
		for (int index = 0; index < parallel; index++) {
			ParallelPayThread thread = new ParallelPayThread(start, finish, index, orderNumber, orderService);
			final Future<Boolean> future = threadPool.submit(thread);
			futures.add(future);
		}
		start.countDown();
		try {
			finish.await();
			for (final Future<Boolean> future : futures) {
				if (Boolean.TRUE.equals(future.get())) {
					succeedCount++;
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.info(e.getMessage());
		}
		// 没有具体实际意义，只是为了回滚数据，方便下次跑测试。
		// 若修改expire或者注释锁注解，可以注释掉这条语句查看order表status的数据。
		orderService.rollBackPay(orderNumber);
		Assert.assertEquals(1, succeedCount);
	}

}
