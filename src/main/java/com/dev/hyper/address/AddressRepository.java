package com.dev.hyper.address;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressInfo, Long> {
}
