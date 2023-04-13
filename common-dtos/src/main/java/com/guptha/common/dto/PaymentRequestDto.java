package com.guptha.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentRequestDto {

	private int orderId;
	private int userId;
	private int amount;

}
