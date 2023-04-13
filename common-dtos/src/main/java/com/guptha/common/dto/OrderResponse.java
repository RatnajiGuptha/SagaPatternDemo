package com.guptha.common.dto;

import com.guptha.common.event.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

	private int userId;
	private int productId;
	private int amount;
	private int orderId;
	private OrderStatus orderStatus;
}
