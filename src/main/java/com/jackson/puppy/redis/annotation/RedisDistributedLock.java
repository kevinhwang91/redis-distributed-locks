package com.jackson.puppy.redis.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisDistributedLock {

	int keyIndex() default 0;

	long expire() default 0;

	int retry() default 1;

	long retryInterval() default 1000;
}
