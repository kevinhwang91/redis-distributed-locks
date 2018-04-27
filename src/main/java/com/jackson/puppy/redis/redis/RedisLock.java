package com.jackson.puppy.redis.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Protocol;
import redis.clients.util.SafeEncoder;

import java.util.Collections;

/**
 * @author Kevin
 * @since 4/23/2018
 */
@Component
public class RedisLock {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	public Boolean tryLock(String key, String from, Long expire) {

		Boolean isLock = false;
		final String redisReply = stringRedisTemplate.execute((RedisCallback<String>) connection -> {
			//  等效于redis命令 set key value nx px。
			//  如果key不存在则添加这个键值对，并且设置px毫秒后超时。
			final Object object = connection.execute("set", SafeEncoder.encode(key), SafeEncoder.encode(from), SafeEncoder.encode("nx"),
					SafeEncoder.encode("px"), Protocol.toByteArray(expire));
			return object == null ? null : SafeEncoder.encode((byte[]) object);
		});
		if ("OK".equalsIgnoreCase(redisReply)) {
			isLock = true;
		}
		return isLock;
	}

	public Boolean releaseLock(String key, String from) {

		Boolean isReleaseLock = false;

		//  redis lua脚本，目的是为了保证解锁操作的原子性。
		//  判断这个键对应的值和传递的值是否一致，若一致则删除这个键值对。
		final DefaultRedisScript<Long> script = new DefaultRedisScript<>("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end", Long.TYPE);
		final Long redisReply = stringRedisTemplate.execute(script, Collections.singletonList(key), from);

		if (redisReply.equals(1L)) {
			isReleaseLock = true;
		}
		return isReleaseLock;
	}

}
