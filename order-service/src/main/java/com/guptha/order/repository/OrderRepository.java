package com.guptha.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guptha.order.entity.PurchaseOrder;

public interface OrderRepository extends JpaRepository<PurchaseOrder, Integer>{

}
