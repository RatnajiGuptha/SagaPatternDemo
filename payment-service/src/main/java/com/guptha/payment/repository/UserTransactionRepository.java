package com.guptha.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guptha.payment.entity.UserTransaction;

public interface UserTransactionRepository extends JpaRepository<UserTransaction, Integer> {

}
