package com.jackson.puppy.redis.dao;

import com.jackson.puppy.redis.domain.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Kevin
 * @since 4/14/2018
 */
@Mapper
public interface OrderDao {

	@Results({
			@Result(column = "id", property = "id"),
			@Result(column = "order_number", property = "orderNumber"),
			@Result(column = "status", property = "status")
	})
	@Select("select id, order_number, status from `order`")
	List<Order> findAll();

	@Results({
			@Result(column = "id", property = "id"),
			@Result(column = "order_number", property = "orderNumber"),
			@Result(column = "status", property = "status")
	})
	@Select("select id, order_number, status from `order` where order_number = #{orderNumber}")
	Order getByOrderNumber(String orderNumber);

	@Update("update `order` set status = #{status} where order_number = #{orderNumber}")
	Integer updateByOrderNumber(Order order);

}
