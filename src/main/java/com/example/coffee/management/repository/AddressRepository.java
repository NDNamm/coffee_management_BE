package com.example.coffee.management.repository;

import com.example.coffee.management.model.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {

    Address findAddressByOrderId(long orderId);
}
