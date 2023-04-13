package com.guptha.order.config;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.guptha.common.dto.OrderRequestDto;
import com.guptha.common.event.OrderStatus;
import com.guptha.common.event.PaymentStatus;
import com.guptha.order.entity.PurchaseOrder;
import com.guptha.order.repository.OrderRepository;
import com.guptha.order.service.OrderStatusPublisher;

import jakarta.transaction.Transactional;

@Configuration
public class OrderStatusHandler {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderStatusPublisher orderStatusPublisher;

	@Transactional
	public void updateOrder(int id, Consumer<PurchaseOrder> consumer) {
		orderRepository.findById(id).ifPresent(consumer.andThen(this::updateOrder));
	}

	private void updateOrder(PurchaseOrder purchaseorder) {
		boolean isPaymentComplete = PaymentStatus.PAYMENT_COMPLETED.equals(purchaseorder.getPaymentStatus());
		OrderStatus orderSatus = isPaymentComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
		purchaseorder.setOrderStatus(orderSatus);

		if (!isPaymentComplete) {
			orderStatusPublisher.publishOrderEvent(convertEntityToDto(purchaseorder), orderSatus);
		}
	}

	public OrderRequestDto convertEntityToDto(PurchaseOrder purchaseorder) {
		OrderRequestDto dto = new OrderRequestDto();
		dto.setOrderId(purchaseorder.getId());
		dto.setUserId(purchaseorder.getUserId());
		dto.setProductId(purchaseorder.getProductId());
		dto.setAmount(purchaseorder.getPrice());
		return dto;

	}

}
