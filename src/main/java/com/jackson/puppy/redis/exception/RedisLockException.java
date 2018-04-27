package com.jackson.puppy.redis.exception;

/**
 * @author Kevin
 * @since 4/24/2018
 */
public class RedisLockException extends Exception {

	public int id;

	public RedisLockException(int id) {
		this.id = id;
	}

	public RedisLockException(String message, int id) {
		super(message);
		this.id = id;
	}

	public RedisLockException(String message, Throwable cause, int id) {
		super(message, cause);
		this.id = id;
	}

	public RedisLockException(Throwable cause, int id) {
		super(cause);
		this.id = id;
	}
}
