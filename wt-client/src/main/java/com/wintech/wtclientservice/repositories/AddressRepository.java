package com.wintech.wtclientservice.repositories;

import com.wintech.wtclientservice.domains.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long>{
}
