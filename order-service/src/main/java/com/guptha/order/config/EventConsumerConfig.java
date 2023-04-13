package com.guptha.order.config;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.guptha.common.event.PaymentEvent;

@Configuration
public class EventConsumerConfig {

	@Autowired
	private OrderStatusHandler handler;

	@Bean
	public Consumer<PaymentEvent> paymentEventConsumer() {
		return (payment) -> handler.updateOrder(payment.getPaymentRequestDto().getOrderId(),
				po -> po.setPaymentStatus(payment.getPaymentStatus()));
	}
}
