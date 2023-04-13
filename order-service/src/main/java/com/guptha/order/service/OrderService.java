package com.guptha.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guptha.common.dto.OrderRequestDto;
import com.guptha.common.event.OrderStatus;
import com.guptha.order.entity.PurchaseOrder;
import com.guptha.order.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private OrderStatusPublisher orderStatusPublisher;

	@Transactional
	public PurchaseOrder createOrder(OrderRequestDto orderRequestDto) {
		PurchaseOrder order = orderRepo.save(convertDtoToEntity(orderRequestDto));
		orderRequestDto.setOrderId(order.getId());

		orderStatusPublisher.publishOrderEvent(orderRequestDto, OrderStatus.ORDER_CREATED);
		return order;
	}

	public List<PurchaseOrder> getAllOrders() {
		return orderRepo.findAll();
	}

	private PurchaseOrder convertDtoToEntity(OrderRequestDto dto) {
		PurchaseOrder order = new PurchaseOrder();
		order.setOrderStatus(OrderStatus.ORDER_CREATED);
		order.setUserId(dto.getUserId());
		order.setPrice(dto.getAmount());
		order.setProductId(dto.getProductId());
		return order;
	}

}
