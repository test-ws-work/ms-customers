package br.com.wswork.module.customers.repositories;

import br.com.wswork.module.customers.entities.CustomerStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerStoreRepository extends JpaRepository<CustomerStore, Long> {
}
