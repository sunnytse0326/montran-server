package com.montran.server.repository;

import com.montran.server.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

     boolean existsByEmail(String email);

     Customer findCustomerByEmail(String email);
}