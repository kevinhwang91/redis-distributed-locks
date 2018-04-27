package com.jackson.puppy.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackson.puppy.redis.domain.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTests {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void valueOperations() {

		final ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
		valueOperations.set("key1", "1");
		valueOperations.set("key2", "2");
		valueOperations.increment("key2", 2);
		Assert.assertEquals("1", valueOperations.get("key1"));
		Assert.assertEquals("4", valueOperations.get("key2"));
	}

	@Test
	public void hashOperations() {
		final HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
		final Order order = new Order();
		order.setStatus("paid");
		order.setId(1);
		order.setOrderNumber("2018");
		String orderJson = "";
		try {
			orderJson = objectMapper.writeValueAsString(order);
		} catch (JsonProcessingException e) {
			logger.info(e.getMessage());
		}
		hashOperations.put("map", "key1", "1");
		hashOperations.put("map", "key2", "2");
		hashOperations.put("map", "order", orderJson);

		hashOperations.increment("map", "key2", 2);
		final Map<String, String> map = hashOperations.entries("map");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			logger.info("key: {}, value: {}", entry.getKey(), entry.getValue());
		}
		Assert.assertEquals("1", hashOperations.get("hash", "key1"));
		Assert.assertEquals("4", hashOperations.get("hash", "key2"));
		Assert.assertEquals(orderJson, hashOperations.get("hash", "order"));
	}

	@Test
	public void setOperations() {
		final SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
		setOperations.add("set", "11");
		setOperations.add("set", "5", "6", "5", "7", "2");
		final Set<String> set = setOperations.members("set");
		for (String s : set) {
			logger.info("value: {}", s);
		}
		final Long size = setOperations.size("set");
		logger.info(size.toString());
		Assert.assertEquals(5, size.longValue());
	}

	@Test
	public void listOperations() {
		final ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
		listOperations.leftPush("list", "a");
		listOperations.leftPush("list", "b");
		listOperations.leftPush("list", "c");
		listOperations.rightPush("list", "x");
		listOperations.rightPush("list", "y");
		listOperations.rightPush("list", "z");
		final List<String> list = listOperations.range("list", 0, -1);
		final StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
		}
		logger.info(sb.toString());
		final String left1 = listOperations.leftPop("list");
		final String right1 = listOperations.rightPop("list");
		Assert.assertEquals("c", left1);
		Assert.assertEquals("z", right1);
		Assert.assertEquals(4, listOperations.size("list").longValue());
	}

	@Test
	public void zSetOperations() {
		final ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
		zSetOperations.add("zset", "Tom", 60);
		zSetOperations.add("zset", "Lucy", 98);
		zSetOperations.add("zset", "Leo", 88);
		final Set<ZSetOperations.TypedTuple<String>> zsetWithScore = zSetOperations.rangeByScoreWithScores("zset", 0, 100);
		for (ZSetOperations.TypedTuple<String> stringTypedTuple : zsetWithScore) {
			logger.info("value: {}, score: {}", stringTypedTuple.getValue(), stringTypedTuple.getScore());
		}
		final Double score = zSetOperations.incrementScore("zset", "Tom", 35);
		Assert.assertEquals(95.0, score, 0.0001);


	}
}
