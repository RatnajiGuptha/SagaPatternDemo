package com.guptha.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guptha.payment.entity.UserBalance;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Integer> {

}
