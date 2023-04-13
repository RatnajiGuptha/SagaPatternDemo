package com.guptha.payment.servcie;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guptha.common.dto.OrderRequestDto;
import com.guptha.common.dto.PaymentRequestDto;
import com.guptha.common.event.OrderEvent;
import com.guptha.common.event.PaymentEvent;
import com.guptha.common.event.PaymentStatus;
import com.guptha.payment.entity.UserBalance;
import com.guptha.payment.entity.UserTransaction;
import com.guptha.payment.repository.UserBalanceRepository;
import com.guptha.payment.repository.UserTransactionRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class PaymentService {

	@Autowired
	private UserBalanceRepository userBalanceRepository;

	@Autowired
	private UserTransactionRepository userTransactionRepository;

	@PostConstruct
	public void initUserBalanceInDB() {
		userBalanceRepository
				.saveAll(Stream.of(new UserBalance(101, 5000), new UserBalance(102, 1000), new UserBalance(103, 15000))
						.collect(Collectors.toList()));
	}

	@Transactional
	public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
		OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();
		PaymentRequestDto paymentRequestDto = new PaymentRequestDto(orderRequestDto.getOrderId(),
				orderRequestDto.getUserId(), orderRequestDto.getAmount());

		return userBalanceRepository.findById(orderRequestDto.getUserId())
				.filter(ub -> ub.getPrice() > orderRequestDto.getAmount()).map(ub -> {
					ub.setPrice(ub.getPrice() - orderRequestDto.getAmount());
					userTransactionRepository.save(new UserTransaction(orderRequestDto.getOrderId(),
							orderRequestDto.getUserId(), orderRequestDto.getAmount()));
					return new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_COMPLETED);
				}).orElse(new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_FAILED));
	}

	@Transactional
	public void cancelOrderEvent(OrderEvent orderEvent) {

		userTransactionRepository.findById(orderEvent.getOrderRequestDto().getOrderId()).ifPresent(ut -> {
			userTransactionRepository.delete(ut);
			userTransactionRepository.findById(ut.getUserId())
					.ifPresent(ub -> ub.setAmount(ub.getAmount() + ut.getAmount()));
		});

	}

}
